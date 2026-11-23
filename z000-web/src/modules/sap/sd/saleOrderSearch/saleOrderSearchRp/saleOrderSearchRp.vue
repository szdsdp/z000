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
      row-key="ebelp"
      border
      stripe />
  </div>
</template>

<script>
  import { querySaleOrderSearch } from "../api/saleOrderSearch.js";

  export default {
    name: "Z000_SD_SALE_ORDER_SEARCH_01B",
    data() {
      return {
        queryParams: { ranges: {}, orders: [] },
        schLay: {
          layout: { report: "Z000_SD_SALE_ORDER_SEARCH_01B", span: 8, more: 3 },
          schCat: [
            { field: "kunnr", label: "客户", type: "string", noTo: true, required: true, help: { a: "CORE_CUST" } },
            { field: "vbeln", label: "销售订单", type: "string", noTo: true },
            { field: "auart", label: "订单类型", type: "string", noTo: true, help: { a: "Z000_SD_ORDER_TYPE" } },
            { field: "vkorg", label: "销售组织", type: "string", noTo: true, required: true, help: { a: "Z000_MDM_ORG_SALE" } },
            { field: "dateRange", label: "创建日期", type: "date", whereOpt: "BT" }
          ]
        },
        tableParams: {
          layout: {
            report: "Z000_SD_SALE_ORDER_SEARCH_01B",
            showSummary: false,
            merge: "0",
            height: "calc(100vh - 220px)"
          },
          toolbar: { btns: [] },
          fieldCat: [
            { field: "vbeln", label: "销售凭证", minWidth: 120 },
            { field: "erdat", label: "创建日期", minWidth: 120 },
            { field: "auart", label: "销售凭证类型", minWidth: 130 },
            { field: "vkorg", label: "销售机构", minWidth: 100 },
            { field: "vtweg", label: "分销渠道", minWidth: 100 },
            { field: "kunnr", label: "客户编号", minWidth: 100 },
            { field: "posnr", label: "项目", minWidth: 80 },
            { field: "matnr", label: "物料编号", minWidth: 120 },
            { field: "matkl", label: "物料组", minWidth: 100 },
            { field: "arktx", label: "短文本", minWidth: 200 },
            { field: "kwmeng", label: "目标数量", minWidth: 110, type: "number", numDec: 3 },
            { field: "zieme", label: "计量单位", minWidth: 100 },
            { field: "zjhds", label: "已创建交货单数量", minWidth: 140, type: "number", numDec: 3 },
            { field: "zfhds", label: "已交货数量", minWidth: 120, type: "number", numDec: 3 }
          ],
          page: { total: 0, pageNum: 1, pageSize: 20 },
          oper: {}
        },
        dataList: []
      };
    },
    methods: {
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
        // dateRange 拆分为 bdate / edate
        if (ranges.dateRange && ranges.dateRange[0]) {
          const dr = ranges.dateRange[0];
          ranges.bdate = [{ type: "date", option: "BT", low: dr.low || "" }];
          ranges.edate = [{ type: "date", option: "EQ", low: dr.high || "" }];
          delete ranges.dateRange;
        }
        submitParams.pageNum = this.tableParams.page.pageNum;
        submitParams.pageSize = this.tableParams.page.pageSize;
        querySaleOrderSearch(submitParams).then(response => {
          this.dataList = response.rows;
          this.tableParams.page.total = response.total;
        });
      },
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
          let response = await querySaleOrderSearch(_query);
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
