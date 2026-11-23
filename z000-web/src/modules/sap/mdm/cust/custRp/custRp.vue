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
      @selection-change="handleSelRow"
      :callMethod="handleCallMethod"
      row-key="bussId"
      border
      stripe />
  </div>
</template>

<script>
  import { listApply } from "../api/cust.js";

  export default {
    name: "Z000_MDM_CUST_01B",
    data() {
      return {
        routerCodeDg: "Z000_MDM_CUST_01A",
        queryParams: {
          ranges: {},
          orders: []
        },
        schLay: {
          layout: { report: "Z000_MDM_CUST_01B", span: 8, more: 3 },
          schCat: [
            { field: "bussDocId", label: "业务单号", type: "string" },
            { field: "bussDate", label: "业务日期", type: "date" },
            { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } },
            { field: "custId", label: "客商编码", type: "string" },
            { field: "custName", label: "客商名称", type: "string" },
            {
              field: "custGrp",
              label: "客商分组",
              type: "string",
              selectOpt: true,
              help: { a: "Z000_MDM_CUST_BUSS_GROUP" }
            },
            {
              field: "partnerType",
              label: "伙伴类型",
              type: "string",
              selectOpt: true,
              help: { a: "Z000_MDM_CUST_BUSS_TYPE" }
            },
            // { field: "u8Code", label: "U8旧编码", type: "string" },
            { field: "riskClass", label: "风险类", type: "string", selectOpt: true, help: { a: "Z000_MDM_CUST_RISK" } },
            { field: "province", label: "省", type: "string", help: { a: "CORE_PROV", c: ["provSap"] } },
            { field: "city", label: "市", type: "string", help: { a: "CORE_CITY", c: ["cityId"] } },
            { field: "createTime", label: "创建日期", type: "date" }
          ]
        },
        tableParams: {
          layout: { report: "Z000_MDM_CUST_01B", merge: "0", selection: "0", optionCell: "optionCell", sort: "2" },
          toolbar: {
            btns: [{ fCode: "add", label: "新增", icon: "Plus" }]
          },
          fieldCat: [
            { field: "bussDocId", label: "业务单号", minWidth: 150, sort: true },
            { field: "bussDate", label: "业务日期", minWidth: 100, type: "date", sort: true },
            { field: "bussStatusName", label: "单据状态", minWidth: 100 },
            { field: "custId", label: "客商编码", minWidth: 100, sort: true },
            { field: "custName", label: "客商名称", minWidth: 250 },
            { field: "custGrpName", label: "客商分组", minWidth: 150 },
            { field: "partnerTypeName", label: "伙伴类型", minWidth: 150 },
            // { field: "u8Code", label: "U8旧编码", minWidth: 100 },
            { field: "ctryName", label: "国家", minWidth: 100 },
            { field: "provinceName", label: "省份", minWidth: 120 },
            { field: "cityName", label: "城市", minWidth: 120 },
            { field: "address", label: "地址", minWidth: 350 },
            { field: "contact", label: "联系人", minWidth: 120 },
            { field: "phone", label: "联系电话", minWidth: 100 },
            { field: "idNum", label: "税号/身份证", minWidth: 180, sort: true },
            { field: "riskClassName", label: "风险类", minWidth: 100 },
            { field: "title", label: "业务标题", minWidth: 200 },
            { field: "remark", label: "备注", minWidth: 200 },
            { field: "createByName", label: "创建人", minWidth: 150 },
            { field: "createTime", label: "创建时间", minWidth: 150, type: "datetime", sort: true },
            { field: "updateByName", label: "更新人", minWidth: 150 },
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
          // 分页
          page: {
            total: 0,
            pageNum: 1,
            pageSize: 20
          }
        },
        //数据列表
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
      //查询列表
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
      //新增
      handleAdd() {
        this.szdWf.goNav(this.routerCodeDg);
      },
      //修改
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
