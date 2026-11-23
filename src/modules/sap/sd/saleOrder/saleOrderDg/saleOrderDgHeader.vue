<template>
  <var-tabs elevation color="var(--color-primary)" active-color="var(--color-on-primary)" inactive-color="var(--color-on-info)" v-model:active="activeTab">
    <var-tab name="Tab1">{{ $t("基本信息") }}</var-tab>
    <var-tab name="Tab2">{{ $t("SAP信息") }}</var-tab>
    <var-tab name="Tab3">{{ $t("行项目") }}</var-tab>
  </var-tabs>
  <var-tabs-items v-model:active="activeTab" class="flex-1">
    <var-tab-item name="Tab1"><saleOrderDgHeaderTab10 :objData="objData" :SR="SR" /></var-tab-item>
    <var-tab-item name="Tab2"><saleOrderDgHeaderTab20 :objData="objData" :SR="SR" /></var-tab-item>
    <var-tab-item name="Tab3"><saleOrderDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" /></var-tab-item>
  </var-tabs-items>
</template>
<script>
  import saleOrderDgHeaderTab10 from "./saleOrderDgHeaderTab10.vue";
  import saleOrderDgHeaderTab20 from "./saleOrderDgHeaderTab20.vue";
  import saleOrderDgItemTab10 from "./saleOrderDgItemTab10.vue";
  import { ref } from "vue";
  export default {
    props: ["objData", "SR", "wfOper"],
    components: { saleOrderDgHeaderTab10, saleOrderDgHeaderTab20, saleOrderDgItemTab10 },
    setup() { const activeTab = ref("Tab1"); return { activeTab }; }
  };
</script>
