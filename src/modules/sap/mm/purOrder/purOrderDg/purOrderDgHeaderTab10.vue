<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col label="业务单号" :span="8" v-model="objData.header.bussDocId" disabled />
      <szd-input-form-item-col
        label="采购申请业务单号"
        :span="8"
        v-model="objData.header.applyBussDocId"
        disabled
        dblclick
        @dblclick="handleGoToApply" />
      <szd-input-form-item-col label="业务类型" v-model="objData.header.bstp" disabled :span="8" :descSpan="14" :desc="objData.header.bstpName" />
      <szd-date-form-item-col label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" :span="8" />
      <szd-select-form-item-col label="单据状态" :span="8" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled clearable />
      <szd-input-form-item-col
        label="创建人"
        v-model="objData.header.createBy"
        disabled
        :szdopt="SR.szdopt"
        :span="8"
        :descSpan="14"
        :desc="objData.header.createByName" />
      <szd-input-form-item-col label="创建时间" :span="8" v-model="objData.header.createTime" disabled />
      <szd-input-form-item-col
        label="公司代码"
        :span="8"
        v-model="objData.header.cmpy"
        :szdopt="SR.szdopt"
        required
        :descSpan="14"
        :desc="objData.header.cmpyName"
        readonly
        clearable
        showHelp
        @help="szdSh({ a: 'CORE_CMPY', b: objData.header, c: ['cmpy-cmpy', 'cmpyName-cmpyName'] })" />
      <szd-input-form-item-col
        label="成本中心"
        v-model="objData.header.cstc"
        disabled
        required
        :szdopt="SR.szdopt"
        :span="8"
        :descSpan="14"
        :desc="objData.header.cstcName" />
      <szd-select-form-item-col
        label="采购凭证类型"
        v-model="objData.header.vchType"
        baseField="Z000_MM_VCH_TYPE"
        :szdopt="SR.szdopt"
        clearable
        required
        :span="8" />
      <szd-input-form-item-col
        label="供应商账号"
        :span="8"
        v-model="objData.header.supp"
        :szdopt="SR.szdopt"
        required
        :descSpan="14"
        :desc="objData.header.suppName"
        readonly
        clearable
        showHelp
        @help="szdSh({ a: 'CORE_SUPP', b: objData.header, c: ['supp-supp', 'suppName-suppName'] })" />
      <szd-select-form-item-col
        label="采购组织"
        v-model="objData.header.purOrg"
        baseField="Z000_MDM_ORG_PUR"
        :szdopt="SR.szdopt"
        required
        :span="8"
        clearable />
      <szd-select-form-item-col
        label="采购组"
        v-model="objData.header.purGroup"
        baseField="Z000_MM_CGDD_CGZ"
        :szdopt="SR.szdopt"
        required
        :span="8"
        clearable />
      <szd-select-form-item-col label="货币码" v-model="objData.header.curr" baseField="CORE_CURR" :szdopt="SR.szdopt" required :span="8" clearable />
      <szd-date-form-item-col label="采购凭证日期" v-model="objData.header.vchDate" :szdopt="SR.szdopt" :span="8" required />
      <szd-input-form-item-col label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :span="8" :maxlength="255" />
    </el-row>
    <el-row>
      <szd-textarea-form-item-col
        required
        label="抬头文本"
        :span="16"
        label-width="12.5%"
        v-model="objData.header.textLine"
        :szdopt="SR.szdopt"
        :maxlength="132"
        :rows="2" />
    </el-row>
    <el-row>
      <szd-textarea-form-item-col
        label="备注"
        :span="16"
        label-width="12.5%"
        v-model="objData.header.remark"
        :szdopt="SR.szdopt"
        :maxlength="150"
        :rows="2" />
    </el-row>
  </el-form>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    data() {
      return {};
    },
    methods: {
      //跳转单据
      handleGoToApply() {
        this.szdWf.goDoc("z000_mm_cgsq_01a", this.objData.header.applyId, "V");
      }
    }
  };
</script>
