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
      </div>
    </div>
    <el-table ref="refTable" border stripe :data="objData.itemList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">{{ scope.row.itemNo }}</template>
      </el-table-column>
      <el-table-column label="销售订单号" prop="orderNo" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="销售订单号" />
        </template>
        <template v-slot="scope">
          <szd-input-desc v-model="scope.row.orderNo" disabled />
        </template>
      </el-table-column>
      <el-table-column label="销售订单行号" prop="orderItemNo" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.orderItemNo" disabled />
        </template>
      </el-table-column>
      <el-table-column label="物料编码" prop="mtlCode" min-width="200">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.mtlCode"
            showHelp
            :help="{ a: 'Z000_MDM_MTL', b: scope.row, c: ['mtlCode-mtlCode', 'mtlName-mtlName', 'unit-unit'] }"
            disabled
            :descSpan="10"
            :desc="scope.row.mtlName" />
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
      <el-table-column label="订单数量" prop="orderQty" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="订单数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.orderQty" disabled type="number" numDot :numDec="3" />
        </template>
      </el-table-column>
      <el-table-column label="未交货数量" prop="undeliveredQty" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="未交货数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.undeliveredQty" disabled type="number" numDot :numDec="3" />
        </template>
      </el-table-column>
      <el-table-column label="交货数量" prop="dlvQty" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="交货数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.dlvQty" :szdopt="SR.szdopt" type="number" numDot :numDec="3" @change="handleDlvQtyChange(scope.row)" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
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
      // 新增行-搜索帮助选择销售订单
      addRow() {
        this.szdShShow({
          a: "Z000_SD_SALE_ORDER_SAP"
        }).then(res => {
          if (res.data && res.data.length > 0) {
            for (let i = 0; i < res.data.length; i++) {
              let item = res.data[i];
              // 销售订单号+行号去重
              if (this.objData.itemList.some(r => r.orderNo === item.vbeln && r.orderItemNo === item.posnr)) {
                continue;
              }
              // 未交货数量 = 目标数量 - 已创建交货单数量
              const undeliveredQty = (parseFloat(item.kwmeng) || 0) - (parseFloat(item.zjhds) || 0);
              const newRow = {
                itemNo: 0,
                orderNo: item.vbeln || "",
                orderItemNo: item.posnr || "",
                mtlCode: item.matnr || "",
                mtlName: item.arktx || "",
                orderQty: item.kwmeng || "",
                undeliveredQty: undeliveredQty > 0 ? String(undeliveredQty) : "0",
                dlvQty: "",
                unit: item.zieme || "",
                unitName: ""
              };
              this.objData.itemList.push(newRow);
            }
            this.setItemNo();
          }
        });
      },
      // 交货数量校验：不能超过未交货数量
      handleDlvQtyChange(row) {
        const undelivered = parseFloat(row.undeliveredQty) || 0;
        const dlvQty = parseFloat(row.dlvQty) || 0;
        if (dlvQty > undelivered) {
          this.$mW(`第${row.itemNo}行交货数量不能超过未交货数量(${undelivered})`);
          this.$nextTick(() => {
            row.dlvQty = "";
          });
        }
      },
      delRow() {
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      }
    }
  };
</script>
