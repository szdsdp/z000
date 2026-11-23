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
      <mtlcjDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('基础视图')" name="Tab2">
      <mtlcjDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('采购视图')" name="Tab3">
      <mtlcjDgHeaderTab30 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP返回信息')" name="Tab4">
      <mtlcjDgHeaderTab40 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import mtlcjDgHeaderTab10 from "./mtlcjDgHeaderTab10.vue";
  import mtlcjDgHeaderTab20 from "./mtlcjDgHeaderTab20.vue";
  import mtlcjDgHeaderTab30 from "./mtlcjDgHeaderTab30.vue";
  import mtlcjDgHeaderTab40 from "./mtlcjDgHeaderTab40.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      mtlcjDgHeaderTab10,
      mtlcjDgHeaderTab20,
      mtlcjDgHeaderTab30,
      mtlcjDgHeaderTab40
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>
