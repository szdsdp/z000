<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <div class="main-table-title" style="height: 35px">
      <div class="main-table-title-left" style="width: 50%"></div>
      <div class="main-table-title-right" style="width: 50%; align-items: center">
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="新增"
          type="primary"
          link
          @click="addRow"
          icon="Plus"
          :szdopt="SR.szdopt" />
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="删除"
          type="primary"
          link
          @click="delRow"
          icon="Delete"
          :szdopt="SR.szdopt" />
        <szd-button v-if="objData.header.bussStatus === 'F'" label="生成交货单" type="primary" link @click="createSaleDelivery" icon="Upload" />
      </div>
    </div>
    <el-table ref="refTable" border stripe :data="objData.itemList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">{{ scope.row.itemNo }}</template>
      </el-table-column>
      <el-table-column label="物料编码" prop="mtlCode" min-width="200">
        <template v-slot:header>
          <szd-table-col-header required label="物料编码" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.mtlCode"
            showHelp
            :help="{ a: 'Z000_MDM_MTL', b: scope.row, c: ['mtlCode-mtlCode', 'mtlName-mtlName', 'unit-unit'] }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.mtlName" />
        </template>
      </el-table-column>
      <el-table-column label="数量" prop="entryQnt" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.entryQnt" :szdopt="SR.szdopt" type="number" numDot :numDec="3" @change="onEntryQntChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="单位" prop="unit" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="单位" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.unit" baseField="CORE_UNIT" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="交货日期" prop="deliveryDate" min-width="140">
        <template v-slot:header>
          <szd-table-col-header required label="交货日期" />
        </template>
        <template v-slot="scope">
          <szd-date v-model="scope.row.deliveryDate" :szdopt="SR.szdopt" type="date" @change="onDeliveryDateChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="行文本" prop="itemText" min-width="200">
        <template v-slot:header>
          <szd-table-col-header label="行文本" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.itemText" :szdopt="SR.szdopt" :maxlength="200" />
        </template>
      </el-table-column>
      <el-table-column label="交货计划" width="100" align="center" fixed="right">
        <template v-slot="scope">
          <szd-button label="计划" type="primary" link @click="toggleSchedExpand(scope.row)" icon="Timer" />
        </template>
      </el-table-column>
    </el-table>

    <!-- 交货计划弹窗 -->
    <saleOrderDgItemTab10Sub10 ref="schedDialog" :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>

<script>
  import saleOrderDgItemTab10Sub10 from "./saleOrderDgItemTab10Sub10.vue";
  import { createFromSaleOrder } from "../../saleDelivery/api/saleDelivery.js";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: { saleOrderDgItemTab10Sub10 },
    data() {
      return { listSel: [] };
    },
    methods: {
      // 行勾选变更
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 新增空白行
      addRow() {
        this.objData.itemList.push({
          itemNo: "",
          mtlCode: "",
          mtlName: "",
          entryQnt: "",
          unit: "",
          unitName: "",
          deliveryDate: "",
          itemText: ""
        });
        this.setItemNo();
      },
      // 删除勾选行（同步删除关联的交货计划）
      delRow() {
        // 删除关联的交货计划
        this.listSel.forEach(item => {
          if (this.objData.schedList) {
            this.objData.schedList = this.objData.schedList.filter(s => s.itemNo !== item.itemNo);
          }
        });
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      // 重新编排行项目号
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      },
      // 交货日期变更时自动同步交货计划
      onDeliveryDateChange(row) {
        if (!this.objData.schedList) {
          this.objData.schedList = [];
        }
        const scheds = this.objData.schedList.filter(s => s.itemNo === row.itemNo);
        // 清空交货日期 → 仅1条计划时自动删除
        if (!row.deliveryDate) {
          if (scheds.length === 1) {
            this.objData.schedList = this.objData.schedList.filter(s => s.itemNo !== row.itemNo);
          }
          return;
        }
        // 无计划 → 自动创建一条
        if (scheds.length === 0) {
          this.objData.schedList.push({
            orderId: this.objData.header.orderId || "",
            itemNo: row.itemNo,
            schedNo: 1,
            reqDate: row.deliveryDate,
            reqQty: row.entryQnt || ""
          });
        } else if (scheds.length === 1) {
          // 仅1条计划 → 自动更新日期
          scheds[0].reqDate = row.deliveryDate;
        }
        // 多条计划时不自动处理（用户已手动管理）
      },
      // 数量变更时自动同步交货计划数量
      onEntryQntChange(row) {
        if (!this.objData.schedList) {
          return;
        }
        const scheds = this.objData.schedList.filter(s => s.itemNo === row.itemNo);
        // 仅1条计划时自动更新数量
        if (scheds.length === 1) {
          scheds[0].reqQty = row.entryQnt || "";
        }
      },
      // 打开发货计划弹窗
      toggleSchedExpand(row) {
        this.$refs.schedDialog.acceptData(row);
      },
      // 勾选行项目生成销售交货单
      createSaleDelivery() {
        if (!this.listSel || this.listSel.length === 0) {
          this.$mW("请先勾选行项目");
          return;
        }
        this.szdPop("确定要基于所选行项目创建销售交货单吗？", true)
          .then(() => {
            // 将选中行映射为交货单行项目
            let itemList = [];
            this.listSel.forEach(item => {
              itemList.push({
                orderNo: this.objData.header.vbeln || "",
                orderItemNo: String(item.itemNo),
                mtlCode: item.mtlCode || "",
                orderQty: item.entryQnt || "",
                dlvQty: "",
                unit: item.unit || ""
              });
            });
            // 组装交货单数据
            let data = {
              header: {
                routerCode: "Z000_SD_XSJH_01A",
                bstp: "Z000_SD_XSJH01",
                cmpy: this.objData.header.cmpy,
                cstc: this.objData.header.cstc
              },
              itemList: itemList
            };
            // 调用后端创建草稿交货单，成功后跳转
            createFromSaleOrder(data)
              .then(response => {
                if (response.code === 200 && response.data.status === "S") {
                  this.$mS("创建销售交货单成功");
                  this.szdWf.goDoc("Z000_SD_XSJH_01A", response.data.docId, "U");
                }
              })
          })
          .catch(() => {});
      }
    }
  };
</script>
