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
      <purStoreDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
    <var-tab-item name="Tab2">
      <purStoreDgHeaderTab20 :objData="objData" :SR="SR" />
    </var-tab-item>
    <var-tab-item name="Tab3">
      <purStoreDgHeaderTab30 :objData="objData" :SR="SR" />
    </var-tab-item>
    <var-tab-item name="Tab4">
      <purStoreDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
  </var-tabs-items>
</template>

<script>
  import purStoreDgHeaderTab10 from "./purStoreDgHeaderTab10.vue";
  import purStoreDgHeaderTab20 from "./purStoreDgHeaderTab20.vue";
  import purStoreDgHeaderTab30 from "./purStoreDgHeaderTab30.vue";
  import purStoreDgItemTab10 from "./purStoreDgItemTab10.vue";
  import { ref } from "vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      purStoreDgHeaderTab10,
      purStoreDgHeaderTab20,
      purStoreDgHeaderTab30,
      purStoreDgItemTab10
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
