<template>
  <el-tabs v-model="SR.SRI" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff">
          <FolderRemove />
        </el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('银行视图')" name="Tab1">
      <custDgItemTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('客户-公司视图')" name="Tab2" v-if="this.objData.header.custFlag.includes('K')">
      <custDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('客户-销售视图')" name="Tab4" v-if="objData.header.custFlag.includes('K')">
      <custDgItemTab30 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('客户-信用视图')" name="Tab6" v-if="objData.header.creditFlag === '0'">
      <custDgItemTab50 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('供应商-公司视图')" name="Tab3" v-if="objData.header.custFlag.includes('D')">
      <custDgItemTab11 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
    <el-tab-pane :label="$t('供应商-采购视图')" name="Tab5" v-if="objData.header.custFlag.includes('D')">
      <custDgItemTab40 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import custDgItemTab10 from "./custDgItemTab10.vue";
  import custDgItemTab11 from "./custDgItemTab11.vue";
  import custDgItemTab20 from "./custDgItemTab20.vue";
  import custDgItemTab30 from "./custDgItemTab30.vue";
  import custDgItemTab40 from "./custDgItemTab40.vue";
  import custDgItemTab50 from "./custDgItemTab50.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      custDgItemTab10,
      custDgItemTab11,
      custDgItemTab20,
      custDgItemTab30,
      custDgItemTab40,
      custDgItemTab50,
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>
