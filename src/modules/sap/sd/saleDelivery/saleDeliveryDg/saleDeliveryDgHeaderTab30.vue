<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-date-form-item-col label="拣配日期" v-model="objData.post.pickDate"  :span="8" />
      <szd-date-form-item-col label="过账日期" v-model="objData.post.postDate"  :span="8" />
      <szd-date-form-item-col label="发货日期" v-model="objData.post.shipDate"  :span="8" />
      <szd-input-form-item-col label="物料凭证" :span="8" v-model="objData.post.mblnr" disabled />
      <szd-input-form-item-col label="凭证年度" :span="8" v-model="objData.post.mjahr" disabled />
      <szd-input-form-item-col label="SAP消息" :span="8" v-model="objData.post.sapMsg" disabled />
      <szd-input-form-item-col label="SAP消息ID" :span="8" v-model="objData.post.sapMessId" disabled />
      <szd-input-form-item-col label="冲销物料凭证" :span="8" v-model="objData.post.revMblnr" disabled />
      <szd-input-form-item-col label="冲销凭证年度" :span="8" v-model="objData.post.revMjahr" disabled />
      <szd-input-form-item-col label="冲销SAP消息" :span="8" v-model="objData.post.revSapMsg" disabled />
      <szd-input-form-item-col label="冲销SAP消息ID" :span="8" v-model="objData.post.revSapMessId" disabled />
    </el-row>
  </el-form>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    data() { return {}; },
    methods: {}
  };
</script>
