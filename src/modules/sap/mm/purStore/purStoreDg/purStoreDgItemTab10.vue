<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <div class="main-table-title" style="height: 35px">
      <div class="main-table-title-left" style="width: 50%"></div>
      <div class="main-table-title-right" style="width: 50%; align-items: center">
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus) && objData.header.bstp === 'Z000_MM_CGRK01'"
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
        <szd-button
          v-if="objData.header.bussStatus === 'F' && objData.header.bstp === 'Z000_MM_CGRK01'"
          label="发票校验"
          link
          type="primary"
          @click="handleCreateInvCheck"
          icon="Upload" />
      </div>
    </div>
    <el-table ref="refTable" border stripe :data="objData.itemList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">
          {{ scope.row.itemNo }}
        </template>
      </el-table-column>
      <el-table-column label="前置单据业务单号" prop="refBussDocId" min-width="170">
        <template v-slot="scope">
          <szd-input v-model="scope.row.refBussDocId" :szdopt="SR.szdopt" disabled dblclick
                     @dblclick="handleGoToRefDoc(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="订单号" prop="orderNo" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="订单号" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.orderNo" disabled />
        </template>
      </el-table-column>
      <el-table-column label="订单项目编号" prop="orderItemNo" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="订单项目编号" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.orderItemNo" disabled />
        </template>
      </el-table-column>
      <el-table-column label="订单行数量" prop="orderQty" min-width="140">
        <template v-slot="scope">
          <szd-input v-model="scope.row.orderQty" :szdopt="SR.szdopt" type="number" numDot :numDec="3" disabled />
        </template>
      </el-table-column>
      <el-table-column label="物料编码" prop="mtlCode" min-width="250">
        <template v-slot:header>
          <szd-table-col-header required label="物料编码" />
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
      <el-table-column label="单位" prop="unit" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="单位" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.unit" baseField="CORE_UNIT" :szdopt="SR.szdopt" />
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
      <el-table-column label="库存地点" prop="store" min-width="200">
        <template v-slot:header>
          <szd-table-col-header required label="库存地点" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.store"
            showHelp
            :help="{
              a: 'CORE_STOR',
              b: scope.row,
              c: ['store-stor', 'storeName-storName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.storeName" />
        </template>
      </el-table-column>
      <el-table-column label="数量" prop="quantity" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.quantity" :szdopt="SR.szdopt" type="number" numDot :numDec="2" />
        </template>
      </el-table-column>
      <el-table-column label="项目文本" prop="itemText" min-width="200">
        <template v-slot:header>
          <szd-table-col-header label="项目文本" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.itemText" :szdopt="SR.szdopt" :maxlength="50" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { createFromPurStore } from "../../invCheck/api/invCheck.js";

  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {
        listSel: []
      };
    },

    methods: {
      handleSelRow(selection) {
        this.listSel = selection;
      },

      // 新增行-搜索帮助抽取SAP采购订单
      addRow() {
        this.szdShShow({
          a: "Z000_MM_PUR_ORDER_SAP"
        }).then(res => {
          if (res.data && res.data.length > 0) {
            for (let i = 0; i < res.data.length; i++) {
              let item = res.data[i];
              // SAP订单项目编号原样取值，不做转换
              const orderItemNo = item.ebelp;
              // 订单号+项目编号去重，已存在则跳过
              if (this.objData.itemList.some(ele => ele.orderNo === item.ebeln && ele.orderItemNo === orderItemNo)) {
                continue;
              }
              const newRow = {
                itemNo: 0,
                mtlCode: item.matnr || "",
                mtlName: item.txz01 || "",
                fact: item.werks || "",
                factName: item.werksT || "",
                store: "",
                quantity: 0,
                orderQty: item.menge || "",
                unit: item.meins || "",
                orderNo: item.ebeln || "",
                orderItemNo: orderItemNo,
                refBussDocId: "",
                refBstp: "Z000_MM_CGRK01"
              };
              this.objData.itemList.push(newRow);
            }
            this.setItemNo();
          }
        });
      },

      delRow() {
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      },
      // 跳转前置单据
      handleGoToRefDoc(row) {
        if (row.refBussDocId && row.refBstp) {
          this.szdWf.goDocId({ bstp: row.refBstp, bussDocId: row.refBussDocId, option: "V" });
        }
      },
      // 生成发票校验
      handleCreateInvCheck() {
        if (!this.listSel || this.listSel.length === 0) {
          this.$mW("请先勾选行项目");
          return;
        }
        this.szdPop("确定要基于所选行项目创建发票校验单据吗？", true)
          .then(() => {
            let invCheckItemList = [];
            this.listSel.forEach(item => {
              invCheckItemList.push({
                orderNo: item.orderNo || "",
                orderItemNo: item.orderItemNo,
                mtlCode: item.mtlCode || "",
                mtlName: item.mtlName || "",
                quantity: item.quantity || "",
                unit: item.unit || "",
                itemText: "",
                taxCode: "",
                itemAmount: ""
              });
            });

            let invCheckData = {
              header: {
                routerCode: "Z000_MM_FPJY_01A",
                bstp: "Z000_MM_FPJY01",
                cmpy: this.objData.header.cmpy,
                cstc: this.objData.header.cstc,
                storeBussDocId: this.objData.header.bussDocId
              },
              itemList: invCheckItemList
            };

            createFromPurStore(invCheckData)
              .then(response => {
                if (response.code === 200 && response.data.status === "S") {
                  this.$mS("创建发票校验成功");
                  this.szdWf.goDoc("Z000_MM_FPJY_01A", response.data.docId, "U");
                } else {
                  this.$mE("创建发票校验失败");
                }
              })
              .catch(() => {
                this.$mE("创建发票校验失败");
              });
          })
          .catch(() => {
          });
      }
    }
  };
</script>
