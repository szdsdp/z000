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
      <vchDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2">
      <vchDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import vchDgHeaderTab10 from "./vchDgHeaderTab10.vue";
  import vchDgHeaderTab20 from "./vchDgHeaderTab20.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      vchDgHeaderTab10,
      vchDgHeaderTab20
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>
