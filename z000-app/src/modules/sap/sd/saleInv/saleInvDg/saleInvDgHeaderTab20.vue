<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>SAP信息</template>
      <template #main>
        <szd-input label="发票号" v-model="objData.header.billDoc" disabled />
        <szd-input label="SAP消息ID" v-model="objData.header.sapMessId" disabled />
        <szd-input label="SAP消息" v-model="objData.header.sapMsg" disabled />
        <szd-input label="冲销发票号" v-model="objData.header.revBillDoc" disabled />
        <szd-input label="冲销SAP消息ID" v-model="objData.header.revSapMessId" disabled />
        <szd-input label="冲销SAP消息" v-model="objData.header.revSapMsg" disabled />
      </template>
    </szd-card-a>
  </div>
</template>
<script>export default { props: ["objData", "SR"] };</script>
