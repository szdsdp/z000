<template>
  <var-tabs elevation color="var(--color-primary)" active-color="var(--color-on-primary)" inactive-color="var(--color-on-info)" v-model:active="activeTab">
    <var-tab name="Tab1">{{ $t("基本信息") }}</var-tab>
    <var-tab name="Tab2">{{ $t("SAP信息") }}</var-tab>
    <var-tab name="Tab3">{{ $t("行项目") }}</var-tab>
  </var-tabs>
  <var-tabs-items v-model:active="activeTab" class="flex-1">
    <var-tab-item name="Tab1"><saleInvDgHeaderTab10 :objData="objData" :SR="SR" /></var-tab-item>
    <var-tab-item name="Tab2"><saleInvDgHeaderTab20 :objData="objData" :SR="SR" /></var-tab-item>
    <var-tab-item name="Tab3"><saleInvDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" /></var-tab-item>
  </var-tabs-items>
</template>
<script>
  import saleInvDgHeaderTab10 from "./saleInvDgHeaderTab10.vue";
  import saleInvDgHeaderTab20 from "./saleInvDgHeaderTab20.vue";
  import saleInvDgItemTab10 from "./saleInvDgItemTab10.vue";
  import { ref } from "vue";
  export default { props: ["objData", "SR", "wfOper"], components: { saleInvDgHeaderTab10, saleInvDgHeaderTab20, saleInvDgItemTab10 }, setup() { const activeTab = ref("Tab1"); return { activeTab }; } };
</script>
