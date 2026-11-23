<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>SAP信息</template>
      <template #main>
        <szd-input label="发票凭证编号" v-model="objData.header.reBelnr" disabled />
        <szd-input label="财年" v-model="objData.header.reGjahr" disabled />
        <szd-input label="会计凭证编号" v-model="objData.header.belnr" disabled />
        <szd-input label="会计财年" v-model="objData.header.gjahr" disabled />
        <szd-input label="SAP消息ID" v-model="objData.header.sapMessId" disabled />
        <szd-input label="SAP消息" v-model="objData.header.sapMsg" disabled />
      </template>
    </szd-card-a>
    <szd-card-a :footer="false" style="margin-top: 10px">
      <template #header>冲销信息</template>
      <template #main>
        <szd-input label="冲销发票凭证" v-model="objData.header.revReBelnr" disabled />
        <szd-input label="冲销财年" v-model="objData.header.revReGjahr" disabled />
        <szd-input label="冲销会计凭证" v-model="objData.header.revBelnr" disabled />
        <szd-input label="冲销会计财年" v-model="objData.header.revGjahr" disabled />
        <szd-input label="冲销SAP消息ID" v-model="objData.header.revSapMessId" disabled />
        <szd-input label="冲销SAP消息" v-model="objData.header.revSapMsg" disabled />
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
