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
      row-key="orderNo"
      border
      stripe />

    <el-dialog title="抽取SAP生产订单" v-model="syncDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form class="app-el-form">
        <el-row>
          <szd-input-form-item-col
            label="公司"
            v-model="syncForm.bukrs"
            :span="24"
            required
            showHelp
            :help="{ a: 'CORE_CMPY', b: syncForm, c: ['bukrs-cmpy', 'bukrsName-cmpyName'] }"
            :desc="syncForm.bukrsName"
            :descSpan="12" />
          <szd-input-form-item-col
            label="工厂"
            v-model="syncForm.werks"
            :span="24"
            required
            showHelp
            :help="{ a: 'CORE_FACT', b: syncForm, c: ['werks-fact', 'werksName-factName'] }"
            :desc="syncForm.werksName"
            :descSpan="12" />
          <szd-select-form-item-col
            label="订单类型"
            v-model="syncForm.auart"
            :span="24"
            baseField="Z000_PP_SCDDCJ_ORDER_TYPE" />
          <szd-date-form-item-col label="创建/修改开始日期" v-model="syncForm.bdate" type="date" :span="24" />
          <szd-date-form-item-col label="创建/修改结束日期" v-model="syncForm.edate" type="date" :span="24" />
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="syncDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSync" :loading="syncLoading">开始抽取</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { listApply, syncApply } from "../api/orderExtract.js";

  export default {
    name: "Z000_PP_SCDDCQ_01B",
    data() {
      return {
        routerCodeDg: "Z000_PP_SCDDCQ_01A",
        queryParams: { ranges: {}, orders: [] },
        schLay: {
          layout: { report: "Z000_PP_SCDDCQ_01B", span: 8, more: 3, optionCell: "optionCell" },
          schCat: [
            { field: "orderNo", label: "订单号", type: "string" },
            { field: "mtlCode", label: "物料编码", type: "string" },
            { field: "fact", label: "工厂", type: "string" },
            { field: "prodVer", label: "生产版本", type: "string" }
          ]
        },
        tableParams: {
          layout: { report: "Z000_PP_SCDDCQ_01B", merge: "0", selection: "0", optionCell: "optionCell", sort: "2" },
          toolbar: { btns: [{ fCode: "sync", label: "抽取", icon: "Refresh" }] },
          fieldCat: [
            { field: "orderNo", label: "订单号", minWidth: 130, sort: true },
            { field: "orderType", label: "订单类型", minWidth: 100 },
            { field: "mtlCode", label: "物料编码", minWidth: 150 },
            { field: "fact", label: "工厂", minWidth: 100 },
            { field: "orderDesc", label: "订单描述", minWidth: 200 },
            { field: "prodVer", label: "生产版本", minWidth: 120 },
            { field: "unit", label: "单位", minWidth: 80 },
            { field: "orderQty", label: "订单数量", minWidth: 120 },
            { field: "shippedQty", label: "已发货数量", minWidth: 100 },
            { field: "receivedQty", label: "已收货数量", minWidth: 100 },
            { field: "orderStatus", label: "订单状态", minWidth: 100 },
            { field: "startDate", label: "订单开始日期", minWidth: 110 },
            { field: "finishDate", label: "基本完成日期", minWidth: 110 },
            {
              field: "operation",
              label: "操作",
              type: "button",
              fixed: "right",
              align: "center",
              ctrlParam: [{ fCode: "view", label: "显示", icon: "View", type: "link" }],
              width: 100
            }
          ],
          page: { total: 0, pageNum: 1, pageSize: 20 }
        },
        dataList: [],
        syncDialogVisible: false,
        syncLoading: false,
        syncForm: { bdate: "", edate: "", bukrs: "", bukrsName: "", auart: "", werks: "", werksName: "" }
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
          case "sync":
            this.syncForm = { bdate: "", edate: "", bukrs: "", bukrsName: "", auart: "", werks: "", werksName: "" };
            this.syncDialogVisible = true;
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
        }
      },
      //修改
      handleRow(row) {
        this.szdRouter.goNav(this.routerCodeDg, {
          orderNo: row.orderNo
        });
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
      },
      // 抽取
      handleSync() {
        if (!this.syncForm.bukrs) {
          this.$mW("请选择公司");
          return;
        }
        if (!this.syncForm.werks) {
          this.$mW("请选择工厂");
          return;
        }
        this.szdPop("确定要抽取SAP生产订单吗？抽取可能需要一些时间。", true)
          .then(() => {
            this.syncLoading = true;
            syncApply(this.syncForm)
              .then(response => {
                this.syncLoading = false;
                if (response.code === 200 && response.data.status === "S") {
                  this.$mS(response.data.msg || "抽取完成");
                  this.syncDialogVisible = false;
                  this.getList();
                } else {
                  this.$mE(response.data.msg || response.msg || "抽取失败");
                }
              })
              .catch(() => {
                this.syncLoading = false;
                this.$mE("抽取失败");
              });
          })
          .catch(() => {});
      }
    }
  };
</script>
