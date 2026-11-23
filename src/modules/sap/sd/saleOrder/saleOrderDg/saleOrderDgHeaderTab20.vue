<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>SAP信息</template>
      <template #main>
        <szd-input label="销售订单号" v-model="objData.header.vbeln" disabled />
        <szd-input label="SAP消息ID" v-model="objData.header.sapMessId" disabled />
        <szd-input label="SAP消息" v-model="objData.header.sapMsg" disabled />
      </template>
    </szd-card-a>
  </div>
</template>
<script>
  export default { props: ["objData", "SR"], components: {}, data() { return {}; }, methods: {} };
</script>
