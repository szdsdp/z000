<template>
  <var-tabs
    elevation
    color="var(--color-primary)"
    active-color="var(--color-on-primary)"
    inactive-color="var(--color-on-info)"
    v-model:active="activeTab">
    <var-tab name="Tab1">{{ $t("基本信息") }}</var-tab>
    <var-tab name="Tab2">{{ $t("SAP/过账信息") }}</var-tab>
    <var-tab name="Tab3">{{ $t("关联单据") }}</var-tab>
    <var-tab name="Tab4">{{ $t("行项目") }}</var-tab>
  </var-tabs>
  <var-tabs-items v-model:active="activeTab" class="flex-1">
    <var-tab-item name="Tab1">
      <saleDeliveryDgHeaderTab10 :objData="objData" :SR="SR" />
    </var-tab-item>
    <var-tab-item name="Tab2">
      <saleDeliveryDgHeaderTab20 :objData="objData" :SR="SR" />
    </var-tab-item>
    <var-tab-item name="Tab3">
      <saleDeliveryDgHeaderTab30 :objData="objData" :SR="SR" />
    </var-tab-item>
    <var-tab-item name="Tab4">
      <saleDeliveryDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
    </var-tab-item>
  </var-tabs-items>
</template>
<script>
  import saleDeliveryDgHeaderTab10 from "./saleDeliveryDgHeaderTab10.vue";
  import saleDeliveryDgHeaderTab20 from "./saleDeliveryDgHeaderTab20.vue";
  import saleDeliveryDgHeaderTab30 from "./saleDeliveryDgHeaderTab30.vue";
  import saleDeliveryDgItemTab10 from "./saleDeliveryDgItemTab10.vue";
  import { ref } from "vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      saleDeliveryDgHeaderTab10,
      saleDeliveryDgHeaderTab20,
      saleDeliveryDgHeaderTab30,
      saleDeliveryDgItemTab10
    },
    setup() {
      const activeTab = ref("Tab1");
      return { activeTab };
    }
  };
</script>
