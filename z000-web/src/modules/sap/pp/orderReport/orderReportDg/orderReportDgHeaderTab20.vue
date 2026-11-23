<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="确认编号"
        :span="6"
        v-model="objData.header.confno"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.confno)" />
      <szd-input-form-item-col label="确认计数器" :span="6" v-model="objData.header.confcnt" disabled />
      <szd-input-form-item-col label="SAP消息ID" :span="6" v-model="objData.header.sapMessId" disabled />
      <szd-input-form-item-col label="SAP消息" :span="6" v-model="objData.header.sapMsg" disabled />
    </el-row>
    <el-row>
      <szd-input-form-item-col label="冲销确认计数器" :span="6" v-model="objData.header.revConfcnt" disabled />
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
      handleGoToSap(confno) {
        if (confno) {
          this.szdSapTool.goSapTCode("CO14", {
            "CORUF-RUECK": confno
          });
        }
      }
    }
  };
</script>
