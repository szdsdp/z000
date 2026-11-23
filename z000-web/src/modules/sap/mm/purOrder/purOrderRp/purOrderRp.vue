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
  import { listApply, createPurStoreFromPurOrderList } from "../api/purOrder.js";

  export default {
    name: "Z000_MM_CGDD_01B",
    data() {
      return {
        routerCodeDg: "Z000_MM_CGDD_01A",
        queryParams: {
          ranges: {},
          orders: []
        },
        schLay: {
          layout: { report: "Z000_MM_CGDD_01B", span: 8, more: 3 },
          schCat: [
            { field: "bussDocId", label: "业务单号", type: "string" },
            { field: "bussDate", label: "业务日期", type: "date" },
            { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } },
            { field: "vchNo", label: "采购凭证号", type: "string" },
            { field: "vchType", label: "采购凭证类型", type: "string", help: { a: "Z000_MM_CGDD_CGPZLX" } },
            { field: "supp", label: "供应商账号", type: "string" },
            { field: "purOrg", label: "采购组织", type: "string", help: { a: "Z000_MM_CGDD_CGZZ" } },
            { field: "purGroup", label: "采购组", type: "string", help: { a: "Z000_MM_CGDD_CGZ" } },
            { field: "curr", label: "货币码", type: "string", help: { a: "CORE_CURR" } },
            { field: "cmpy", label: "公司代码", type: "string", help: { a: "CORE_CMPY" } },
            { field: "cstc", label: "部门", type: "string", help: { a: "CORE_CSTC" } },
            { field: "createTime", label: "创建日期", type: "date" }
          ]
        },
        tableParams: {
          layout: { report: "Z000_MM_CGDD_01B", merge: "0", selection: "1", optionCell: "optionCell", sort: "2" },
          toolbar: {
            btns: [
              { fCode: "add", label: "新增", icon: "Plus" },
              { fCode: "cgrk", label: "物料凭证", icon: "Upload" }
            ]
          },
          fieldCat: [
            { field: "bussDocId", label: "业务单号", minWidth: 150, sort: true },
            { field: "bussDate", label: "业务日期", minWidth: 100, type: "date", sort: true },
            { field: "bussStatusName", label: "单据状态", minWidth: 100 },
            { field: "vchNo", label: "采购凭证号", minWidth: 120, sort: true },
            { field: "vchTypeName", label: "采购凭证类型", minWidth: 120 },
            { field: "supp", label: "供应商", minWidth: 120 },
            { field: "purOrgName", label: "采购组织", minWidth: 120 },
            { field: "purGroupName", label: "采购组", minWidth: 100 },
            { field: "currName", label: "货币", minWidth: 100 },
            { field: "cmpyName", label: "公司代码", minWidth: 150 },
            { field: "cstcName", label: "部门", minWidth: 150 },
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
          page: {
            total: 0,
            pageNum: 1,
            pageSize: 20
          }
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
          case "cgrk":
            this.handleCreateCgrk();
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
      // 物料凭证
      handleCreateCgrk() {
        if (!this.listSel || this.listSel.length === 0) {
          this.$mW("请先勾选采购订单");
          return;
        }
        // 校验单据状态必须为F
        const allFStatus = this.listSel.every(item => item.bussStatus === "F");
        if (!allFStatus) {
          this.$mW("仅可勾选单据状态为【同步完成】的采购订单");
          return;
        }

        // 校验同一供应商
        const firstSupp = this.listSel[0].supp;
        const sameSupplier = this.listSel.every(item => item.supp === firstSupp);
        if (!sameSupplier) {
          this.$mW("所选采购订单供应商不一致，请选择同一供应商的采购订单");
          return;
        }
        const purOrderIds = this.listSel.map(item => item.bussId);
        this.szdPop("确定要基于所选采购订单生成物料凭证单吗？", true)
          .then(() => {
            createPurStoreFromPurOrderList(purOrderIds)
              .then(res => {
                if (res.code === 200 && res.data.status === "S") {
                  this.$mS("生成物料凭证成功");
                  this.szdWf.goDoc("Z000_MM_CGRK_01A", res.data.docId, "U");
                } else {
                  this.$mE(res.msg || "生成物料凭证失败");
                }
              })
              .catch(() => {
                this.$mE("生成物料凭证失败");
              });
          })
          .catch(() => {});
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
