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
      <purApplyDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2">
      <purApplyDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane label="关联单据" name="Tab3">
      <purApplyDgHeaderTab30 :objData="objData" :SR="SR" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import purApplyDgHeaderTab10 from "./purApplyDgHeaderTab10.vue";
  import purApplyDgHeaderTab20 from "./purApplyDgHeaderTab20.vue";
  import purApplyDgHeaderTab30 from "./purApplyDgHeaderTab30.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { purApplyDgHeaderTab10, purApplyDgHeaderTab20, purApplyDgHeaderTab30 },
    data() {
      return {};
    },
    methods: {}
  };
</script>
