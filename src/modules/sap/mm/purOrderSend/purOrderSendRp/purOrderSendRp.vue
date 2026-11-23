<template>
  <div class="app-container flex-y">
    <!-- 查询条件 -->
    <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSeniorSearchEventImp" />
    <!-- 表单输出 -->
    <szd-table
      :layout="tableParams.layout"
      :toolbar="tableParams.toolbar"
      :fieldCat="tableParams.fieldCat"
      v-model:data="dataList"
      v-model:page="tableParams.page"
      @handleRequest="handleRequestImp"
      @handleEvent="handleEventImp"
      :callMethod="handleCallMethod"
      border
      stripe />
  </div>
</template>

<script>
  import { listSend } from "../api/purOrderSend.js";

  export default {
    name: "Z000_MM_CGDDTS_01B",
    data() {
      return {
        queryParams: {
          ranges: {},
          orders: []
        },
        schLay: {
          layout: { report: "Z000_MM_CGDDTS_01B", span: 8, more: 3 },
          schCat: [
            { field: "ebeln", label: "采购凭证号", type: "string" },
            { field: "lifnr", label: "供应商帐号", type: "string" },
            { field: "name1", label: "供应商名称", type: "string" },
            { field: "mtlCode", label: "物料编码", type: "string" },
            { field: "fact", label: "工厂", type: "string" },
            { field: "bukrs", label: "公司代码", type: "string" },
            { field: "bedat", label: "采购订单日期", type: "date" },
            { field: "aedat", label: "创建日期", type: "date" }
          ]
        },
        tableParams: {
          layout: { report: "Z000_MM_CGDDTS_01B", merge: "0", selection: "0", sort: "2" },
          toolbar: {},
          fieldCat: [
            { field: "ebeln", label: "采购凭证号", minWidth: 120, sort: true },
            { field: "ebelp", label: "行项目编号", minWidth: 100 },
            { field: "txz01", label: "短文本", minWidth: 200 },
            { field: "mtlCode", label: "物料编码", minWidth: 130 },
            { field: "maktx", label: "物料描述", minWidth: 200 },
            { field: "menge", label: "数量", minWidth: 100 },
            { field: "meins", label: "单位", minWidth: 70 },
            { field: "fact", label: "工厂", minWidth: 80 },
            { field: "lifnr", label: "供应商帐号", minWidth: 120 },
            { field: "name1", label: "供应商名称", minWidth: 180 },
            { field: "bukrs", label: "公司代码", minWidth: 100 },
            { field: "ekorg", label: "采购组织", minWidth: 100 },
            { field: "ekgrp", label: "采购组", minWidth: 80 },
            { field: "waers", label: "货币码", minWidth: 70 },
            { field: "bsart", label: "订单类型", minWidth: 100 },
            { field: "statu", label: "凭证状态", minWidth: 80 },
            { field: "bedat", label: "采购订单日期", minWidth: 120, type: "date", sort: true },
            { field: "aedat", label: "创建日期", minWidth: 110, type: "date", sort: true },
            { field: "ernam", label: "创建人", minWidth: 120 },
            { field: "createTime", label: "接收时间", minWidth: 150, type: "datetime", sort: true }
          ],
          page: {
            total: 0,
            pageNum: 1,
            pageSize: 20
          }
        },
        dataList: []
      };
    },
    created() {
      this.getList();
    },
    methods: {
      // 高级查询按钮回调
      handleSeniorSearchEventImp(obj) {
        switch (obj) {
          case "$search":
            this.getList();
            break;
        }
      },
      // 表格按钮回调
      handleEventImp() {},
      // 表格请求回调
      handleRequestImp(obj) {
        this.getList(obj.sort);
      },
      getList(sort) {
        this.queryParams.pageNum = this.tableParams.page.pageNum;
        this.queryParams.pageSize = this.tableParams.page.pageSize;
        this.queryParams.orders = sort && sort.length > 0 ? sort : [];
        listSend(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.tableParams.page.total = response.total;
        });
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
          let response = await listSend(_query);
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
