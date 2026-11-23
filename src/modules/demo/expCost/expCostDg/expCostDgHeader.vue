<template>
  <el-tabs v-model="SR.SRH" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff">
          <FolderRemove />
        </el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('基本信息1')" name="Tab1">
      <expCostDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('基本信息2')" name="Tab2">
      <expCostDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('发票信息')" name="Tab3">
      <compInvDoc :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>
<script>
  import expCostDgHeaderTab10 from "./expCostDgHeaderTab10.vue";
  import expCostDgHeaderTab20 from "./expCostDgHeaderTab20.vue";
  import compInvDoc from "@/modules/components/inv/compInvDoc.vue";
  import {FolderRemove} from "@element-plus/icons-vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      FolderRemove,
      expCostDgHeaderTab10,
      expCostDgHeaderTab20,
      compInvDoc
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>