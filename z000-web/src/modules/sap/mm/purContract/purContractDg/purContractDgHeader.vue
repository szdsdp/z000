<template>
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff">
          <FolderRemove />
        </el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('基本信息')" name="Tab1">
      <purContractDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2">
      <purContractDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import purContractDgHeaderTab10 from "./purContractDgHeaderTab10.vue";
  import purContractDgHeaderTab20 from "./purContractDgHeaderTab20.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { purContractDgHeaderTab10, purContractDgHeaderTab20 },
    data() {
      return {};
    },
    methods: {}
  };
</script>
