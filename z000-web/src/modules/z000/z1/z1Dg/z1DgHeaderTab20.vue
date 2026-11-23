<template>
  <div class="app-sub-container" style="margin-top: 0px; overflow: auto">
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
        <szd-input-form-item-col
          label="申请金额"
          :span="8"
          v-model="objData.header.amtApply"
          disabled
          type="number"
          :numDot="true"
          :numDec="2" />
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
