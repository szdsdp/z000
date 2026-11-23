<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <!-- 功能按钮 -->
    <div class="main-table-title" style="height: 35px">
      <div class="main-table-title-left" style="width: 50%"></div>
      <div class="main-table-title-right" style="width: 50%; align-items: center">
        <szd-button v-if="objData.header" label="生产投料" type="primary" @click="handleCreatePurStore" link icon="Upload" />
      </div>
    </div>
    <el-table ref="refTable" border stripe :data="objData.moduleList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('序号')" type="index" width="60" align="center" fixed="left" />
      <el-table-column label="预留编号" prop="resNo" min-width="120" />
      <el-table-column label="预留项目" prop="resItem" min-width="100" />
      <el-table-column label="物料编码" prop="mtlCode" min-width="140" />
      <el-table-column label="工厂" prop="fact" min-width="100" />
      <el-table-column label="库存地点" prop="store" min-width="100" />
      <el-table-column label="需求日期" prop="reqDate" min-width="110" />
      <el-table-column label="需求数量" prop="reqQty" min-width="100" />
      <el-table-column label="单位" prop="unit" min-width="80" />
    </el-table>
  </div>
</template>

<script>
  import { createFromPurOrder } from "../../../mm/purStore/api/purStore.js";

  export default {
    props: ["objData", "SR"],
    data() {
      return {
        listSel: []
      };
    },
    methods: {
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 创建生产投料入库单
      handleCreatePurStore() {
        if (!this.listSel || this.listSel.length === 0) {
          this.$mW("请先勾选组件行");
          return;
        }
        this.szdPop("确定要基于所选组件行创建生产投料单据吗？", true)
          .then(() => {
            // 将选中组件数据映射为入库行项目
            let purStoreItemList = [];
            this.listSel.forEach(item => {
              purStoreItemList.push({
                mtlCode: item.mtlCode || "",
                fact: item.fact || "",
                store: item.store || "",
                entryQnt: item.reqQty || "",
                unit: item.unit || "",
                orderNo: this.objData.header.orderNo || "",
                orderItemNo: item.resItem,
                itemText: "行收货"
              });
            });

            let purStoreData = {
              header: {
                routerCode: "Z000_MM_CGRK_01A",
                bstp: "Z000_MM_CGRK02",
                title: "生产投料"
              },
              itemList: purStoreItemList
            };

            createFromPurOrder(purStoreData)
              .then(response => {
                if (response.code === 200 && response.data.status === "S") {
                  this.$mS("创建生产投料成功");
                  this.szdWf.goDoc("Z000_MM_CGRK_01A", response.data.docId, "U");
                } else {
                  this.$mE("创建生产投料失败");
                }
              })
              .catch(() => {
                this.$mE("创建生产投料失败");
              });
          })
          .catch(() => {});
      }
    }
  };
</script>
