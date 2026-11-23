<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>SAP信息</template>
      <template #main>
        <szd-input label="物料凭证" v-model="objData.header.mblnr" disabled />
        <szd-input label="物料凭证年度" v-model="objData.header.mjahr" disabled />
        <szd-input label="SAP消息" v-model="objData.header.sapMsg" disabled />
        <szd-input label="SAP消息ID" v-model="objData.header.sapMessId" disabled />
        <szd-input label="冲销物料凭证" v-model="objData.header.revMblnr" disabled />
        <szd-input label="冲销物料凭证年度" v-model="objData.header.revMjahr" disabled />
        <szd-input label="冲销SAP消息" v-model="objData.header.revSapMsg" disabled />
        <szd-input label="冲销SAP消息ID" v-model="objData.header.revSapMessId" disabled />
      </template>
    </szd-card-a>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    components: {},
    data() {
      return {};
    },
    methods: {}
  };
</script>
