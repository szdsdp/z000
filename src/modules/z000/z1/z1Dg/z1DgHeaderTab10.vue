<template>
  <div class="app-sub-container" style="padding: 0">
    <el-form class="app-el-form">
      <el-row>
        <szd-input-form-item-col label="业务单号" :span="8" v-model="objData.header.bussDocId" disabled />
        <szd-input-form-item-col
          label="公司"
          v-model="objData.header.cmpy"
          showHelp
          @help="szdSh({ a: 'CORE_CMPY', b: objData.header, c: ['cmpy-cmpy', 'cmpyName-cmpyName'] })"
          required
          :szdopt="SR.szdopt"
          :span="8"
          :descSpan="12"
          :desc="objData.header.cmpyName" />
        <szd-input-form-item-col
          label="部门"
          v-model="objData.header.cstc"
          showHelp
          @help="szdSh({ a: 'CORE_CSTC', b: objData.header, c: ['cstc-cstc', 'cstcName-cstcName'], d: [['cmpy', objData.header.cmpy]] })"
          required
          :szdopt="SR.szdopt"
          :span="8"
          :descSpan="12"
          :desc="objData.header.cstcName" />
      </el-row>
      <el-row>
        <szd-input-form-item-col
          label="业务人"
          v-model="objData.header.userId"
          showHelp
          @help="szdSh({ a: 'CORE_USER', b: objData.header, c: ['userId', 'userName'] })"
          required
          :szdopt="SR.szdopt"
          :span="8"
          :descSpan="12"
          :desc="objData.header.userName" />
        <szd-input-form-item-col
          label="业务类型"
          v-model="objData.header.bstp"
          required
          :szdopt="SR.szdopt"
          :span="8"
          :descSpan="12"
          :desc="objData.header.bstpName" />
        <szd-input-form-item-col
          v-if="wfOper.dyWfId || objData.header.wfDyId"
          label="动态流程"
          v-model="objData.header.wfDyId"
          showHelp
          @help="szdWf.shDyId(objData.header.bstp, objData.header)"
          readonly
          :disabled="!['A', 'B'].includes(objData.header.bussStatus)"
          :szdopt="SR.szdopt"
          :span="8"
          :descSpan="12"
          :desc="objData.header.wfDyName" />
      </el-row>
      <el-row>
        <szd-date-form-item-col label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" :span="8" />
        <szd-select-form-item-col label="货币" v-model="objData.header.curr" baseField="CORE_CURR" required :szdopt="SR.szdopt" :span="8" />
        <szd-select-form-item-col label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled :span="8" />
      </el-row>
      <el-row>
        <szd-input-form-item-col label="业务标题" v-model="objData.header.title" :maxlength="50" required :szdopt="SR.szdopt" :span="8" />
      </el-row>
      <el-row>
        <szd-textarea-form-item-col
          label="备注"
          v-model="objData.header.remark"
          :maxlength="150"
          :rows="2"
          :szdopt="SR.szdopt"
          :span="24"
          label-width="8.35%" />
      </el-row>
    </el-form>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    components: {},
    data() {
      return {};
    },
    methods: {}
  };
</script>
