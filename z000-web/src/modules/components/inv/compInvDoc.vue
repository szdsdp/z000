<template>
  <div class="szd__tb__wrapper" style="margin-top: 0">
    <!-- 表单输出 -->
    <szd-table
      :layout="tabLay.layout"
      :toolbar="tabLay.toolbar"
      :fieldCat="tabLay.fieldCat"
      v-model:data="objData.invList"
      row-key="invNo"
      border
      stripe />
  </div>
</template>
<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {
        tabLay: {
          layout: { report: "" },
          toolbar: { hide: true },
          fieldCat: [
            { field: "checkFlag", label: "验真标识", minWidth: 120 },
            { field: "invNo", label: "发票号码", minWidth: 120 },
            { field: "invCode", label: "发票代码", minWidth: 120 },
            { field: "invTypeCode", label: "票据类型", minWidth: 150 },
            { field: "invDate", label: "发票日期", minWidth: 100 },
            { field: "invAmtTax", label: "税额", minWidth: 120, type: "number", numDec: 2, doSum: "1" },
            { field: "invAmtNoTax", label: "金额", minWidth: 120, type: "number", numDec: 2, doSum: "1" },
            { field: "invAmt", label: "价税合计", minWidth: 120, type: "number", numDec: 2, doSum: "1" },
            { field: "invCheckCode", label: "发票校验码", minWidth: 160 },
            { field: "sellerName", label: "销方信息", minWidth: 280 },
            { field: "sellerTaxNum", label: "纳税人识别号", minWidth: 160 }
          ]
        }
      };
    },
    methods: {}
  };
</script>