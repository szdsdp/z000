<template>
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff"><FolderRemove /></el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane label="基本信息" name="Tab1">
      <checkTaskDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import checkTaskDgHeaderTab10 from "./checkTaskDgHeaderTab10.vue";

export default {
  props: ["objData", "SR", "wfOper"],
  components: { checkTaskDgHeaderTab10 },
  data() { return {}; },
  methods: {}
};
</script>
