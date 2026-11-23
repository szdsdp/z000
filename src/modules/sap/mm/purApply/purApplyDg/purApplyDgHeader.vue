<template>
  <!-- 页标签 -->
  <var-tabs
    elevation
    color="var(--color-primary)"
    active-color="var(--color-on-primary)"
    inactive-color="var(--color-on-info)"
    v-model:active="activeTab">
    <var-tab name="Tab1">{{ $t("基本信息") }}</var-tab>
    <var-tab name="Tab2">{{ $t("SAP信息") }}</var-tab>
    <var-tab name="Tab3">{{ $t("关联单据") }}</var-tab>
    <var-tab name="Tab4">{{ $t("行项目") }}</var-tab>
  </var-tabs>

  <!-- tab内容 -->
  <var-tabs-items v-model:active="activeTab" class="flex-1">
    <var-tab-item name="Tab1">
      <purApplyDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
    <var-tab-item name="Tab2">
      <purApplyDgHeaderTab20 :objData="objData" :SR="SR" />
    </var-tab-item>
    <var-tab-item name="Tab3">
      <purApplyDgHeaderTab30 :objData="objData" :SR="SR" />
    </var-tab-item>
    <var-tab-item name="Tab4">
      <purApplyDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
  </var-tabs-items>
</template>

<script>
  import purApplyDgHeaderTab10 from "./purApplyDgHeaderTab10.vue";
  import purApplyDgHeaderTab20 from "./purApplyDgHeaderTab20.vue";
  import purApplyDgHeaderTab30 from "./purApplyDgHeaderTab30.vue";
  import purApplyDgItemTab10 from "./purApplyDgItemTab10.vue";
  import { ref } from "vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      purApplyDgHeaderTab10,
      purApplyDgHeaderTab20,
      purApplyDgHeaderTab30,
      purApplyDgItemTab10
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
