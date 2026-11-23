<template>
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold"><template #label><el-icon style="font-size:18px;color:#1890ff"><FolderRemove /></el-icon></template></el-tab-pane>
    <el-tab-pane :label="$t('基本信息')" name="Tab1"><invCheckDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" /></el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2"><invCheckDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" /></el-tab-pane>
  </el-tabs>
</template>
<script>
  import invCheckDgHeaderTab10 from "./invCheckDgHeaderTab10.vue";
  import invCheckDgHeaderTab20 from "./invCheckDgHeaderTab20.vue";
  export default { props: ["objData","SR","wfOper"], components: { invCheckDgHeaderTab10, invCheckDgHeaderTab20 }, data() { return {}; }, methods: {} };
</script>
