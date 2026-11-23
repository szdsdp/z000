<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <!-- 功能按钮 -->
    <div class="main-table-title">
      <div class="main-table-title-left" style="width: 50%" />
      <div class="main-table-title-right" style="width: 50%">
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="导入数据"
          type="primary"
          link
          @click="addRows"
          icon="Plus"
          :szdopt="SR.szdopt" />
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
    <!-- 表格输出 -->
    <el-table
      ref="refTable"
      border
      stripe
      :data="objData.itemList"
      @selection-change="handleSelRow"
      show-summary
      :summary-method="handleSummaryMethod"
      class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" min-width="60" align="center" fixed="left">
        <template v-slot="scope">
          <el-button type="primary" link @click="handleUpdRow(scope.row)">{{ scope.row.itemNo }}</el-button>
        </template>
      </el-table-column>
      <!-- 业务小类 -->
      <el-table-column label="业务小类" prop="bsub" min-width="250">
        <template v-slot:header>
          <szd-table-col-header required label="业务小类" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.bsub"
            showHelp
            :help="{
              a: 'CORE_BSUB01',
              b: scope.row,
              c: ['bsub', 'bsubName'],
              d: [
                ['bstp', objData.header.bstp],
                ['structCode', moduleName]
              ]
            }"
            :szdopt="SR.szdopt"
            :descSpan="12"
            :desc="scope.row.bsubName" />
        </template>
      </el-table-column>
      <el-table-column label="税率" prop="taxr" min-width="80">
        <template v-slot:header>
          <szd-table-col-header required label="税率" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.taxr"
            baseField="CORE_TAXR"
            :showKey="false"
            first
            :szdopt="SR.szdopt"
            @change="calcTax(objData, SR, scope.row)" />
        </template>
      </el-table-column>
      <!-- 申请金额 -->
      <el-table-column label="金额" prop="amt" min-width="120" align="right">
        <template v-slot:header>
          <szd-table-col-header required label="金额" />
        </template>
        <template v-slot="scope">
          <szd-input
            type="number"
            v-model="scope.row.amt"
            numDot
            :numDec="2"
            :numMax="100000000000"
            :numMin="0"
            :szdopt="SR.szdopt"
            @change="calcAmt(objData, SR, scope.row)" />
        </template>
      </el-table-column>
      <el-table-column :label="$t('税额')" prop="amtTax" min-width="120" align="right">
        <template v-slot="scope">
          <szd-input
            type="number"
            v-model="scope.row.amtTax"
            numDot
            :numDec="2"
            :numMax="100000000000"
            :numMin="0"
            :disabled="scope.row.taxr == '0'"
            :szdopt="SR.szdopt"
            @change="calcAmtTax(objData, SR, scope.row)" />
        </template>
      </el-table-column>
      <el-table-column :label="$t('金额(不含税)')" prop="amtNoTax" min-width="120" align="right">
        <template v-slot="scope">
          <szd-input type="number" v-model="scope.row.amtNoTax" numDot :numDec="2" disabled />
        </template>
      </el-table-column>
      <el-table-column :label="$t('备注')" prop="remark" min-width="150">
        <template v-slot="scope">
          <szd-input v-model="scope.row.remark" :maxlength="128" clearable :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
    </el-table>

    <expCostItemTab10Info ref="expCostItemTab10Info" :objData="objData" :SR="SR" :wfOper="wfOper" :moduleName="moduleName" />
  </div>
</template>
<script>
  import { calcAddRow, calcAmt, calcAmtTax, calcTax, calcAmtHeader } from "./js/expCostDgCalc";
  import expCostItemTab10Info from "./expCostItemTab10Info.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      expCostItemTab10Info
    },
    data() {
      return {
        moduleName: "itemList",
        listSel: []
      };
    },
    methods: {
      calcTax,
      calcAmt,
      calcAmtTax,
      // 新增行
      addRow() {
        calcAddRow(this.objData, this.SR, this.objData.itemList, this.moduleName).then(resItem => {
          if (resItem) {
            this.objData.itemList.push(resItem);
          }
        });
      },
      // 删除行
      delRow() {
        if (this.listSel.length === 0) {
          this.$mW("请至少选择一行需要删除的数据");
          return;
        }
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        calcAmtHeader(this.objData);
      },
      // 选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 明细显示
      handleUpdRow(row) {
        this.$refs.expCostItemTab10Info.initExpCostItemInfo(row);
      },
      // 合计
      handleSummaryMethod(obj) {
        return this.szdCalc.tableSum(obj, ["amt", "amtTax", "amtNoTax"], 1);
      },
      // 导入数据
      addRows() {
        let uploadParam = { acceptTxt: "xlsx格式", fileName: "费用明细数据" };
        let fCats = [
          { field: "bsub", label: "业务小类", type: "string" },
          { field: "taxr", label: "税率", type: "string" },
          { field: "amt", label: "金额", type: "number" },
          { field: "amtTax", label: "税额", type: "number" },
          { field: "remark", label: "备注", type: "string" }
        ];
        this.szdUploadExcel(uploadParam, fCats).then(res => {
          this.uploadResult(res);
          this.setItemNo();
        });
      },
      uploadResult(rows) {
        if (rows && rows.length > 0) {
          for (let i = 0; i < rows.length; i++) {
            if (!rows[i].taxr) rows[i].taxr = "0";
            if (!rows[i].amtTax) {
              this.calcTax(this.objData, this.SR, rows[i]);
            }
            this.objData.itemList.push(rows[i]);
          }
          calcAmtHeader(this.objData);
        }
      },
      //设置行号
      setItemNo() {
        let _maxItemNo = 0;
        for (let i = 0; i < this.objData.itemList.length; i++) {
          let item = this.objData.itemList[i];
          if (_maxItemNo < item.itemNo) _maxItemNo = item.itemNo;
        }
        for (let i = 0; i < this.objData.itemList.length; i++) {
          let item = this.objData.itemList[i];
          if (item.itemNo === 0) {
            _maxItemNo = _maxItemNo + 1;
            item.itemNo = _maxItemNo;
          }
        }
      }
    }
  };
</script>
