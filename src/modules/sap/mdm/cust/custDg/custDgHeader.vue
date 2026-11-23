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
      <custDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('管理信息')" name="Tab2">
      <custDgHeaderTab30 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import custDgHeaderTab10 from "./custDgHeaderTab10.vue";
  import custDgHeaderTab30 from "./custDgHeaderTab30.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      custDgHeaderTab10,
      custDgHeaderTab30,
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>
