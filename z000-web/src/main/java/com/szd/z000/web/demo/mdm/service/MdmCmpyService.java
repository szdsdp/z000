package com.szd.z000.web.demo.mdm.service;

import com.szd.core.client.ClientCore;
import com.szd.core.client.exception.ClientCustomException;
import com.szd.core.client.util.ClientUtilString;
import com.szd.z000.base.orm.domain.demo.mdm.MdmCmpy;
import com.szd.z000.base.orm.mapper.demo.mdm.MdmCmpyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service业务层处理
 *
 * @author duanlizeng
 */
@Service
public class MdmCmpyService {
    private static final Logger logger = LoggerFactory.getLogger(MdmCmpyService.class);
    @Resource
    private MdmCmpyMapper mdmCmpyMapper;
    @Resource
    private ClientCore clientCore;

    public List<MdmCmpy> selectList(MdmCmpy mdmCmpy) {
        return mdmCmpyMapper.selectList(mdmCmpy);
    }

    public MdmCmpy selectById(String cmpy) {
        return mdmCmpyMapper.selectById(cmpy);
    }

    public void insert(MdmCmpy mdmCmpy) {
        checkData(mdmCmpy);
        try {
            mdmCmpyMapper.insert(mdmCmpy);
        } catch (Exception e) {
            logger.error("新增公司失败", e);
            throw new ClientCustomException(e.getMessage());
        }
    }

    public void update(MdmCmpy mdmCmpy) {
        MdmCmpy exists = selectById(mdmCmpy.getCmpy());
        if (exists == null) {
            throw new ClientCustomException("公司" + mdmCmpy.getCmpy() + "不存在");
        }
        try {
            mdmCmpyMapper.update(mdmCmpy);
        } catch (Exception e) {
            logger.error("更新公司失败", e);
            throw new ClientCustomException(e.getMessage());
        }
    }

    public void deleteByIds(String[] ids) {
        mdmCmpyMapper.deleteByIds(ids);
    }

    private void checkData(MdmCmpy mdmCmpy) {
        if (mdmCmpy == null || ClientUtilString.isBlank((mdmCmpy.getCmpy()))
                || ClientUtilString.isBlank((mdmCmpy.getCmpyName()))
                || ClientUtilString.isBlank((mdmCmpy.getAccSet()))) {
            throw new ClientCustomException("新增公司失败，编码、名称或账套不能为空");
        }
        MdmCmpy exists = selectById(mdmCmpy.getCmpy());
        if (exists != null) {
            throw new ClientCustomException("新增公司失败，" + mdmCmpy.getCmpy() + "已存在");
        }
        clientCore.mdm.checkAE("CORE_VCH_ACCS", mdmCmpy.getAccSet(), "账套", "0");
    }
}
