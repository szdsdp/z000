<template>
  <el-tabs v-model="SR.SRI" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff"><FolderRemove /></el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane v-if="['20', '40'].includes(objData.header.payNature)" :label="$t('销售合同')" name="Tab1">
      <payReceDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane v-if="['10', '30'].includes(objData.header.payNature)" :label="$t('未清信息')" name="Tab1">
      <payReceDgItemTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import payReceDgItemTab10 from "./payReceDgItemTab10.vue";
  import payReceDgItemTab20 from "./payReceDgItemTab20.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { payReceDgItemTab10, payReceDgItemTab20 },
    data() { return {}; },
    methods: {}
  };
</script>
