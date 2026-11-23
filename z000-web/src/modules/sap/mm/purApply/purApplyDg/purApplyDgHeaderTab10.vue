<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col label="业务单号" :span="8" v-model="objData.header.bussDocId" disabled />
      <szd-select-form-item-col label="申请类型" v-model="objData.header.applyType" baseField="Z000_MM_CGSQ_SQLX" :szdopt="SR.szdopt" required clearable :span="8" />
      <szd-input-form-item-col label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :span="8" :maxlength="255" />
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
        :descSpan="14"
        :desc="objData.header.cmpyName"
        readonly
        showHelp
        clearable
        required
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
        showHelp
        clearable
        @help="szdSh({ a: 'CORE_CSTC', b: objData.header, c: ['cstc-cstc', 'cstcName-cstcName'] })" />
      <szd-input-form-item-col
        label="业务类型"
        :span="8"
        v-model="objData.header.bstp"
        :szdopt="SR.szdopt"
        disabled
        :descSpan="14"
        :desc="objData.header.bstpName" />
      <szd-date-form-item-col label="业务日期" v-model="objData.header.bussDate" :szdopt="SR.szdopt" :span="8" />
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
    <el-row>
      <szd-upload-view-form-item-col
        label-width="11%"
        required
        :szdopt="SR.szdopt"
        v-model="objData.header.fileObjId"
        label="附件"
        fileCate="LICENS"
        listType="picture-card"
        acceptTxt="附件"
        :cardWidth="100"
        :cardHeight="100"
        :limit="10"
        :span="18" />
    </el-row>
  </el-form>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    components: {},
    data() {
      return {};
    },
    methods: {}
  };
</script>
