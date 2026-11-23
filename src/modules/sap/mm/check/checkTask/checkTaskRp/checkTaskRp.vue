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
      :callMethod="handleCallMethod"
      row-key="taskId"
      border
      stripe />
  </div>
</template>

<script>
import { listApply } from "../api/checkTask.js";

export default {
  name: "Z000_MM_KCPD_02B",
  data() {
    return {
      routerCodeDg: "Z000_MM_KCPD_02A",
      queryParams: {
        ranges: {},
        orders: []
      },
      schLay: {
        layout: { report: "Z000_MM_KCPD_02B", span: 8, more: 3 },
        schCat: [
          { field: "bussDocId", label: "任务单号", type: "string" },
          { field: "planId", label: "计划单号", type: "string" },
          { field: "fact", label: "工厂", type: "string", help: { a: "CORE_FACT" } },
          { field: "store", label: "库位", type: "string", help: { a: "CORE_STOR" } },
          { field: "taskStatus", label: "任务状态", type: "string", help: { a: "Z000_KCPD_TASK_STATUS" } },
          { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } },
          { field: "createTime", label: "创建日期", type: "date" }
        ]
      },
      tableParams: {
        layout: { report: "Z000_MM_KCPD_02B", merge: "0", selection: "0", optionCell: "optionCell", sort: "2" },
        toolbar: {},
        fieldCat: [
          { field: "bussDocId", label: "任务单号", minWidth: 150, sort: true },
          { field: "factName", label: "工厂", minWidth: 120 },
          { field: "storeName", label: "库位", minWidth: 120 },
          { field: "taskStatusName", label: "任务状态", minWidth: 100, type: "dict", help: { a: "Z000_KCPD_TASK_STATUS" } },
          { field: "bussStatusName", label: "单据状态", minWidth: 100 },
          { field: "createByName", label: "创建人", minWidth: 120 },
          { field: "createTime", label: "创建时间", minWidth: 150, type: "datetime", sort: true },
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
    handleEventImp(obj) {},
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
    handleRow(row, option) {
      this.szdWf.goDoc(row.routerCode, row.taskId, option);
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
