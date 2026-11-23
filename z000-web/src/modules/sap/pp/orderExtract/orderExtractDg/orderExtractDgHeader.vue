<template>
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff"><FolderRemove /></el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('基本信息')" name="Tab1">
      <orderExtractDgHeaderTab10 :objData="objData" :SR="SR" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import orderExtractDgHeaderTab10 from "./orderExtractDgHeaderTab10.vue";
  import orderReportDgHeaderTab20 from "@/modules/sap/pp/orderReport/orderReportDg/orderReportDgHeaderTab20.vue";
  import orderReportDgHeaderTab10 from "@/modules/sap/pp/orderReport/orderReportDg/orderReportDgHeaderTab10.vue";
  export default { props: ["objData", "SR"], components: { orderReportDgHeaderTab10, orderReportDgHeaderTab20, orderExtractDgHeaderTab10 } };
</script>
