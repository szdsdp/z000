<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="SAP会计凭证编号"
        :span="6"
        v-model="objData.header.belnr"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header)" />
      <szd-input-form-item-col label="SAP财年" :span="6" v-model="objData.header.gjahr" disabled />
      <szd-input-form-item-col label="SAP消息ID" :span="6" v-model="objData.header.sapMessId" disabled />
      <szd-input-form-item-col label="SAP消息" :span="6" v-model="objData.header.sapMsg" disabled />
    </el-row>
  </el-form>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {};
    },
    methods: {
      // 跳转SAP
      handleGoToSap(header) {
        if (header) {
          this.szdSapTool.goSapTCode("FB03", {
            "RF05L-BELNR": header.belnr,
            "RF05L-GJAHR": header.gjahr,
            "RF05L-BUKRS": header.cmpy
          });
        }
      }
    }
  };
</script>
