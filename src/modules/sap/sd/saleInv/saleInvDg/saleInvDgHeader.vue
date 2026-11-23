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
      <saleInvDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2">
      <saleInvDgHeaderTab20 :objData="objData" :SR="SR" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import saleInvDgHeaderTab10 from "./saleInvDgHeaderTab10.vue";
  import saleInvDgHeaderTab20 from "./saleInvDgHeaderTab20.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { saleInvDgHeaderTab10, saleInvDgHeaderTab20 },
    data() {
      return {};
    },
    methods: {}
  };
</script>
