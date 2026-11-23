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
      <purStoreDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('SAP信息')" name="Tab2">
      <purStoreDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane label="关联单据" name="Tab3">
      <purStoreDgHeaderTab30 :objData="objData" :SR="SR" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import purStoreDgHeaderTab10 from "./purStoreDgHeaderTab10.vue";
  import purStoreDgHeaderTab20 from "./purStoreDgHeaderTab20.vue";
  import purStoreDgHeaderTab30 from "./purStoreDgHeaderTab30.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      purStoreDgHeaderTab10,
      purStoreDgHeaderTab20,
      purStoreDgHeaderTab30
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>
