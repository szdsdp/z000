<template>
  <!-- 页标签 -->
  <var-tabs
    elevation
    color="var(--color-primary)"
    active-color="var(--color-on-primary)"
    inactive-color="var(--color-on-info)"
    v-model:active="activeTab">
    <var-tab name="Tab1">{{ $t("基本信息") }}</var-tab>
    <var-tab name="Tab2">{{ $t("关联单据") }}</var-tab>
    <var-tab name="Tab3">{{ $t("SAP信息") }}</var-tab>
    <var-tab name="Tab4">{{ $t("盘点范围") }}</var-tab>
  </var-tabs>

  <!-- tab内容 -->
  <var-tabs-items v-model:active="activeTab" class="flex-1">
    <var-tab-item name="Tab1">
      <checkPlanDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
    <var-tab-item name="Tab2">
      <checkPlanDgHeaderTab30 :objData="objData" :SR="SR" />
    </var-tab-item>
    <var-tab-item name="Tab3">
      <checkPlanDgHeaderTab20 :objData="objData" :SR="SR" />
    </var-tab-item>
    <var-tab-item name="Tab4">
      <checkPlanDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
  </var-tabs-items>
</template>

<script>
  import checkPlanDgHeaderTab10 from "./checkPlanDgHeaderTab10.vue";
  import checkPlanDgHeaderTab20 from "./checkPlanDgHeaderTab20.vue";
  import checkPlanDgHeaderTab30 from "./checkPlanDgHeaderTab30.vue";
  import checkPlanDgItemTab10 from "./checkPlanDgItemTab10.vue";
  import { ref } from "vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      checkPlanDgHeaderTab10,
      checkPlanDgHeaderTab20,
      checkPlanDgHeaderTab30,
      checkPlanDgItemTab10
    },
    setup() {
      const activeTab = ref("Tab1"); // 默认选中第一个 tab
      return { activeTab };
    },
    data() {
      return {};
    },
    methods: {}
  };
</script>
