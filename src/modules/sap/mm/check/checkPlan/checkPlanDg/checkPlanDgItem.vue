<template>
  <el-tabs v-model="SR.SRI" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff"><FolderRemove /></el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane label="盘点范围" name="Tab1">
      <checkPlanDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import checkPlanDgItemTab10 from "./checkPlanDgItemTab10.vue";

export default {
  props: ["objData", "SR", "wfOper"],
  components: { checkPlanDgItemTab10 },
  data() {
    return {};
  },
  methods: {}
};
</script>
