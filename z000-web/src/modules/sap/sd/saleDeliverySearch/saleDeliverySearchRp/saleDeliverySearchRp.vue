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
      row-key="posnr"
      border
      stripe />
  </div>
</template>

<script>
  import { querySaleDeliverySearch } from "../api/saleDeliverySearch.js";

  export default {
    name: "Z000_SD_SALE_DELIVERY_SEARCH_01B",
    data() {
      return {
        queryParams: { ranges: {}, orders: [] },
        schLay: {
          layout: { report: "Z000_SD_SALE_DELIVERY_SEARCH_01B", span: 8, more: 3 },
          schCat: [
            {
              field: "kunnr",
              label: "送达方",
              type: "string",
              required: true,
              noTo: true,
              help: { a: "CORE_CUST", c: "cust" }
            },
            { field: "vbeln", label: "交货单", type: "string" },
            {
              field: "vkorg",
              label: "销售组织",
              type: "string",
              required: true,
              selectOpt: true,
              help: { a: "Z000_MDM_ORG_SALE" }
            },
            { field: "dateRange", label: "创建日期", type: "date", whereOpt: "BT" }
          ]
        },
        tableParams: {
          layout: {
            report: "Z000_SD_SALE_DELIVERY_SEARCH_01B",
            showSummary: false,
            merge: "0",
            height: "calc(100vh - 220px)"
          },
          toolbar: { btns: [] },
          fieldCat: [
            { field: "vbelnVl", label: "交货单", minWidth: 120 },
            { field: "erdat", label: "创建日期", minWidth: 120 },
            { field: "bzirk", label: "销售地区", minWidth: 100 },
            { field: "vstel", label: "装运点", minWidth: 100 },
            { field: "vkorg", label: "销售机构", minWidth: 100 },
            { field: "lfart", label: "交货类型", minWidth: 100 },
            { field: "lddat", label: "装载日期", minWidth: 120 },
            { field: "lfdat", label: "交货日期", minWidth: 120 },
            { field: "kodat", label: "拣配日期", minWidth: 120 },
            { field: "kunnr", label: "送达方", minWidth: 100 },
            { field: "kunag", label: "售达方", minWidth: 100 },
            { field: "posnr", label: "交货项目", minWidth: 90 },
            { field: "matnr", label: "物料编号", minWidth: 120 },
            { field: "matkl", label: "物料组", minWidth: 100 },
            { field: "werks", label: "工厂", minWidth: 90 },
            { field: "lgort", label: "库存地点", minWidth: 100 },
            { field: "lfimg", label: "实际已交货量", minWidth: 120, type: "number", numDec: 3 },
            { field: "vrkme", label: "销售单位", minWidth: 100 },
            { field: "vbelnVa", label: "销售凭证", minWidth: 120 },
            { field: "vbelnVf", label: "开票凭证", minWidth: 120 },
            { field: "fkart", label: "开票类型", minWidth: 100 },
            { field: "posnrVf", label: "出具发票项目", minWidth: 120 },
            { field: "fkimg", label: "实际已开票数量", minWidth: 130, type: "number", numDec: 3 }
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
        // dateRange 拆分为 bdate / edate
        if (ranges.dateRange && ranges.dateRange[0]) {
          const dr = ranges.dateRange[0];
          ranges.bdate = [{ type: "date", option: "BT", low: dr.low || "" }];
          ranges.edate = [{ type: "date", option: "EQ", low: dr.high || "" }];
          delete ranges.dateRange;
        }
        submitParams.pageNum = this.tableParams.page.pageNum;
        submitParams.pageSize = this.tableParams.page.pageSize;
        querySaleDeliverySearch(submitParams).then(response => {
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
          let response = await querySaleDeliverySearch(_query);
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
