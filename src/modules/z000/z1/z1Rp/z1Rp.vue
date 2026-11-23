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
      row-key="bussId"
      border
      stripe/>
  </div>
</template>

<script>
  import { list } from "../api/z1.js";

  export default {
    name: "Z000_Z1_01B",
    data() {
      return {
        queryParams: {
          routerCode: "Z000_Z1_01A",
          ranges: {
            cmpy: [],
            cstc: [],
            userId: [],
            bussDate: [],
            bstp: [],
            status: []
          }
        },
        schLay: {
          layout: { report: "Z000_Z1_01B", span: 8, more: 3 },
          schCat: [
            { field: "cmpy", label: "公司", type: "string", help: { a: "CORE_CMPY", c: "cmpy" } },
            { field: "cstc", label: "部门", type: "string", help: { a: "CORE_CSTC", c: "cstc" } },
            { field: "userId", label: "报账人", type: "string", help: { a: "CORE_USER", c: "userId" } },
            { field: "bussDate", label: "业务日期", type: "date" },
            { field: "bstp", label: "业务类型", type: "string", help: { a: "CORE_BSTP", c: "bstp" } },
            { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } }
          ]
        },
        tableParams: {
          layout: { report: "Z000_Z1_01B", showSummary: true, merge: "0", selection: "2", optionCell: "optionCell" },
          toolbar: {
            btns: [
              { fCode: "add", label: "新增", icon: "Plus" },
            ]
          },
          fieldCat: [
            { field: "bussDocId", label: "业务单号", minWidth: 150 },
            { field: "cmpyName", label: "公司", minWidth: 200 },
            { field: "cstcName", label: "部门", minWidth: 180 },
            { field: "userId", label: "报账人", minWidth: 120 },
            { field: "bstp", label: "业务类型", minWidth: 200 },
            { field: "bussDate", label: "业务日期", minWidth: 100 },
            { field: "bussStatus", label: "单据状态", minWidth: 120 },
            { field: "createBy", label: "制单人", minWidth: 120 },
            { field: "createTime", label: "制单时间", minWidth: 130 },
            { field: "updateBy", label: "更改人", minWidth: 120 },
            { field: "updateTime", label: "更改时间", minWidth: 130 },
            { field: "bussId", label: "业务ID", minWidth: 150 },
            {
              field: "opration",
              label: "操作",
              type: "button",
              fixed: "right",
              align: "center",
              ctrlParam: [
                { fCode: "view", label: "显示", icon: "View", type: "link" },
                { fCode: "edit", label: "修改", icon: "Edit", type: "link" }
              ],
              width: 170,
            }
          ],
          // 分页
          page: {
            total: 0,
            pageNum: 1,
            pageSize: 20
          }
        },
        dataList: [],
      }
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
        this.getList();
      },
      // 行回调
      handleRowEventImp(obj) {
        switch (obj.fCode) {
          case "view":
            this.handleUpdRow(obj.row, "V");
            break;
          case "edit":
            this.handleUpdRow(obj.row, "U");
            break;
        }
      },
      //查询列表
      getList() {
        this.queryParams.pageNum = this.tableParams.page.pageNum;
        this.queryParams.pageSize = this.tableParams.page.pageSize;
        list(this.queryParams).then(response => {
          if (response.code === 200) {
            this.dataList = response.rows;
            this.tableParams.page.total = response.total;
          }
        });
      },
      //新增
      handleAdd() {
        this.szdWf.goNav(this.queryParams.routerCode);
      },
      //修改
      handleUpdRow(row, option) {
        this.szdWf.goDoc(this.queryParams.routerCode, row.bussId, option);
      },
    }
  };
</script>
