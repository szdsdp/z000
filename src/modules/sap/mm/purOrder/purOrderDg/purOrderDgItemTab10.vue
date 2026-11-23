<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <!-- 功能按钮 -->
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
        <szd-button v-if="objData.header.bussStatus === 'F'" label="创建物料凭证" type="primary" link @click="handleCreateCgrk" icon="Upload" />
      </div>
    </div>
    <!-- 表单输出 -->
    <el-table ref="refTable" border stripe :data="objData.itemList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">
          {{ scope.row.itemNo }}
        </template>
      </el-table-column>
      <el-table-column label="物料编号" prop="mtlCode" min-width="250">
        <template v-slot:header>
          <szd-table-col-header required label="物料编号" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.mtlCode"
            showHelp
            :help="{
              a: 'Z000_MDM_MTL',
              b: scope.row,
              c: ['mtlCode-mtlCode', 'mtlName-mtlName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.mtlName" />
        </template>
      </el-table-column>
      <el-table-column label="工厂" prop="fact" min-width="200">
        <template v-slot:header>
          <szd-table-col-header required label="工厂" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.fact"
            showHelp
            :help="{
              a: 'CORE_FACT',
              b: scope.row,
              c: ['fact-fact', 'factName-factName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.factName" />
        </template>
      </el-table-column>
      <el-table-column label="采购订单数量" prop="purNum" min-width="130">
        <template v-slot:header>
          <szd-table-col-header required label="采购订单数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.purNum" :szdopt="SR.szdopt" :maxlength="13" @change="onPurNumChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="订单单位" prop="unit" min-width="100">
        <template v-slot:header>
          <szd-table-col-header required label="订单单位" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.unit" baseField="CORE_UNIT" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="采购申请编号" prop="applyNo" min-width="140">
        <template v-slot="scope">
          <szd-input v-model="scope.row.applyNo" :szdopt="SR.szdopt" :maxlength="10" disabled />
        </template>
      </el-table-column>
      <el-table-column label="采购申请项目" prop="applyNoItem" width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.applyNoItem" :szdopt="SR.szdopt" :maxlength="5" disabled />
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
      <el-table-column label="交货计划" width="100" align="center" fixed="right">
        <template v-slot="scope">
          <szd-button label="计划" type="primary" link @click="toggleSchedExpand(scope.row, scope.$index)" icon="Timer" />
        </template>
      </el-table-column>
    </el-table>

    <!-- 交货计划弹窗 -->
    <purOrderDgItemTab10Sub10 ref="schedDialog" :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>

<script>
  import purOrderDgItemTab10Sub10 from "./purOrderDgItemTab10Sub10.vue";
  import { createFromPurOrder } from "../../purStore/api/purStore.js";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      purOrderDgItemTab10Sub10
    },
    data() {
      return {
        listSel: []
      };
    },

    methods: {
      handleSelRow(selection) {
        this.listSel = selection;
      },
      addRow() {
        this.objData.itemList.push({
          itemNo: "",
          mtlCode: "",
          fact: "",
          purNum: "",
          unit: "",
          applyNo: "",
          applyNoItem: "",
          deliveryDate: ""
        });
        this.setItemNo();
      },
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
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      },
      toggleSchedExpand(row, index) {
        this.$refs.schedDialog.acceptData(row);
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
            schedLine: 1,
            deliveryDate: row.deliveryDate,
            quantity: row.purNum || ""
          });
        } else if (scheds.length === 1) {
          // 仅1条计划 → 自动更新日期
          scheds[0].deliveryDate = row.deliveryDate;
        }
        // 多条计划时不自动处理（用户已手动管理）
      },
      // 数量变更时自动同步交货计划数量
      onPurNumChange(row) {
        if (!this.objData.schedList) {
          return;
        }
        const scheds = this.objData.schedList.filter(s => s.itemNo === row.itemNo);
        // 仅1条计划时自动更新数量
        if (scheds.length === 1) {
          scheds[0].quantity = row.purNum || "";
        }
      },
      handleCreateCgrk() {
        if (!this.listSel || this.listSel.length === 0) {
          this.$mW("请先勾选行项目");
          return;
        }
        this.szdPop("确定要基于所选行项目创建物料凭证单据吗？", true)
          .then(() => {
            let cgrkItemList = [];
            this.listSel.forEach(item => {
              cgrkItemList.push({
                mtlCode: item.mtlCode || "",
                fact: item.fact || "",
                orderNo: this.objData.header.vchNo || "",
                // 采购订单行号按SAP格式转换（×10，5位补零），统一物料凭证中的订单项目编号格式
                orderItemNo: String(item.itemNo * 10).padStart(5, '0'),
                refBussDocId: this.objData.header.bussDocId || "",
                refBstp: this.objData.header.bstp || "",
                entryQnt: item.purNum || "",
                unit: item.unit || "",
                store: "",
                itemText: ""
              });
            });

            let cgrkData = {
              header: {
                routerCode: "Z000_MM_CGRK_01A",
                bstp: "Z000_MM_CGRK01",
                cmpy: this.objData.header.cmpy,
                cstc: this.objData.header.cstc,
                title: "采购收货"
              },
              itemList: cgrkItemList
            };

            createFromPurOrder(cgrkData)
              .then(response => {
                if (response.code === 200 && response.data.status === "S") {
                  this.$mS("创建物料凭证成功");
                  this.szdWf.goDoc("Z000_MM_CGRK_01A", response.data.docId, "U");
                } else {
                  this.$mE("创建物料凭证失败");
                }
              })
              .catch(() => {
                this.$mE("创建物料凭证失败");
              });
          })
          .catch(() => {});
      }
    }
  };
</script>
