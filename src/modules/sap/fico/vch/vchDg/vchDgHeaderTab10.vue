<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col label="业务单号" :span="8" v-model="objData.header.bussDocId" disabled />
      <szd-input-form-item-col label="业务类型" v-model="objData.header.bstp" disabled :span="8" :descSpan="14" :desc="objData.header.bstpName" />
      <szd-date-form-item-col label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" :span="8" />
      <szd-select-form-item-col label="单据状态" :span="8" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled clearable />
      <szd-select-form-item-col label="货币" :span="8" v-model="objData.header.curr" baseField="CORE_CURR" :szdopt="SR.szdopt" required clearable />
      <szd-input-form-item-col label="申请金额" :span="8" v-model="objData.header.amtApply" type="number" numDot :numDec="2" disabled />
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
        label="部门"
        :span="8"
        v-model="objData.header.cstc"
        :szdopt="SR.szdopt"
        required
        :descSpan="14"
        :desc="objData.header.cstcName"
        readonly
        clearable
        showHelp
        @help="szdSh({ a: 'CORE_CSTC', b: objData.header, c: ['cstc-cstc', 'cstcName-cstcName'] })" />
      <szd-input-form-item-col label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :span="8" :maxlength="255" required />
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
    props: ["objData", "SR", "wfOper"],
    data() {
      return {};
    },
    methods: {}
  };
</script>
