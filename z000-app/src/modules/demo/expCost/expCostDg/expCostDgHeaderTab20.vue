<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a>
      <template #header>
        <szd-title-label text="组织内容" />
      </template>
      <template #main>
        <szd-input label="制单人公司" v-model="objData.header.cmpyCreate" disabled :desc="objData.header.cmpyCreateName" />
        <szd-input label="制单人部门" v-model="objData.header.cstcCreate" disabled :desc="objData.header.cstcCreateName" />
      </template>
    </szd-card-a>
    <szd-card-a>
      <template #header>
        <szd-title-label text="日期内容" />
      </template>
      <template #main>
        <szd-input label="制单人" v-model="objData.header.createBy" disabled :desc="objData.header.createByName" />
        <szd-input label="制单时间" v-model="objData.header.createTime" disabled />
        <szd-input label="提交人" v-model="objData.header.commitBy" disabled :desc="objData.header.commitByName" />
        <szd-input label="提交时间" v-model="objData.header.commitTime" disabled />
        <szd-input label="更改人" v-model="objData.header.updateBy" disabled :desc="objData.header.updateByName" />
        <szd-input label="更改时间" v-model="objData.header.updateTime" disabled />
      </template>
    </szd-card-a>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {};
    },
    methods: {}
  };
</script>