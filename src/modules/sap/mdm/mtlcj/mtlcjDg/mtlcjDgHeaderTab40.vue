<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="物料编码"
        :span="8"
        v-model="objData.header.mtlCode"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.mtlCode)" />
      <szd-input-form-item-col label="SAP消息" v-model="objData.header.sapMsg" disabled :span="8" />
      <szd-input-form-item-col label="SAP消息ID" v-model="objData.header.sapMessId" disabled :span="8" />
    </el-row>
  </el-form>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    methods: {
      // 跳转SAP
      handleGoToSap(mtlCode) {
        if (mtlCode) {
          this.szdSapTool.goSapTCode("MM03", {
            "RMMG1-MATNR": mtlCode
          });
        }
      }
    }
  };
</script>
