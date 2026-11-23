<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <!-- 表格输出 -->
    <szd-table
        :layout="tabLay.layout"
        :toolbar="tabLay.toolbar"
        :fieldCat="tabLay.fieldCat"
        v-model:data="objData.itemList"
        @handleEvent="handleEventImp"
        @selection-change="handleSelRow"
        border
        stripe/>

  </div>
</template>
<script>
import {calcAddRow, calcAmtHeader} from "@/modules/demo/expCost/expCostDg/js/expCostDgCalc";

export default {
  props: ["objData", "SR", "wfOper"],
  components: {},
  data() {
    return {
      moduleName: "itemList",
      tabLay: {
        layout: {report: "Z000_DEMO_EXP_01B", selection: "2", sort: "2"},
        toolbar: {
          btns: [
            {fCode: "add", label: "新增", icon: "Plus"},
            {fCode: "del", label: "删除", icon: "Delete"}
          ]
        },
        fieldCat: [
          {field: "itemNo", label: "行号", minWidth: 50, type: "number"},
          {
            field: "bsub",
            label: "业务小类",
            minWidth: 150,
            edit: true,
            ctrlParam: {showHelp: true, baseField: "CORE_BSUB01", baseMapping: ["bsub", "bsubName"], descSpan: "6"}
          },
          {
            field: "taxr",
            label: "税率",
            minWidth: 200,
            edit: true,
            ctrlType: "select",
            ctrlParam: {showHelp: true, baseField: "CORE_TAXR"}
          },
          {field: "amt", label: "金额", minWidth: 180, edit: true, type: "number"},
          {field: "amtTax", label: "税额", minWidth: 180, type: "number"},
          {field: "amtNoTax", label: "金额(不含税)", minWidth: 180, edit: true, type: "number"},
          {field: "remark", label: "备注", minWidth: 120, edit: true,},
        ],
      },
      listSel: []
    };
  },
  methods: {
    // 表格按钮回调
    handleEventImp(obj) {
      switch (obj.fCode) {
        case "add":
          this.addRow();
          break;
        case "del":
          this.delRow();
          break;
      }
    },
    // 选择行
    handleSelRow(selection) {
      this.listSel = selection;
    },
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
  }
};
</script>
