<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="采购凭证号"
        :span="8"
        v-model="objData.header.vchNo"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.vchNo)" />
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
      handleGoToSap(vchNo) {
        if (vchNo) {
          this.szdSapTool.goSapTCode(
            "MMPURPAMEPO",
            {
              P_EBELN: vchNo
            },
            "=ONLI"
          );
        }
      }
    }
  };
</script>
