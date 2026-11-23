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
      <purOrderDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2">
      <purOrderDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane label="关联单据" name="Tab3">
      <purOrderDgHeaderTab30 :objData="objData" :SR="SR" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import purOrderDgHeaderTab10 from "./purOrderDgHeaderTab10.vue";
  import purOrderDgHeaderTab20 from "./purOrderDgHeaderTab20.vue";
  import purOrderDgHeaderTab30 from "./purOrderDgHeaderTab30.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      purOrderDgHeaderTab10,
      purOrderDgHeaderTab20,
      purOrderDgHeaderTab30
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>
