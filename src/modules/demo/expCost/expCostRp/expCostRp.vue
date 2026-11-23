<template>
  <div class="app-container flex-y">
    <!-- 查询条件 -->
    <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSchEventImp" />
    <!-- 表单输出 -->
    <szd-table
      :layout="tabLay.layout"
      :toolbar="tabLay.toolbar"
      :fieldCat="tabLay.fieldCat"
      v-model:data="dataList"
      v-model:page="tabLay.page"
      @handleRequest="handleRequestImp"
      @handleEvent="handleEventImp"
      @handleRowEvent="handleRowEventImp"
      border
      stripe />
  </div>
</template>

<script>
  import { listApply } from "../api/expCost.js";

  export default {
    data() {
      return {
        routerCodeDg: "Z000_DEMO_EXP_01A",
        queryParams: {
          // 查询条件
          ranges: {},
          // 排序条件
          orders:[]
        },
        schLay: {
          layout: { report: "Z000_DEMO_EXP_01B", span: 8, more: 3 },
          schCat: [
            { field: "cmpy", label: "公司", type: "string", help: { a: "CORE_CMPY", c: "cmpy" }, whereOpt: "SP" },
            { field: "cstc", label: "部门", type: "string", help: { a: "CORE_CSTC", c: "cstc" }, whereOpt: "SP" },
            { field: "userId", label: "报账人", type: "string", help: { a: "CORE_USER", c: "userId", whereOpt: "SP" } },
            { field: "bussDate", label: "业务日期", type: "date" },
            { field: "bstp", label: "业务类型", type: "string", help: { a: "CORE_BSTP", c: "bstp" } },
            { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } }
          ]
        },
        tabLay: {
          layout: { report: "Z000_DEMO_EXP_01B", selection: "2", sort: "2"},
          toolbar: {
            btns: [{ fCode: "add", label: "新增", icon: "Plus" }]
          },
          fieldCat: [
            { field: "bussDocId", label: "业务单号", minWidth: 150, sort: true },
            { field: "cmpyName", label: "公司", minWidth: 200 },
            { field: "cstcName", label: "部门", minWidth: 180 },
            { field: "userName", label: "报账人", minWidth: 120 },
            { field: "bstp", label: "业务类型", minWidth: 200 },
            { field: "bussDate", label: "业务日期", minWidth: 100 },
            { field: "amtApply", label: "报账金额", minWidth: 100, type: "number", numDec: 2 },
            { field: "title", label: "业务标题", minWidth: 300 },
            { field: "bussStatus", label: "单据状态", minWidth: 120 },
            { field: "createBy", label: "制单人", minWidth: 120 },
            { field: "createTime", label: "制单时间", minWidth: 130 },
            { field: "updateBy", label: "更改人", minWidth: 120 },
            { field: "updateTime", label: "更改时间", minWidth: 130 },
            { field: "bussId", label: "业务ID", minWidth: 150 },
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
              width: 120
            }
          ],
          // 分页
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
      handleSchEventImp(obj) {
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
            this.handleUpdRow(obj.row, "V");
            break;
          case "edit":
            this.handleUpdRow(obj.row, "U");
            break;
        }
      },
      //查询列表
      getList(sort) {
        this.queryParams.pageNum = this.tabLay.page.pageNum;
        this.queryParams.pageSize = this.tabLay.page.pageSize;
        this.queryParams.orders = sort && sort.length > 0 ? sort : [];
        listApply(this.queryParams).then(response => {
          if (response.code === 200) {
            this.dataList = response.rows;
            this.tabLay.page.total = response.total;
          }
        });
      },
      //新增
      handleAdd() {
        this.szdWf.goNav(this.routerCodeDg);
      },
      //修改
      handleUpdRow(row, option) {
        this.szdWf.goDoc(this.routerCodeDg, row.bussId, option);
      }
    }
  };
</script>
