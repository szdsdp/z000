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
  import { queryStoreSearch } from "../api/storeSearch.js";

  export default {
    name: "Z000_MM_STORE_SEARCH_01B",
    data() {
      return {
        queryParams: { ranges: {}, orders: [] },
        schLay: {
          layout: { report: "Z000_MM_STORE_SEARCH_01B", span: 8, more: 3 },
          schCat: [
            { field: "bukrs", label: "公司", type: "string", required: true, help: { a: "CORE_CMPY" } },
            { field: "lifnr", label: "供应商", type: "string", required: true, help: { a: "CORE_SUPP" } },
            { field: "waers", label: "工厂", type: "string", help: { a: "CORE_FACT" } },
            { field: "ebeln", label: "采购订单号", type: "string" },
            { field: "matnr", label: "物料", type: "string", help: { a: "Z000_MDM_MTL" } },
            { field: "dateRange", label: "采购凭证日期", type: "date", whereOpt: "BT" }
          ]
        },
        tableParams: {
          layout: {
            report: "Z000_MM_STORE_SEARCH_01B",
            showSummary: false,
            merge: "0",
            height: "calc(100vh - 220px)"
          },
          toolbar: { btns: [] },
          fieldCat: [
            { field: "ebeln", label: "采购凭证号", minWidth: 120 },
            { field: "ebelp", label: "项目编号", minWidth: 100 },
            { field: "matnr", label: "物料编号", minWidth: 120 },
            { field: "matnrT", label: "物料短文本", minWidth: 180 },
            { field: "bukrs", label: "公司代码", minWidth: 100 },
            { field: "lifnr", label: "供应商", minWidth: 100 },
            { field: "bsart", label: "采购凭证类型", minWidth: 120 },
            { field: "zterm", label: "付款条件", minWidth: 100 },
            { field: "ekorg", label: "采购组织", minWidth: 100 },
            { field: "ekgrp", label: "采购组", minWidth: 100 },
            { field: "waers", label: "货币码", minWidth: 80 },
            { field: "bedat", label: "采购凭证日期", minWidth: 120 },
            { field: "werks", label: "工厂", minWidth: 100 },
            { field: "werksT", label: "工厂名称", minWidth: 150 },
            { field: "lgort", label: "库存地点", minWidth: 100 },
            { field: "matkl", label: "物料组", minWidth: 100 },
            { field: "menge", label: "采购订单数量", minWidth: 120, type: "number", numDec: 3 },
            { field: "meins", label: "单位", minWidth: 80 },
            { field: "mwskz", label: "税码", minWidth: 100 },
            { field: "mwskzV", label: "税率", minWidth: 80, type: "number", numDec: 2 },
            { field: "knumv", label: "单据条件数", minWidth: 110 },
            { field: "kalsm", label: "定价过程", minWidth: 110 },
            { field: "wrbtrJe", label: "金额(JE)", minWidth: 120, type: "number", numDec: 2 },
            { field: "wrbtrHs", label: "金额(HS)", minWidth: 120, type: "number", numDec: 2 },
            { field: "wrbtrSe", label: "金额(SE)", minWidth: 120, type: "number", numDec: 2 },
            { field: "wrbtrDj", label: "金额(DJ)", minWidth: 120, type: "number", numDec: 2 },
            { field: "mmenge", label: "入库数量", minWidth: 110, type: "number", numDec: 3 },
            { field: "mwrbtrje", label: "入库金额(JE)", minWidth: 130, type: "number", numDec: 2 },
            { field: "mwrbtrhs", label: "入库金额(HS)", minWidth: 130, type: "number", numDec: 2 },
            { field: "mwrbtrse", label: "入库金额(SE)", minWidth: 130, type: "number", numDec: 2 },
            { field: "fmenge", label: "发票数量", minWidth: 110, type: "number", numDec: 3 },
            { field: "fwrbtrje", label: "发票金额(JE)", minWidth: 130, type: "number", numDec: 2 },
            { field: "fwrbtrhs", label: "发票金额(HS)", minWidth: 130, type: "number", numDec: 2 },
            { field: "fwrbtrse", label: "发票金额(SE)", minWidth: 130, type: "number", numDec: 2 }
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
        if (ranges.dateRange && ranges.dateRange[0]) {
          const dr = ranges.dateRange[0];
          ranges.bdate = [{ type: "date", option: "BT", low: dr.low || "" }];
          ranges.edate = [{ type: "date", option: "EQ", low: dr.high || "" }];
          delete ranges.dateRange;
        }
        submitParams.pageNum = this.tableParams.page.pageNum;
        submitParams.pageSize = this.tableParams.page.pageSize;
        queryStoreSearch(submitParams).then(response => {
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
          let response = await queryStoreSearch(_query);
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
