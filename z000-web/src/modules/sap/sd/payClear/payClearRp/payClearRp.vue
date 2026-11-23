<template>
  <div class="app-container flex-y">
    <!-- 查询条件 -->
    <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSeniorSearchEventImp" />

    <!-- 表格输出 -->
    <szd-table
      :layout="tableParams.layout"
      :toolbar="tableParams.toolbar"
      :fieldCat="tableParams.fieldCat"
      v-model:data="dataList"
      v-model:page="tableParams.page"
      @handleRequest="handleRequestImp"
      :callMethod="handleCallMethod"
      row-key="belnr"
      border
      stripe />
  </div>
</template>

<script>
  import { queryPayClear } from "../api/payClear.js";

  export default {
    name: "Z000_SD_PAYCLEAR_01B",
    data() {
      return {
        queryParams: { ranges: {}, orders: [] },
        schLay: {
          layout: { report: "Z000_SD_PAYCLEAR_01B", span: 8, more: 3 },
          schCat: [
            {
              field: "bukrs",
              label: "公司",
              type: "string",
              selectOpt: true,
              help: { a: "CORE_CMPY" }
            },
            {
              field: "lifnr",
              label: "供应商",
              type: "string",
              help: { a: "CORE_SUPP", c: "supp" }
            },
            {
              field: "kunnr",
              label: "客户",
              type: "string",
              help: { a: "CORE_CUST", c: "cust" }
            },
            { field: "budatRange", label: "过账日期", type: "date", whereOpt: "BT" },
            { field: "blart", label: "凭证类型", type: "string" },
            { field: "hkont", label: "科目", type: "string", help: { a: "CORE_ACCS" } },
            { field: "gjahr", label: "年度", type: "string" },
            { field: "belnr", label: "凭证号", type: "string" }
          ]
        },
        tableParams: {
          layout: {
            report: "Z000_SD_PAYCLEAR_01B",
            showSummary: false,
            merge: "0",
            height: "calc(100vh - 220px)"
          },
          toolbar: { btns: [] },
          fieldCat: [
            { field: "bukrs", label: "公司代码", minWidth: 100 },
            { field: "belnr", label: "会计凭证编号", minWidth: 130 },
            { field: "gjahr", label: "财年", minWidth: 80 },
            { field: "blart", label: "凭证类型", minWidth: 100 },
            { field: "bktxt", label: "凭证抬头文本", minWidth: 180 },
            { field: "waers", label: "货币码", minWidth: 90 },
            { field: "buzei", label: "行项目数", minWidth: 90 },
            { field: "bschl", label: "记帐代码", minWidth: 90 },
            { field: "shkzg", label: "借/贷", minWidth: 70 },
            { field: "wrbtr", label: "凭证货币金额", minWidth: 130, type: "number", numDec: 2 },
            { field: "lifnr", label: "供应商", minWidth: 110 },
            { field: "lifnrT", label: "供应商名称", minWidth: 180 },
            { field: "kunnr", label: "客户", minWidth: 110 },
            { field: "kunnrT", label: "客户名称", minWidth: 180 },
            { field: "hkont", label: "总账科目", minWidth: 110 },
            { field: "hkontT", label: "科目文本", minWidth: 200 },
            { field: "zuonr", label: "分配编号", minWidth: 130 },
            { field: "sgtxt", label: "项目文本", minWidth: 200 }
          ],
          page: { total: 0, pageNum: 1, pageSize: 20 },
          oper: {}
        },
        dataList: []
      };
    },
    methods: {
      // 高级查询按钮回调
      handleSeniorSearchEventImp(obj) {
        switch (obj) {
          case "$search":
            this.getList();
            break;
          case "$reset":
            this.handleReset();
            break;
        }
      },
      handleReset() {
        this.queryParams.ranges = {};
        this.dataList = [];
        this.tableParams.page.total = 0;
      },
      getList() {
        const submitParams = JSON.parse(JSON.stringify(this.queryParams));
        const ranges = submitParams.ranges;
        // budatRange 拆分为 budatStart / budatEnd
        if (ranges.budatRange && ranges.budatRange[0]) {
          const dr = ranges.budatRange[0];
          ranges.budatStart = [{ type: "date", option: "BT", low: dr.low || "" }];
          ranges.budatEnd = [{ type: "date", option: "BT", low: dr.high || "" }];
          delete ranges.budatRange;
        }
        submitParams.pageNum = this.tableParams.page.pageNum;
        submitParams.pageSize = this.tableParams.page.pageSize;
        queryPayClear(submitParams).then(response => {
          this.dataList = response.rows;
          this.tableParams.page.total = response.total;
        });
      },
      // 表格请求回调
      handleRequestImp() {
        this.getList();
      },
      //AI回调
      async handleCallMethod(obj) {
        console.log("handleCallMethod", obj);
        if (obj.fCode === "$aiPageDataList") {
          let _query = {
            pageNum: obj.page.pageNum,
            pageSize: obj.page.pageSize,
            orders: this.queryParams.orders,
            ranges: this.queryParams.ranges
          };
          let response = await queryPayClear(_query);
          if (response.code === 200) {
            obj.page.total = response.total;
            return response.rows || [];
          } else {
            obj.page.total = 0;
            return [];
          }
        }
      }
    }
  };
</script>
