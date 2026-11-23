<template>
  <!-- 关联单据 -->
  <div class="szd__tb__wrapper" style="padding-top: 5px">
    <div class="szd__tb__main">
      <szd-card-a v-for="row in objData.relateList" :key="row.bussId" :footer="false" :header="false">
        <template #main>
          <szd-card-row-span label="业务单号" :text="row.bussDocId" />
          <szd-card-row-span label="单据类型" :text="row.bussDocType" />
          <szd-card-row-span label="业务状态" :text="row.bussStatusName" />
        </template>
      </szd-card-a>
      <szd-nothing v-if="!objData.relateList || objData.relateList.length === 0" text="暂无关联单据" />
    </div>
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
