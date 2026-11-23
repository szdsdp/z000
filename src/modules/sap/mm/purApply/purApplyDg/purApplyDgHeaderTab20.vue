<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="采购申请号"
        :span="8"
        v-model="objData.header.applyNo"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.applyNo)" />
      <szd-input-form-item-col label="SAP消息ID" :span="8" v-model="objData.header.sapMessId" disabled />
      <szd-input-form-item-col label="SAP消息" :span="8" v-model="objData.header.sapMsg" disabled />
    </el-row>
  </el-form>
</template>

<script>
  import szdSapTool from "szd-client-web/client/common/tools/sapTool";

  export default {
    props: ["objData", "SR"],
    components: {},
    data() {
      return {};
    },
    methods: {
      // 跳转SAP
      handleGoToSap(applyNo) {
        if (applyNo) {
          this.szdSapTool.goSapTCode(
            "MMPURPAMEREQ",
            {
              "P_BANFN": applyNo
            },
            "=ONLI"
          );
        }
      }
    }
  };
</script>
