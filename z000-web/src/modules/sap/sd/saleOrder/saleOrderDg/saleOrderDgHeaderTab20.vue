<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="销售订单号"
        :span="8"
        v-model="objData.header.vbeln"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.vbeln)" />
      <szd-input-form-item-col label="SAP消息" :span="8" v-model="objData.header.sapMsg" disabled />
      <szd-input-form-item-col label="SAP消息ID" :span="8" v-model="objData.header.sapMessId" disabled />
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
      // 跳转SAP
      handleGoToSap(vbeln) {
        if (vbeln) {
          this.szdSapTool.goSapTCode("VA03", {
            "VBAK-VBELN": vbeln
          });
        }
      }
    }
  };
</script>
