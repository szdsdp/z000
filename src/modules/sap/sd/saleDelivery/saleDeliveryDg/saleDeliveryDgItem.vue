<template>
  <el-tabs v-model="SR.SRI" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff">
          <FolderRemove />
        </el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('行项目')" name="Tab1">
      <saleDeliveryDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import saleDeliveryDgItemTab10 from "./saleDeliveryDgItemTab10.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { saleDeliveryDgItemTab10 },
    data() {
      return {};
    },
    methods: {}
  };
</script>
