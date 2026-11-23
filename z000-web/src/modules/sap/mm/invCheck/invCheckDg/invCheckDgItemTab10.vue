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
      <el-table-column label="订单号" prop="orderNo" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="订单号" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.orderNo" disabled :maxlength="10" />
        </template>
      </el-table-column>
      <el-table-column label="订单项目编号" prop="orderItemNo" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="订单项目编号" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.orderItemNo" disabled :maxlength="5" />
        </template>
      </el-table-column>
      <el-table-column label="物料编码" prop="mtlCode" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.mtlCode" :szdopt="SR.szdopt" disabled :maxlength="40" />
        </template>
      </el-table-column>
      <el-table-column label="物料名称" prop="mtlName" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.mtlName" :szdopt="SR.szdopt" disabled :maxlength="200" />
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
      <el-table-column label="税码" prop="taxCode" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="税码" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.taxCode"
            showHelp
            :help="{
              a: 'CORE_TAXR',
              b: scope.row,
              c: ['taxCode-taxrJ']
            }"
            :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="金额" prop="amt" min-width="130">
        <template v-slot:header>
          <szd-table-col-header required label="金额" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.amt" :szdopt="SR.szdopt" type="number" numDot :numDec="2" @change="$emit('update-gross')" />
        </template>
      </el-table-column>
      <el-table-column label="税收金额" prop="taxAmount" min-width="130">
        <template v-slot="scope">
          <szd-input v-model="scope.row.taxAmount" :szdopt="SR.szdopt" type="number" numDot :numDec="2" @change="$emit('update-gross')" />
        </template>
      </el-table-column>
      <el-table-column label="数量" prop="quantity" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.quantity" :szdopt="SR.szdopt" :maxlength="13" type="number" numDot :numDec="2" @change="handleQtyChange(scope.row)" />
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
      // 新增行-搜索帮助抽取SAP物料凭证
      addRow() {
        this.szdShShow({
          a: "Z000_MM_STORE_SAP"
        }).then(res => {
          if (res.data && res.data.length > 0) {
            for (let i = 0; i < res.data.length; i++) {
              let item = res.data[i];
              // SAP订单项目编号原样取值，不做转换
              const orderItemNo = item.ebelp;
              // 订单号+项目编号去重，同一订单行不可重复添加
              if (this.objData.itemList.some(r => r.orderNo === item.ebeln && r.orderItemNo === orderItemNo)) {
                continue;
              }
              const newRow = {
                itemNo: 0,
                orderNo: item.ebeln || "",
                orderItemNo: orderItemNo,
                mtlCode: item.matnr || "",
                mtlName: item.matnrT || "",
                unit: item.meins || "",
                taxCode: item.mwskz || "",
                amt: "",
                taxAmount: "",
                quantity: "",
                itemText: "",
                mmenge: item.mmenge || "0",
                fmenge: item.fmenge || "0"
              };
              this.objData.itemList.push(newRow);
            }
            this.setItemNo();
          }
        });
      },

      // 数量变更校验：不能超过剩余未发票校验数量
      handleQtyChange(row) {
        const mmenge = parseFloat(row.mmenge) || 0;
        const fmenge = parseFloat(row.fmenge) || 0;
        const remaining = mmenge - fmenge;
        const qty = parseFloat(row.quantity) || 0;
        if (qty > remaining) {
          this.$mW(`第${row.itemNo}行数量不能超过剩余未发票校验数量(${remaining})`);
          this.$nextTick(() => {
            row.quantity = "";
          });
        }
      },

      delRow() {
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
        this.$emit("update-gross");
      },
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) this.objData.itemList[i].itemNo = i + 1;
      }
    }
  };
</script>
