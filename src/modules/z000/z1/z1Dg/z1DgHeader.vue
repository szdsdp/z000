<template>
  <!-- 页标签信息 -->
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff">
          <FolderRemove />
        </el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane label="基本信息1" name="Tab1">
      <z1DgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane label="基本信息2" name="Tab2">
      <z1DgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane label="发票信息" name="Tab3">
      <invTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>
<script>
  import z1DgHeaderTab10 from "./z1DgHeaderTab10.vue";
  import z1DgHeaderTab20 from "./z1DgHeaderTab20.vue";
  import invTab10 from "../../../components/inv/invTab10.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      z1DgHeaderTab10,
      z1DgHeaderTab20,
      invTab10
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>
