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
      <saleDeliveryDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2">
      <saleDeliveryDgHeaderTab20 :objData="objData" :SR="SR" />
    </el-tab-pane>
    <el-tab-pane :label="$t('过账信息')" name="Tab3">
      <saleDeliveryDgHeaderTab30 :objData="objData" :SR="SR" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import saleDeliveryDgHeaderTab10 from "./saleDeliveryDgHeaderTab10.vue";
  import saleDeliveryDgHeaderTab20 from "./saleDeliveryDgHeaderTab20.vue";
  import saleDeliveryDgHeaderTab30 from "./saleDeliveryDgHeaderTab30.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { saleDeliveryDgHeaderTab10, saleDeliveryDgHeaderTab20, saleDeliveryDgHeaderTab30 },
    data() {
      return {};
    },
    methods: {}
  };
</script>
