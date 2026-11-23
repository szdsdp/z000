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
      <el-table-column label="行项目" prop="itemNo" min-width="60" align="center" fixed="left">
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
      <!-- 摘要 -->
      <el-table-column :label="$t('摘要')" prop="summary" min-width="200">
        <template v-slot="scope">
          <szd-input v-model="scope.row.summary" :maxlength="50" clearable :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <!-- 会计科目 -->
      <el-table-column label="会计科目" prop="accs" min-width="250">
        <template v-slot:header>
          <szd-table-col-header label="会计科目" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.accs"
            showHelp
            :help="{ a: 'CORE_ACCS', b: scope.row, c: ['accs', 'accsName'] }"
            :szdopt="SR.szdopt"
            :descSpan="12"
            :desc="scope.row.accsName" />
        </template>
      </el-table-column>
      <!-- 特别总账标识 -->
      <el-table-column :label="$t('特别总账标识')" prop="sgl" min-width="120">
        <template v-slot="scope">
          <szd-select v-model="scope.row.sgl" baseField="CORE_VCH_ACCGL" clearable :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <!-- 供应商 -->
      <el-table-column label="供应商" prop="supp" min-width="250">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.supp"
            showHelp
            :help="{ a: 'CORE_SUPP', b: scope.row, c: ['supp', 'suppName'] }"
            :szdopt="SR.szdopt"
            :descSpan="12"
            :desc="scope.row.suppName" />
        </template>
      </el-table-column>
      <el-table-column label="客户" prop="cust" min-width="250">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.cust"
            showHelp
            :help="{ a: 'CORE_CUST', b: scope.row, c: ['cust', 'custName'] }"
            :szdopt="SR.szdopt"
            :descSpan="12"
            :desc="scope.row.custName" />
        </template>
      </el-table-column>
      <!-- 成本中心 -->
      <el-table-column :label="$t('成本中心')" prop="cstc" min-width="250">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.cstc"
            showHelp
            :help="{ a: 'CORE_CSTC', b: scope.row, c: ['cstc', 'cstcName'], d: [['cmpy', objData.header.cmpy]] }"
            :szdopt="SR.szdopt"
            :descSpan="12"
            :desc="scope.row.cstcName" />
        </template>
      </el-table-column>
      <!-- 利润中心 -->
      <el-table-column :label="$t('利润中心')" prop="prfc" min-width="250">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.prfc"
            showHelp
            :help="{ a: 'CORE_PRFC', b: scope.row, c: ['prfc', 'prfcName'] }"
            :szdopt="SR.szdopt"
            :descSpan="12"
            :desc="scope.row.prfcName" />
        </template>
      </el-table-column>
      <el-table-column :label="$t('备注')" prop="remark" min-width="150">
        <template v-slot="scope">
          <szd-input v-model="scope.row.remark" :maxlength="128" clearable :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
    </el-table>
    <z1ItemTab10Info ref="z1ItemTab10Info" :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>
<script>
  import { calcAddRow, calcAmt, calcAmtTax, calcTax, calcAmtHeader } from "./js/amtCalc";
  import z1ItemTab10Info from "./z1ItemTab10Info.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      z1ItemTab10Info
    },
    data() {
      return {
        listSel: [],
        listCtrl: {},
        moduleName: "itemList"
      };
    },
    methods: {
      calcTax,
      calcAmt,
      calcAmtTax,

      addRows() {
        let uploadParam = {
          acceptTxt: "xlsx格式", //上传的地址
          fileName: "行项目数据",
          title: "",
          dialogWidth: 300,
          dialogHeight: 300
        };
        let fCats = [
          { field: "bsub", label: "业务小类", type: "string" },
          { field: "taxr", label: "税率", type: "string" },
          { field: "amt", label: "金额", type: "number" },
          { field: "amtTax", label: "税额", type: "number" },
          { field: "amtNoTax", label: "金额(不含税)", type: "number" },
          { field: "summary", label: "摘要", type: "string" },
          { field: "accs", label: "会计科目", type: "string" },
          { field: "sgl", label: "特别总账标识", type: "string" },
          { field: "supp", label: "供应商", type: "string" },
          { field: "cust", label: "客户", type: "string" },
          { field: "cstc", label: "成本中心", type: "string" },
          { field: "prfc", label: "利润中心", type: "string" },
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
            this.objData.itemList.push(rows[i]);
          }
        }
      },
      addRow() {
        calcAddRow(this.objData, this.SR, this.objData.itemList, this.moduleName).then(resItem => {
          if (resItem) {
            this.objData.itemList.push(resItem);
          }
        });
      },
      delRow() {
        if (this.listSel.length === 0) {
          this.$mW("请至少选择一行需要删除的数据");
          return;
        }
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        calcAmtHeader(this.objData);
      },
      handleSelRow(selection) {
        this.listSel = selection;
      },
      handleUpdRow(row) {
        this.$refs.z1ItemTab10Info.acceptData(row);
      },
      // 合计
      handleSummaryMethod(obj) {
        return this.szdCalc.tableSum(obj, ["amt", "amtTax", "amtNoTax"], 1);
      },
      //设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      }
    }
  };
</script>
