<template>
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff"><FolderRemove /></el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('基本信息')" name="Tab1">
      <saleOrderDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2">
      <saleOrderDgHeaderTab20 :objData="objData" :SR="SR" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import saleOrderDgHeaderTab10 from "./saleOrderDgHeaderTab10.vue";
  import saleOrderDgHeaderTab20 from "./saleOrderDgHeaderTab20.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { saleOrderDgHeaderTab10, saleOrderDgHeaderTab20 },
    data() { return {}; },
    methods: {}
  };
</script>
