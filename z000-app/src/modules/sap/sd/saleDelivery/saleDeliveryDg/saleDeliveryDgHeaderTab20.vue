<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>SAP信息</template>
      <template #main>
        <szd-input label="交货单号" v-model="objData.header.vbeln" disabled />
        <szd-input label="SAP消息ID" v-model="objData.header.sapMessId" disabled />
        <szd-input label="SAP消息" v-model="objData.header.sapMsg" disabled />
      </template>
    </szd-card-a>
    <szd-card-a :footer="false" style="margin-top: 10px">
      <template #header>过账信息</template>
      <template #main>
        <szd-date label="拣配日期" v-model="objData.post.pickDate" :szdopt="SR.szdopt" required />
        <szd-date label="过账日期" v-model="objData.post.postDate" :szdopt="SR.szdopt" required />
        <szd-date label="发货日期" v-model="objData.post.shipDate" :szdopt="SR.szdopt" required />
        <szd-input label="物料凭证" v-model="objData.post.mblnr" disabled />
        <szd-input label="冲销物料凭证" v-model="objData.post.revMblnr" disabled />
      </template>
    </szd-card-a>
  </div>
</template>
<script>
  export default { props: ["objData", "SR"] };
</script>
