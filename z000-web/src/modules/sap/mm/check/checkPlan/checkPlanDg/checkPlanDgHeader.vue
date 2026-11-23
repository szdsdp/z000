<template>
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff"><FolderRemove /></el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane label="基本信息" name="Tab1">
      <checkPlanDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane label="关联单据" name="Tab2">
      <checkPlanDgHeaderTab30 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane label="SAP信息" name="Tab3">
      <checkPlanDgHeaderTab20 :objData="objData" :SR="SR" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import checkPlanDgHeaderTab10 from "./checkPlanDgHeaderTab10.vue";
import checkPlanDgHeaderTab20 from "./checkPlanDgHeaderTab20.vue";
import checkPlanDgHeaderTab30 from "./checkPlanDgHeaderTab30.vue";

export default {
  props: ["objData", "SR", "wfOper"],
  components: { checkPlanDgHeaderTab10, checkPlanDgHeaderTab20, checkPlanDgHeaderTab30 },
  data() {
    return {};
  },
  methods: {}
};
</script>
