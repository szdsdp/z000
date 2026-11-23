<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="公司"
        v-model="objData.header.cmpy"
        disabled
        required
        :szdopt="SR.szdopt"
        :span="8"
        :descSpan="14"
        :desc="objData.header.cmpyName" />
      <szd-input-form-item-col
        label="成本中心"
        v-model="objData.header.cstc"
        disabled
        required
        :szdopt="SR.szdopt"
        :span="8"
        :descSpan="14"
        :desc="objData.header.cstcName" />
      <szd-input-form-item-col label="业务类型" v-model="objData.header.bstp" disabled :span="8" :descSpan="14" :desc="objData.header.bstpName" />
      <szd-date-form-item-col label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" :span="8" />
      <szd-select-form-item-col clearable label="单据状态" :span="8" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled />
    </el-row>
    <el-row>
       <szd-input-form-item-col
        label="创建人"
        v-model="objData.header.createBy"
        disabled
        :szdopt="SR.szdopt"
        :span="8"
        :descSpan="14"
        :desc="objData.header.createByName" />
      <szd-input-form-item-col label="创建时间" :span="8" v-model="objData.header.createTime" disabled />
    </el-row>
    <el-row>
      <szd-input-form-item-col label="SAP消息ID" :span="8" v-model="objData.header.sapMessId" disabled />
      <szd-input-form-item-col label="SAP消息" :span="8" v-model="objData.header.sapMsg" disabled />
    </el-row>
  </el-form>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    data() {
      return {};
    },
    methods: {}
  };
</script>
