<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="发票号"
        :span="8"
        v-model="objData.header.billDoc"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.billDoc)" />
      <szd-input-form-item-col label="SAP消息" :span="8" v-model="objData.header.sapMsg" disabled />
      <szd-input-form-item-col label="SAP消息ID" :span="8" v-model="objData.header.sapMessId" disabled />
      <szd-input-form-item-col label="冲销发票号" :span="8" v-model="objData.header.revBillDoc" disabled />
      <szd-input-form-item-col label="冲销SAP消息" :span="8" v-model="objData.header.revSapMsg" disabled />
      <szd-input-form-item-col label="冲销SAP消息ID" :span="8" v-model="objData.header.revSapMessId" disabled />
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
      handleGoToSap(billDoc) {
        if (billDoc) {
          this.szdSapTool.goSapTCode("VF03", {
            "VBRK-VBELN": billDoc
          });
        }
      }
    }
  };
</script>
