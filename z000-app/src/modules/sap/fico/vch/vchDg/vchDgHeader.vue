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
    <var-tab name="Tab3">{{ $t("项目信息") }}</var-tab>
    <var-tab name="Tab4">{{ $t("凭证调整分录") }}</var-tab>
  </var-tabs>

  <!-- tab内容 -->
  <var-tabs-items v-model:active="activeTab" class="flex-1">
    <var-tab-item name="Tab1">
      <vchDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
    <var-tab-item name="Tab2">
      <vchDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
    <var-tab-item name="Tab3">
      <vchDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
    <var-tab-item name="Tab4">
      <vchDgItemTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
  </var-tabs-items>
</template>

<script>
  import vchDgHeaderTab10 from "./vchDgHeaderTab10.vue";
  import vchDgHeaderTab20 from "./vchDgHeaderTab20.vue";
  import vchDgItemTab10 from "./vchDgItemTab10.vue";
  import vchDgItemTab20 from "./vchDgItemTab20.vue";
  import { ref } from "vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { vchDgHeaderTab10, vchDgHeaderTab20, vchDgItemTab10, vchDgItemTab20 },
    setup() {
      const activeTab = ref("Tab1");
      return { activeTab };
    }
  };
</script>
