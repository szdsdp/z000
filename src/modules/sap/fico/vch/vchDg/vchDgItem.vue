<template>
  <el-tabs v-model="SR.SRI" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff">
          <FolderRemove />
        </el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('项目信息')" name="Tab1">
      <vchDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('凭证调整分录')" name="Tab2">
      <vchDgItemTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import vchDgItemTab10 from "./vchDgItemTab10.vue";
  import vchDgItemTab20 from "./vchDgItemTab20.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      vchDgItemTab10,
      vchDgItemTab20
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>
