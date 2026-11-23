<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="物料凭证"
        :span="6"
        v-model="objData.header.mblnr"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.mblnr, objData.header.mjahr)" />
      <szd-input-form-item-col label="物料凭证年度" :span="6" v-model="objData.header.mjahr" disabled />
      <szd-input-form-item-col label="SAP消息ID" :span="6" v-model="objData.header.sapMessId" disabled />
      <szd-input-form-item-col label="SAP消息" :span="6" v-model="objData.header.sapMsg" disabled />
    </el-row>
    <el-row>
      <szd-input-form-item-col
        label="冲销物料凭证"
        :span="6"
        v-model="objData.header.revMblnr"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.revMblnr, objData.header.revMjahr)" />
      <szd-input-form-item-col label="冲销物料凭证年度" :span="6" v-model="objData.header.revMjahr" disabled />
      <szd-input-form-item-col label="冲销SAP消息ID" :span="6" v-model="objData.header.revSapMessId" disabled />
      <szd-input-form-item-col label="冲销SAP消息" :span="6" v-model="objData.header.revSapMsg" disabled />
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
      handleGoToSap(mblnr, mjahr) {
        if (mblnr && mjahr) {
          this.szdSapTool.goSapTCode("MB03", {
            "RM07M-MBLNR": mblnr,
            "RM07M-MJAHR": mjahr
          });
        }
      }
    }
  };
</script>
