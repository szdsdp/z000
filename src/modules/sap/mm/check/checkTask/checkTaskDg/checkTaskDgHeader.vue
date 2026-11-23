<template>
  <!-- 页标签 -->
  <var-tabs
    elevation
    color="var(--color-primary)"
    active-color="var(--color-on-primary)"
    inactive-color="var(--color-on-info)"
    v-model:active="activeTab">
    <var-tab name="Tab1">{{ $t("基本信息") }}</var-tab>
    <var-tab name="Tab2">{{ $t("行项目") }}</var-tab>
  </var-tabs>

  <!-- tab内容 -->
  <var-tabs-items v-model:active="activeTab" class="flex-1">
    <var-tab-item name="Tab1">
      <checkTaskDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
    <var-tab-item name="Tab2">
      <checkTaskDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
  </var-tabs-items>
</template>

<script>
  import checkTaskDgHeaderTab10 from "./checkTaskDgHeaderTab10.vue";
  import checkTaskDgItemTab10 from "./checkTaskDgItemTab10.vue";
  import { ref } from "vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      checkTaskDgHeaderTab10,
      checkTaskDgItemTab10
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
