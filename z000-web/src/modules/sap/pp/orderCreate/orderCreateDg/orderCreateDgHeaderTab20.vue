<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="订单号"
        :span="8"
        v-model="objData.header.aufnr"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.aufnr)" />
      <szd-input-form-item-col label="SAP消息ID" :span="8" v-model="objData.header.sapMessId" disabled />
      <szd-input-form-item-col label="SAP消息" :span="8" v-model="objData.header.sapMsg" disabled />
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
    methods: {
      // 跳转SAP
      handleGoToSap(aufnr) {
        if (aufnr) {
          this.szdSapTool.goSapTCode("CO03", {
            "CAUFVD-AUFNR": aufnr
          });
        }
      }
    }
  };
</script>
