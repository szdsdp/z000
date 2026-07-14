package com.szd.z000.web.z1.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.domain.common.ClientResult;
import com.szd.core.client.domain.wf.*;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.security.ClientSecurity;
import com.szd.z000.service.common.utils.Z000Util;
import com.szd.z000.base.orm.domain.z1.Z1Header;
import com.szd.z000.base.orm.domain.z1.Z1Item;
import com.szd.z000.base.orm.domain.z1.Z1RpParam;
import com.szd.z000.web.z1.domain.Z1DataVO;
import com.szd.z000.web.z1.domain.Z1ParamVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Z1功能业务逻辑
 */
@Service
public class Z1Service {
    private static final Logger logger = LoggerFactory.getLogger(Z1Service.class);
    @Resource
    private ClientCore clientCore;
    @Resource
    private Z1HeaderService z1HeaderService;
    @Resource
    private Z1ItemService z1ItemService;

    public List<Z1Header> selectList(Z1RpParam param) {
        return z1HeaderService.selectList(param);
    }

    /**
    * 查询详情
    */
    public Z1DataVO selectData(String bussId) {
        Z1DataVO z1DataVO = new Z1DataVO();
        // 抬头
        Z1Header header = z1HeaderService.selectById(bussId);
        // 行项目
        List<Z1Item> itemList = z1ItemService.selectList(bussId);
        z1DataVO.setHeader(header);
        z1DataVO.setItemList(itemList);
        return z1DataVO;
    }

    /**
    * 保存数据
    */
    public ClientWfReturn saveData(Z1ParamVO z1ParamVO) {
        ClientWfReturn clientWfReturn = new ClientWfReturn();
        // 作废不校验
        if ("btn_delete".equals(z1ParamVO.getWfEvt().getOperate())) {
            // 重新查询数据库数据
            z1ParamVO.setData(selectData(z1ParamVO.getData().getHeader().getBussId()));
        } else {
            // 检查业务数据
            checkData(z1ParamVO);
        }
        // 保存业务数据
        saveBussWfData(z1ParamVO, clientWfReturn);
        return clientWfReturn;
    }

    /**
     * 数据保存-业务数据及工作流
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussWfData(Z1ParamVO z1ParamVO, ClientWfReturn clientWfReturn) {
        // 保存业务数据
        saveBussData(z1ParamVO);
        // 审批
        ClientWfReturn wfReturn = approval(z1ParamVO.getData(), z1ParamVO.getWfEvt());
        // 获取业务主体
        ClientWfBussBase wfBussBase = wfReturn.getWfBussBase();
        // 复制工作流返回消息
        BeanUtils.copyProperties(wfReturn, clientWfReturn);
        clientWfReturn.setBussId(z1ParamVO.getData().getHeader().getBussId());
        clientWfReturn.setBussDocId(z1ParamVO.getData().getHeader().getBussDocId());
        // 根据工作流状态更新业务单据
        if (!"E".equals(wfReturn.getRetCodeF())) {
            //草稿状态的单据，作废时直接删除
            if ("btn_delete".equals(z1ParamVO.getWfEvt().getOperate())
                    && "A".equals(z1ParamVO.getData().getHeader().getBussStatus())) {
                deleteById(clientWfReturn.getBussId());
            } else {
                // 工作流状态转换为业务状态
                String bussStatus = Z000Util.getBussStatus(wfBussBase.getStatus(), wfBussBase.getRejectStatus());
                z1ParamVO.getData().getHeader().setBussStatus(bussStatus);
                z1ParamVO.getData().getHeader().setBussDocId(wfReturn.getBussDocId());
                z1HeaderService.updateStatusAndDocId(z1ParamVO.getData().getHeader());
            }
        }
    }

    /**
     * 保存草稿
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBussData(Z1ParamVO z1ParamVO) {
        try {
            // 业务数据
            Z1DataVO data = z1ParamVO.getData();
            Z1Header header = data.getHeader();
            // 首次保存
            if (StringUtils.isBlank(header.getBussId())) {
                // 单据id
                String bussId = clientCore.tool.getNumSeq();
                header.setBussId(bussId);
                header.setBussId(bussId);
                header.setCreateBy(ClientSecurity.getUserId());
                header.setCreateTime(new Date());
                header.setBussStatus("A");
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 新增
                insert(data, z1ParamVO.getWfEvt().getOperate());
            } else {
                header.setUpdateBy(ClientSecurity.getUserId());
                header.setUpdateTime(new Date());
                // 保存业务主体
                Z000Util.saveBussBase(header);
                // 更新
                update(data, z1ParamVO.getWfEvt().getOperate());
            }
        } catch (Exception e) {
            logger.error("保存失败", e);
            throw new ClientCustomException("保存失败:" + e.getMessage());
        }
    }

    /**
     * 流程审批
     */
    public ClientWfReturn approval(Z1DataVO data, ClientWfParamEvt wfEvt) {
        ClientWfReturn wfReturn = new ClientWfReturn();
        try {
            Z1Header header = data.getHeader();
            String bussId = header.getBussId();
            // 设置流程字段
            ClientWfParamBuss wfParamBuss = Z000Util.setWfParamBuss(header, bussId);
            // 调用工作流
            ClientWfParam wfParam = new ClientWfParam(wfEvt, wfParamBuss);
            wfReturn = clientCore.wf.wfCall(wfParam);
        } catch (Exception e) {
            wfReturn.setRetCodeF("E");
            wfReturn.setRetMsg(e.getMessage());
        }
        return wfReturn;
    }

    /**
     * 业务数据新增
     */
    void insert(Z1DataVO data, String operate) {
        z1HeaderService.insert(data.getHeader());
        z1ItemService.save(data.getHeader().getBussId(), data.getItemList());
    }

    /**
     * 业务数据更新
     */
    void update(Z1DataVO data, String operate) {
        String bussId = data.getHeader().getBussId();
        // 查询历史抬头
        Z1Header headerOld = z1HeaderService.selectById(bussId);
        List<Z1Item> itemListOld = z1ItemService.selectList(bussId);
        // 更新抬头
        z1HeaderService.update(data.getHeader());
        z1ItemService.save(bussId, data.getItemList());
        // 保存修改日志
        saveLog(data, headerOld, itemListOld, operate);
    }

    /**
    * 记录日志
    */
    private void saveLog(Z1DataVO data, Z1Header headerOld, List<Z1Item> itemListOld, String operate) {
        Z1Header header = data.getHeader();
        clientCore.tool.clogSaveA(header.getBussId(), "z000_header", operate, header, headerOld);
        clientCore.tool.clogSaveB(header.getBussId(), "itemNo", "z000_item", operate, data.getItemList(), itemListOld);
    }

    void deleteById(String bussId) {
        z1HeaderService.deleteById(bussId);
        z1ItemService.deleteById(bussId);
    }

    /**
    * 数据检查
    */
    public ClientResult check(Z1ParamVO z1ParamVO) {
        ClientResult ret = new ClientResult();
        checkData(z1ParamVO);
        ret.setStatus("S");
        ret.setMsg("检查通过");
        return ret;
    }

    /**
    * 数据检查
    */
    private void checkData(Z1ParamVO z1ParamVO) {
        z1HeaderService.checkData(z1ParamVO.getData().getHeader());
        z1ItemService.checkData(z1ParamVO.getData().getItemList());
    }
}
