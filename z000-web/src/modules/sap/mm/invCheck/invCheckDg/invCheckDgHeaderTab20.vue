<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="发票凭证编号"
        :span="8"
        v-model="objData.header.reBelnr"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.reBelnr, objData.header.reGjahr)" />
      <szd-input-form-item-col label="财年" :span="8" v-model="objData.header.reGjahr" disabled />
      <szd-input-form-item-col label="会计凭证编号" :span="8" v-model="objData.header.belnr" disabled />
      <szd-input-form-item-col label="会计财年" :span="8" v-model="objData.header.gjahr" disabled />
      <szd-input-form-item-col label="SAP消息ID" :span="8" v-model="objData.header.sapMessId" disabled />
      <szd-input-form-item-col label="SAP消息" :span="8" v-model="objData.header.sapMsg" disabled />
    </el-row>
    <el-row>
      <szd-input-form-item-col
        label="冲销发票凭证"
        :span="8"
        v-model="objData.header.revReBelnr"
        disabled
        dblclick
        @dblclick="handleGoToSap(objData.header.revReBelnr, objData.header.revReGjahr)" />
      <szd-input-form-item-col label="冲销财年" :span="8" v-model="objData.header.revReGjahr" disabled />
      <szd-input-form-item-col label="冲销会计凭证" :span="8" v-model="objData.header.revBelnr" disabled />
      <szd-input-form-item-col label="冲销会计财年" :span="8" v-model="objData.header.revGjahr" disabled />
      <szd-input-form-item-col label="冲销SAP消息ID" :span="8" v-model="objData.header.revSapMessId" disabled />
      <szd-input-form-item-col label="冲销SAP消息" :span="8" v-model="objData.header.revSapMsg" disabled />
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
      handleGoToSap(reBelnr, reGjahr) {
        if (reBelnr && reGjahr) {
          this.szdSapTool.goSapTCode("MIR4", {
            "RBKP-BELNR": reBelnr,
            "RBKP-GJAHR ": reGjahr
          });
        }
      }
    }
  };
</script>
