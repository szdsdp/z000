<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col label="业务单号" :span="8" v-model="objData.header.bussDocId" disabled />
      <szd-select-form-item-col label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled clearable :span="8" />
      <szd-date-form-item-col
        label="业务日期"
        v-model="objData.header.bussDate"
        type="date"
        required
        :szdopt="SR.szdopt"
        :span="8" />
      <szd-select-form-item-col
        label="类型"
        v-model="objData.header.type"
        baseField="Z000_MM_CGHT_LX"
        :szdopt="SR.szdopt"
        :span="8"
        required
        clearable />
      <szd-input-form-item-col
        label="供应商"
        :span="8"
        v-model="objData.header.supp"
        :szdopt="SR.szdopt"
        required
        :descSpan="14"
        :desc="objData.header.suppName"
        readonly
        showHelp
        clearable
        @help="szdSh({ a: 'CORE_SUPP', b: objData.header, c: ['supp-supp', 'suppName-suppName'] })" />
      <szd-select-form-item-col
        label="采购组织"
        :span="8"
        v-model="objData.header.purOrg"
        baseField="Z000_MDM_ORG_PUR"
        :szdopt="SR.szdopt"
        required
        clearable />
      <szd-select-form-item-col
        label="采购组"
        v-model="objData.header.purGroup"
        baseField="Z000_MM_CGDD_CGZ"
        :szdopt="SR.szdopt"
        :span="8"
        required
        clearable />
      <szd-select-form-item-col
        label="货币"
        v-model="objData.header.curr"
        baseField="CORE_CURR"
        :szdopt="SR.szdopt"
        :span="8"
        required
        clearable />
      <szd-date-form-item-col
        label="有效开始日期"
        v-model="objData.header.startDate"
        :szdopt="SR.szdopt"
        :span="8"
        type="date"
        required />
      <szd-date-form-item-col
        label="有效结束日期"
        v-model="objData.header.endDate"
        :szdopt="SR.szdopt"
        :span="8"
        type="date"
        required />
      <szd-input-form-item-col
        label="公司代码"
        :span="8"
        v-model="objData.header.cmpy"
        :szdopt="SR.szdopt"
        required
        :descSpan="14"
        :desc="objData.header.cmpyName"
        readonly
        showHelp
        clearable
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
      <szd-input-form-item-col label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :span="8" :maxlength="255" />
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
        label="业务类型"
        :span="8"
        v-model="objData.header.bstp"
        :szdopt="SR.szdopt"
        disabled
        :descSpan="14"
        :desc="objData.header.bstpName" />
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
