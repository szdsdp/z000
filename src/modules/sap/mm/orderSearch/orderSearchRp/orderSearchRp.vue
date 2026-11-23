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
  import { queryOrderSearch } from "../api/orderSearch.js";

  export default {
    name: "Z000_MM_ORDER_SEARCH_01B",
    data() {
      return {
        queryParams: { ranges: {}, orders: [] },
        schLay: {
          layout: { report: "Z000_MM_ORDER_SEARCH_01B", span: 8, more: 3 },
          schCat: [
            { field: "bukrs", label: "公司", type: "string", required: true, help: { a: "CORE_CMPY" } },
            { field: "lifnr", label: "供应商", type: "string", required: true, help: { a: "CORE_SUPP" } },
            { field: "ebeln", label: "采购订单", type: "string" },
            { field: "bsart", label: "采购订单类型", type: "string" },
            { field: "ekorg", label: "采购组织", type: "string", help: { a: "Z000_MDM_ORG_PUR" } },
            { field: "ekgrp", label: "采购组", type: "string", help: { a: "Z000_MM_CGDD_CGZ" } }
          ]
        },
        tableParams: {
          layout: {
            report: "Z000_MM_ORDER_SEARCH_01B",
            showSummary: false,
            merge: "0",
            height: "calc(100vh - 220px)"
          },
          toolbar: { btns: [] },
          fieldCat: [
            { field: "ebeln", label: "采购凭证号", minWidth: 120 },
            { field: "ebelp", label: "项目编号", minWidth: 100 },
            { field: "matnr", label: "物料编号", minWidth: 120 },
            { field: "txz01", label: "物料名称", minWidth: 180 },
            { field: "bukrs", label: "公司代码", minWidth: 100 },
            { field: "lifnr", label: "供应商", minWidth: 100 },
            { field: "bsart", label: "采购凭证类型", minWidth: 120 },
            { field: "zterm", label: "付款条件", minWidth: 100 },
            { field: "ekorg", label: "采购组织", minWidth: 100 },
            { field: "ekgrp", label: "采购组", minWidth: 100 },
            { field: "aedat", label: "更改日期", minWidth: 120 },
            { field: "ernam", label: "创建人", minWidth: 120 },
            { field: "werks", label: "工厂", minWidth: 100 },
            { field: "matkl", label: "物料组", minWidth: 100 },
            { field: "menge", label: "采购订单数量", minWidth: 120, type: "number", numDec: 3 },
            { field: "meins", label: "计量单位", minWidth: 100 },
            { field: "zshsl", label: "已收货数量", minWidth: 110, type: "number", numDec: 3 }
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
        submitParams.pageNum = this.tableParams.page.pageNum;
        submitParams.pageSize = this.tableParams.page.pageSize;
        queryOrderSearch(submitParams).then(response => {
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
          let response = await queryOrderSearch(_query);
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
