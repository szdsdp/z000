package com.szd.z000.web.sap.fico.vch.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.vch.*;
import com.szd.core.client.util.ClientUtilDate;
import com.szd.z000.base.orm.domain.fico.vch.VchAdjust;
import com.szd.z000.base.orm.domain.fico.vch.VchHeader;
import com.szd.z000.base.orm.domain.fico.vch.VchItem;
import com.szd.z000.common.sap.domain.vch.SapVchCtr;
import com.szd.z000.common.sap.domain.vch.SapVchCtrHeader;
import com.szd.z000.common.sap.domain.vch.SapVchCtrItem;
import com.szd.z000.common.sap.domain.service.SapVchCreateService;
import com.szd.z000.web.sap.fico.vch.domain.VchDataVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 费用报账-SAP接口服务
 */
@Service
public class VchSapService {
    private static final Logger logger = LoggerFactory.getLogger(VchSapService.class);

    @Resource
    private VchService vchService;
    @Resource
    private VchHeaderService vchHeaderService;
    @Resource
    private SapVchCreateService sapVchCreateService;
    @Resource
    private ClientCore clientCore;

    /**
     * 同步SAP凭证
     */
    public ClientResult genSapVch(String costId) {
        ClientResult clientResult = new ClientResult();
        // 状态校验
        VchHeader header = vchHeaderService.selectObjById(costId);
        if (header == null || !"D".equals(header.getBussStatus())) {
            clientResult.setStatus("W");
            clientResult.setMsg("单据状态不支持同步");
            return clientResult;
        }
        // 凭证准备
        ClientVch clientVch = vchPrepare(costId, clientResult);
        if ("E".equals(clientResult.getStatus())) {
            return clientResult;
        }
        // 映射为SAP请求格式
        SapVchCtr sapCtr = mapToSapCtr(header, clientVch);
        // 调用SAP接口
        clientResult = sapVchCreateService.createVch(sapCtr, costId);
        // 更新状态
        if ("S".equals(clientResult.getStatus())) {
            header.setBussStatus("F");
            vchHeaderService.updateStatusAndDocId(header);
        }
        if (clientResult.getRetMsgBox() == null) {
            clientResult.setRetMsgBox(new ArrayList<>());
        }
        return clientResult;
    }

    /**
     * 凭证准备（复用SEXP vchPrepare逻辑）
     */
    public ClientVch vchPrepare(String costId, ClientResult clientResult) {
        VchDataVO dataVO = vchService.selectData(costId);
        VchHeader header = dataVO.getHeader();
        // 组装凭证参数
        ClientVchParam clientVchParam = new ClientVchParam();
        ClientVchParamHead clientVchParamHead = genVchParamHead(header);
        List<ClientVchParamItem> clientVchParamItemList = genVchParamItem(dataVO);
        clientVchParam.setHeader(clientVchParamHead);
        clientVchParam.setItemList(clientVchParamItemList);
        // 调用核心凭证生成
        ClientVch clientVch = clientCore.vch.vchGenEntry(clientVchParam);
        if ("E".equals(clientVch.getStatus())) {
            clientResult.setStatus("E");
            clientResult.setMsg(clientVch.getMsg());
            return clientVch;
        }
        // 重排行号
        int itemNo = 1;
        for (ClientVchItem vchItem : clientVch.getItemList()) {
            vchItem.setVchItem(itemNo);
            itemNo++;
        }
        return clientVch;
    }

    /**
     * 凭证抬头赋值
     */
    private ClientVchParamHead genVchParamHead(VchHeader header) {
        ClientVchParamHead head = new ClientVchParamHead();
        head.setBussDocId(header.getBussDocId());
        head.setCmpy(header.getCmpy());
        head.setBstp(header.getBstp());
        head.setHeadText(header.getTitle());
        head.setCurr(header.getCurr());
        return head;
    }

    /**
     * 凭证行项目赋值（行项目 + 调整分录）
     */
    private List<ClientVchParamItem> genVchParamItem(VchDataVO dataVO) {
        List<ClientVchParamItem> list = new ArrayList<>();
        VchHeader header = dataVO.getHeader();
        // 行项目
        genVchParamItemFromItem(dataVO.getItemList(), header, list);
        // 凭证调整分录
        genVchParamItemFromAdjust(dataVO.getAdjustList(), header,list);
        return list;
    }

    /**
     * 行项目 → 凭证行项目
     */
    private void genVchParamItemFromItem(List<VchItem> itemList, VchHeader header, List<ClientVchParamItem> list) {
        if (itemList == null) {
            return;
        }
        for (VchItem item : itemList) {
            ClientVchParamItem paramItem = new ClientVchParamItem();
            paramItem.setBsub(item.getBsubl());
            paramItem.setAmt01(item.getAmt());
            paramItem.setAmt02(item.getAmtNoTax());
            paramItem.setAmt03(item.getAmtTax());
            paramItem.setAssignNo(header.getBussDocId());
            paramItem.setSummary(header.getTitle());
            paramItem.setCstc(header.getCstc());
            paramItem.setTaxRate(item.getTaxr());
            list.add(paramItem);
        }
    }

    /**
     * 凭证调整分录 → 凭证行项目
     */
    private void genVchParamItemFromAdjust(List<VchAdjust> adjustList,VchHeader header, List<ClientVchParamItem> list) {
        if (adjustList == null) {
            return;
        }
        for (VchAdjust item : adjustList) {
            ClientVchParamItem paramItem = new ClientVchParamItem();
            BeanUtils.copyProperties(item, paramItem);
            paramItem.setBsub("");
            paramItem.setVchAmt(item.getAmt());
            paramItem.setSummary(header.getTitle());
            list.add(paramItem);
        }
    }

    /**
     * ClientVch → SapVchCtr 映射
     */
    private SapVchCtr mapToSapCtr(VchHeader header, ClientVch clientVch) {
        ClientVchHead clientVchHead = clientVch.getHeader();
        List<ClientVchItem> clientVchItemList = clientVch.getItemList();
        SapVchCtr sapCtr = new SapVchCtr();
        // 抬头映射
        SapVchCtrHeader sapHeader = new SapVchCtrHeader();
        sapHeader.setIfDocno(header.getBussDocId());
        sapHeader.setBukrs(header.getCmpy());
        //  凭证类型
//        sapHeader.setBlart(clientVchHead.getVchType());
        sapHeader.setBlart("SA");
        sapHeader.setBktxt(header.getTitle());
        sapHeader.setWaers(clientVchHead.getCurr());
        sapHeader.setKursf("");
        sapHeader.setXblnr("");
        sapHeader.setBudat(ClientUtilDate.dateToSap(clientVchHead.getPostDate()));
        sapHeader.setBldat(ClientUtilDate.dateToSap(clientVchHead.getVchDate()));
        sapHeader.setUsnam("");
        sapHeader.setNumpg("");
        sapCtr.setHeader(sapHeader);
        // 行项目映射
        List<SapVchCtrItem> sapItemList = new ArrayList<>();
        if (clientVchItemList != null) {
            for (ClientVchItem vchItem : clientVchItemList) {
                SapVchCtrItem sapItem = new SapVchCtrItem();
                // Y 行项目数
                sapItem.setBuzei(String.valueOf(vchItem.getVchItem()));
                // 记帐代码
//                sapItem.setBschl(vchItem.getPstky());
                // Y 借方/贷方标识
                sapItem.setShkzg(vchItem.getDrCr());
                // Y 凭证货币金额
                sapItem.setWrbtr(vchItem.getVchAmt() != null ? vchItem.getVchAmt().toString() : null);
                // 按本位币计的金额
                sapItem.setDmbtr(String.valueOf(vchItem.getVchAmt()));
                // 供应商
                sapItem.setLifnr(vchItem.getSupp());
                // 客户
                sapItem.setKunnr(vchItem.getCust());
                // 总账科目
                sapItem.setHkont(vchItem.getAccs());
                // 特殊总帐标识
                sapItem.setUmskz(vchItem.getSgl());
                // 分配编号
                sapItem.setZuonr(vchItem.getAssignNo());
                // Y 项目文本
                sapItem.setSgtxt(vchItem.getSummary());
                // 成本中心
                sapItem.setKostl(vchItem.getCstc());
                // 业务范围
                sapItem.setGsber(vchItem.getBusc());
                // 利润中心
                sapItem.setPrctr(vchItem.getPrfc());
                // 部分报表分段
                sapItem.setSegment(vchItem.getSegm());
                // 销售/购买税代码
                sapItem.setMwskz(vchItem.getTaxRate());
                // 付款原因代码
                sapItem.setRstgr(vchItem.getReas());
                // 采购凭证号
                sapItem.setEbeln(vchItem.getPoDoc());
                // 采购凭证的项目编号
                sapItem.setEbelp(vchItem.getPoDocItem() != null ? String.valueOf(vchItem.getPoDocItem()) : null);
                // 主资产号
                sapItem.setAnln1(vchItem.getAssetNo());
                // 资产次级编号
                sapItem.setAnln2(vchItem.getAssetNoSub());
                // 订单号
                sapItem.setAufnr(vchItem.getOrderCode());
                // 付款条件代码
//                sapItem.setZterm(vchItem.getPaytrm());
//                // 到期日计算基准日期
//                sapItem.setZfbdt(vchItem.getPmntDate() != null ? vchItem.getPmntDate().toString() : null);
//                // 付款方式
//                sapItem.setZlsch(vchItem.getPayMeth());
//                // 收付冻结码
//                sapItem.setZlspr(vchItem.getPmtBlock());
//                // 反记帐标识
//                sapItem.setXnegp(vchItem.getXnegp());
                sapItemList.add(sapItem);
            }
        }
        sapCtr.setItemList(sapItemList);
        return sapCtr;
    }
}
