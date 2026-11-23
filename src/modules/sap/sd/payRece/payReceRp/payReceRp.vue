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
      @handleRowEvent="handleRowEventImp"
      :callMethod="handleCallMethod"
      row-key="receiptId"
      border
      stripe />
  </div>
</template>

<script>
  import { listApply } from "../api/payRece.js";

  export default {
    name: "Z000_SD_PAY_RECE_01B",
    data() {
      return {
        routerCodeDg: "Z000_SD_PAY_RECE_01A",
        queryParams: { ranges: {}, orders: [] },
        schLay: {
          layout: { report: "Z000_SD_PAY_RECE_01B", span: 8, more: 3 },
          schCat: [
            { field: "bussDate", label: "回款日期", type: "date" },
            { field: "cust", label: "客户", type: "string", help: { a: "CORE_CUST" } },
            { field: "bussDocId", label: "业务单号", type: "string" },
            { field: "userId", label: "申请人", type: "string", help: { a: "CORE_USER" } },
            { field: "prfc", label: "利润中心", type: "string", help: { a: "CORE_PRFC" } },
            { field: "payNature", label: "回款性质", type: "string", help: { a: "Z000_PAY_NATURE" } },
            { field: "payWay", label: "支付方式", type: "string", help: { a: "Z000_PAY_WAY" } },
            { field: "cmpy", label: "公司代码", type: "string", help: { a: "CORE_CMPY" } },
            { field: "curr", label: "货币", type: "string", help: { a: "CORE_CURR" } },
            { field: "amtReceipt", label: "认款金额", type: "number" },
            { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } },
            { field: "bankDoc", label: "回单号", type: "string" }
          ]
        },
        tableParams: {
          layout: { report: "Z000_SD_PAY_RECE_01B", showSummary: true, merge: "0", optionCell: "optionCell" },
          toolbar: {},
          fieldCat: [
            { field: "bussDocId", label: "业务单号", minWidth: 150 },
            { field: "bussDate", label: "回款日期", minWidth: 100, type: "date" },
            { field: "custName", label: "客户", minWidth: 200 },
            { field: "amtReceipt", label: "认款金额", minWidth: 130, type: "number", numDec: 2, doSum: "1" },
            { field: "curr", label: "币种", minWidth: 80 },
            { field: "prfcName", label: "利润中心", minWidth: 150 },
            { field: "payNatureName", label: "回款性质", minWidth: 120 },
            { field: "payWayName", label: "支付方式", minWidth: 100 },
            { field: "bankDoc", label: "回单单号", minWidth: 150 },
            { field: "vchNum", label: "凭证号", minWidth: 150 },
            { field: "vchYear", label: "凭证财年", minWidth: 100 },
            { field: "vchNumReverse", label: "冲销凭证号", minWidth: 150 },
            { field: "vchYearReverse", label: "冲销凭证财年", minWidth: 100 },
            { field: "sapMsg", label: "SAP返回消息", minWidth: 200 },
            { field: "title", label: "业务标题", minWidth: 150 },
            { field: "bussStatusName", label: "单据状态", minWidth: 100 },
            { field: "userName", label: "申请人", minWidth: 100 },
            { field: "createTime", label: "制单时间", minWidth: 150, type: "datetime" },
            {
              field: "operation",
              label: "操作",
              type: "button",
              fixed: "right",
              align: "center",
              ctrlParam: [
                { fCode: "view", label: "显示", icon: "View", type: "link" },
                { fCode: "edit", label: "修改", icon: "Edit", type: "link" }
              ],
              width: 150
            }
          ],
          page: { total: 0, pageNum: 1, pageSize: 20 }
        },
        dataList: []
      };
    },
    created() { this.getList(); },
    methods: {
      handleSeniorSearchEventImp(obj) {
        switch (obj) {
          case "$search": this.getList(); break;
        }
      },
      handleRequestImp(obj) { this.getList(); },
      handleRowEventImp(obj) {
        switch (obj.fCode) {
          case "view": this.handleRow(obj.row, "V"); break;
          case "edit": this.handleRow(obj.row, "U"); break;
        }
      },
      getList() {
        this.queryParams.pageNum = this.tableParams.page.pageNum;
        this.queryParams.pageSize = this.tableParams.page.pageSize;
        listApply(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.tableParams.page.total = response.total;
        });
      },
      handleRow(row, option) {
        this.szdWf.goDoc(row.routerCode, row.receiptId, option);
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
          let response = await listApply(_query);
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
