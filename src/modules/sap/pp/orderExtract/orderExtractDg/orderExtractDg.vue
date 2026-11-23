<template>
  <div class="app-container" v-if="dg.open">
    <div style="padding-bottom: 5px; display: flex">
      <szd-button label="生产订单入库" type="primary" @click="handleCreatePurStore" icon="Upload" />
    </div>
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <orderExtractDgHeader :objData="objData" :SR="SR" />
      </template>
      <template v-slot:bottom>
        <orderExtractDgItem :objData="objData" :SR="SR" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply } from "../api/orderExtract.js";
  import { createFromPurOrder } from "../../../mm/purStore/api/purStore.js";
  import orderExtractDgHeader from "./orderExtractDgHeader.vue";
  import orderExtractDgItem from "./orderExtractDgItem.vue";

  export default {
    name: "Z000_PP_SCDDCQ_01A",
    components: { orderExtractDgHeader, orderExtractDgItem },
    data() {
      return {
        dg: { open: false },
        SR: {
          routerCode: "Z000_PP_SCDDCQ_01A",
          SRH_height: 220,
          SRH: "Tab1",
          SRI: "Tab1",
          szdopt: "V"
        },
        objData: { header: {}, processList: [], moduleList: [] }
      };
    },
    created() {
      let query = this.$route.query;
      if (query.orderNo) {
        this.getData(query.orderNo);
      }
    },
    methods: {
      getData(orderNo) {
        getApply({ orderNo: orderNo }).then(response => {
          if (response.code === 200) this.objData = response.data;
          this.dg.open = true;
        });
      },
      // 创建生产订单入库单（从订单头数据生成，仅一行）
      handleCreatePurStore() {
        let header = this.objData.header;
        if (!header || !header.orderNo) {
          this.$mW("订单数据不完整");
          return;
        }
        this.szdPop("确定要基于当前生产订单创建入库单据吗？", true)
          .then(() => {
            let purStoreData = {
              header: {
                routerCode: "Z000_MM_CGRK_01A",
                bstp: "Z000_MM_CGRK03",
                title: "生产订单入库"
              },
              itemList: [
                {
                  mtlCode: header.mtlCode || "",
                  fact: header.fact || "",
                  entryQnt: header.orderQty || "",
                  unit: header.unit || "",
                  orderNo: header.orderNo || "",
                  orderItemNo: "1",
                  store: "",
                  itemText: "生产入库行项目文本"
                }
              ]
            };

            createFromPurOrder(purStoreData)
              .then(response => {
                if (response.code === 200 && response.data.status === "S") {
                  this.$mS("创建生产订单入库成功");
                  this.szdWf.goDoc("Z000_MM_CGRK_01A", response.data.docId, "U");
                } else {
                  this.$mE("创建生产订单入库失败");
                }
              })
              .catch(() => {
                this.$mE("创建生产订单入库失败");
              });
          })
          .catch(() => {});
      }
    }
  };
</script>
