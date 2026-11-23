<template>
  <el-tabs v-model="SR.SRI" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff"><FolderRemove /></el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane label="工序" name="Tab1">
      <orderExtractDgItemTab10 :objData="objData" :SR="SR" />
    </el-tab-pane>
    <el-tab-pane label="组件" name="Tab2">
      <orderExtractDgItemTab20 :objData="objData" :SR="SR" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import orderExtractDgItemTab10 from "./orderExtractDgItemTab10.vue";
  import orderExtractDgItemTab20 from "./orderExtractDgItemTab20.vue";
  export default {
    props: ["objData", "SR"],
    components: { orderExtractDgItemTab10, orderExtractDgItemTab20 }
  };
</script>
