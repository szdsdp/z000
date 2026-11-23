<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>SAP信息</template>
      <template #main>
        <szd-input label="SAP会计凭证编号" v-model="objData.header.belnr" disabled dblclick @dblclick="handleGoToSap(objData.header)" />
        <szd-input label="SAP财年" v-model="objData.header.gjahr" disabled />
        <szd-input label="SAP消息ID" v-model="objData.header.sapMessId" disabled />
        <szd-input label="SAP消息" v-model="objData.header.sapMsg" disabled />
      </template>
    </szd-card-a>
  </div>
</template>
<script>
  export default {
    props: ["objData", "SR", "wfOper"],
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
