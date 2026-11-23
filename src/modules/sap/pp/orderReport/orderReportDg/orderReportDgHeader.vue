<template>
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff"><FolderRemove /></el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('基本信息')" name="Tab1">
      <orderReportDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2">
      <orderReportDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import orderReportDgHeaderTab10 from "./orderReportDgHeaderTab10.vue";
  import orderReportDgHeaderTab20 from "./orderReportDgHeaderTab20.vue";
  export default {
    props: ["objData", "SR", "wfOper"],
    components: { orderReportDgHeaderTab10, orderReportDgHeaderTab20 },
    data() { return {}; },
    methods: {}
  };
</script>
