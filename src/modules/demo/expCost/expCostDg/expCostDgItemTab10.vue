<template>
  <div class="szd__tb__wrapper">
    <!-- 功能按钮 -->
    <div class="szd__tb__top" v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)" style="justify-content: space-between">
      <div>
        <szd-checkbox-all v-model:data="objData.itemList" :szdopt="SR.szdopt" @selection-change="handleSelRow" />
      </div>
      <div>
        <szd-button label="新增" :szdopt="SR.szdopt" @click="addRow" />
        <szd-button label="删除" :szdopt="SR.szdopt" @click="delRow" />
      </div>
    </div>

    <!-- 表格输出 -->
    <div class="szd__tb__main">
      <szd-card-a v-for="(row, index) in objData.itemList">
        <template #header>
          <szd-card-row-title
            v-model="row.$SZDCHECKBOX"
            :index="row.itemNo"
            checkbox
            btnCopy
            btnDelete
            @copy="handleRowCopy(row)"
            @delete="handleRowDelete(row)" />
        </template>
        <template #main>
          <szd-input
            label="业务小类"
            v-model="row.bsub"
            showHelp
            :help="{
              a: 'CORE_BSUB01',
              b: row,
              c: ['bsub', 'bsubName'],
              d: [['bstp', objData.header.bstp]]
            }"
            required
            :szdopt="SR.szdopt"
            :desc="row.bsubName" />
          <szd-select
            label="税率"
            v-model="row.taxr"
            baseField="CORE_TAXR"
            :showKey="false"
            required
            :szdopt="SR.szdopt"
            @change="calcTax(objData, SR, row)" />
          <szd-input
            label="金额"
            v-model="row.amt"
            type="number"
            numDot
            :numDec="2"
            :numMax="100000000000"
            :numMin="0"
            required
            :szdopt="SR.szdopt"
            @change="calcAmt(objData, SR, row)" />
          <szd-input
            label="税额"
            v-model="row.amtTax"
            type="number"
            numDot
            :numDec="2"
            :numMax="100000000000"
            :numMin="0"
            required
            :disabled="row.taxr == '0'"
            :szdopt="SR.szdopt"
            @change="calcAmtTax(objData, SR, row)" />
          <szd-input label="金额(不含税)" v-model="row.amtNoTax" type="number" numDot :numDec="2" disabled :span="12" />
          <szd-textarea label="备注" v-model="row.remark" clearable :maxlength="150" :rows="2" szdopt="SR.szdopt" />
        </template>
      </szd-card-a>
      <!-- 未查询到数据 -->
      <szd-nothing v-if="!objData.itemList || objData.itemList.length === 0" />
    </div>
  </div>
</template>
<script>
  import { calcAddRow, calcAmt, calcAmtTax, calcTax, calcAmtHeader, getMaxItemNo } from "./js/expCostDgCalc";
  import _ from "lodash";

  export default {
    props: ["objData", "SR", "wfOper"],

    data() {
      return {
        moduleName: "demoExpCostItem",
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
      // 复制行
      handleRowCopy(row) {
        let _row = _.cloneDeep(row);
        _row.itemNo = getMaxItemNo(this.objData.itemList);
        this.objData.itemList.push(_row);
      },
      // 删除行
      handleRowDelete(row) {
        this.objData.itemList = this.objData.itemList.filter(item => item !== row);
      }
    }
  };
</script>
