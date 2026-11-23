<template>
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff"><FolderRemove /></el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('基本信息')" name="Tab1">
      <payReceDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('管理信息')" name="Tab2">
      <payReceDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('回款信息')" name="Tab3">
      <payReceDgHeaderTab30 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import payReceDgHeaderTab10 from "./payReceDgHeaderTab10.vue";
  import payReceDgHeaderTab20 from "./payReceDgHeaderTab20.vue";
  import payReceDgHeaderTab30 from "./payReceDgHeaderTab30.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { payReceDgHeaderTab10, payReceDgHeaderTab20, payReceDgHeaderTab30 },
    data() { return {}; },
    methods: {}
  };
</script>
