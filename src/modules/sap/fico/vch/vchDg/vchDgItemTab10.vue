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
      <el-table-column label="行项目" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">
          {{ scope.row.itemNo }}
        </template>
      </el-table-column>
      <el-table-column label="业务小类" prop="bsubl" min-width="200">
        <template v-slot:header>
          <szd-table-col-header required label="业务小类" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.bsubl"
            showHelp
            :help="{
              a: 'CORE_BSUB01',
              b: scope.row,
              c: ['bsubl-bsub', 'bsublName-bsubName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.bsublName" />
        </template>
      </el-table-column>
      <el-table-column label="抵扣" prop="deduFlag" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="抵扣" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.deduFlag" baseField="Z000_DEDU" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="税率" prop="taxr" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="税率" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.taxr" baseField="CORE_TAXR" :szdopt="SR.szdopt" @change="calcAmtFromTaxr(scope.row)" clearable />
        </template>
      </el-table-column>
      <el-table-column label="税额" prop="amtTax" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.amtTax" :szdopt="SR.szdopt" type="number" numDot :numDec="2" @change="calcAmtSum(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="不含税金额" prop="amtNoTax" min-width="130">
        <template v-slot:header>
          <szd-table-col-header required label="不含税金额" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.amtNoTax" :szdopt="SR.szdopt" type="number" numDot :numDec="2" @change="calcAmtFromTaxr(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="含税金额" prop="amt" min-width="130">
        <template v-slot:header>
          <szd-table-col-header label="含税金额" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.amt" :szdopt="SR.szdopt" disabled type="number" numDot :numDec="2" />
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
      addRow() {
        this.objData.itemList.push({
          itemNo: "",
          bsubl: "",
          bsublName: "",
          deduFlag: "",
          taxr: "",
          amt: "",
          amtTax: "",
          amtNoTax: ""
        });
        this.setItemNo();
      },
      delRow() {
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
        this.calcAmtHeader();
      },
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      },
      // 税率/不含税金额变化 → 税额 = 不含税金额 × 税率% → 含税金额 = 不含税金额 + 税额
      calcAmtFromTaxr(row) {
        const amtNoTax = Number(row.amtNoTax) || 0;
        const taxr = Number(row.taxr) || 0;
        if (amtNoTax > 0 && taxr > 0) {
          // 税额 = 不含税金额 × 税率 / 100
          row.amtTax = (amtNoTax * taxr / 100).toFixed(2);
        }
        // 含税金额 = 不含税金额 + 税额
        const amtTax = Number(row.amtTax) || 0;
        if (amtNoTax > 0 || amtTax > 0) {
          row.amt = (amtNoTax + amtTax).toFixed(2);
        } else {
          row.amt = "";
        }
        this.calcAmtHeader();
      },
      // 手动修改税额 → 含税金额 = 不含税金额 + 税额（不做公式回推）
      calcAmtSum(row) {
        const amtNoTax = Number(row.amtNoTax) || 0;
        const amtTax = Number(row.amtTax) || 0;
        if (amtNoTax > 0 || amtTax > 0) {
          row.amt = (amtNoTax + amtTax).toFixed(2);
        } else {
          row.amt = "";
        }
        this.calcAmtHeader();
      },
      // 抬头申请金额汇总（按含税金额汇总）
      calcAmtHeader() {
        let total = 0;
        for (let i = 0; i < this.objData.itemList.length; i++) {
          total += Number(this.objData.itemList[i].amt) || 0;
        }
        this.objData.header.amtApply = total.toFixed(2);
      }
    }
  };
</script>
