<template>
  <div class="app-container flex-y">
    <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSeniorSearchEventImp" />
    <szd-table
      :layout="tableParams.layout"
      :toolbar="tableParams.toolbar"
      :fieldCat="tableParams.fieldCat"
      v-model:data="dataList"
      v-model:page="tableParams.page"
      @handleRequest="handleRequestImp"
      @handleEvent="handleEventImp"
      @handleRowEvent="handleRowEventImp"
      @selection-change="handleSelRow"
      :callMethod="handleCallMethod"
      row-key="bussId"
      border
      stripe />
  </div>
</template>

<script>
  import { listApply } from "../api/saleInv.js";

  export default {
    name: "Z000_SD_XSKP_01B",
    data() {
      return {
        routerCodeDg: "Z000_SD_XSKP_01A",
        queryParams: { ranges: {}, orders: [] },
        schLay: {
          layout: { report: "Z000_SD_XSKP_01B", span: 8, more: 3 },
          schCat: [
            { field: "bussDocId", label: "业务单号", type: "string" },
            { field: "bussDate", label: "业务日期", type: "date" },
            { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } },
            { field: "cmpy", label: "公司代码", type: "string", help: { a: "CORE_CMPY" } },
            { field: "cstc", label: "部门", type: "string", help: { a: "CORE_CSTC" } },
            { field: "createTime", label: "创建日期", type: "date" }
          ]
        },
        tableParams: {
          layout: { report: "Z000_SD_XSKP_01B", merge: "0", selection: "0", optionCell: "optionCell", sort: "2" },
          toolbar: {
            btns: [{ fCode: "add", label: "新增", icon: "Plus" }]
          },
          fieldCat: [
            { field: "bussDocId", label: "业务单号", minWidth: 150, sort: true },
            { field: "bussDate", label: "业务日期", minWidth: 100, type: "date", sort: true },
            { field: "bussStatusName", label: "单据状态", minWidth: 100 },
            { field: "cmpyName", label: "公司", minWidth: 150 },
            { field: "cstcName", label: "部门", minWidth: 150 },
            { field: "title", label: "业务标题", minWidth: 200 },
            { field: "createByName", label: "创建人", minWidth: 120 },
            { field: "createTime", label: "创建时间", minWidth: 150, type: "datetime", sort: true },
            { field: "updateByName", label: "更新人", minWidth: 120 },
            { field: "updateTime", label: "更新时间", minWidth: 150, type: "datetime", sort: true },
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
              width: 170
            }
          ],
          page: { total: 0, pageNum: 1, pageSize: 20 }
        },
        dataList: [],
        listSel: []
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
      handleEventImp(obj) {
        switch (obj.fCode) {
          case "add":
            this.handleAdd();
            break;
        }
      },
      // 表格请求回调
      handleRequestImp(obj) {
        this.getList(obj.sort);
      },
      // 行回调
      handleRowEventImp(obj) {
        switch (obj.fCode) {
          case "view":
            this.handleRow(obj.row, "V");
            break;
          case "edit":
            this.handleRow(obj.row, "U");
            break;
        }
      },
      getList(sort) {
        this.queryParams.pageNum = this.tableParams.page.pageNum;
        this.queryParams.pageSize = this.tableParams.page.pageSize;
        this.queryParams.orders = sort && sort.length > 0 ? sort : [];
        listApply(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.tableParams.page.total = response.total;
        });
      },
      handleSelRow(selection) {
        this.listSel = selection;
      },
      handleAdd() {
        this.szdWf.goNav(this.routerCodeDg);
      },
      handleRow(row, option) {
        this.szdWf.goDoc(row.routerCode, row.bussId, option);
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
