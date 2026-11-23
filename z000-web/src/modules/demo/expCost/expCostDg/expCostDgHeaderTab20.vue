<template>
  <div class="app-sub-container" style="padding: 0">
    <el-form class="app-el-form">
      <el-row>
        <szd-input-form-item-col
          label="制单人公司"
          v-model="objData.header.cmpyCreate"
          disabled
          :span="8"
          :descSpan="12"
          :desc="objData.header.cmpyCreateName" />
        <szd-input-form-item-col
          label="制单人部门"
          v-model="objData.header.cstcCreate"
          disabled
          :span="8"
          :descSpan="12"
          :desc="objData.header.cstcCreateName" />
      </el-row>
    </el-form>
  </div>
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