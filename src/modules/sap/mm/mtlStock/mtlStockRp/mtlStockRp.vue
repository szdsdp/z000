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
      @handleEvent="handleEventImp"
      :callMethod="handleCallMethod"
      row-key="matnr"
      border
      stripe />

    <!-- 同步弹窗 -->
    <el-dialog title="同步SAP库存数据" v-model="syncDialogVisible" width="400px" :close-on-click-modal="false">
      <el-form label-width="50px">
        <el-form-item label="工厂" required>
          <szd-input-form-item-col
            v-model="syncForm.werks"
            :span="24"
            required
            clearable
            showHelp
            :help="{ a: 'CORE_FACT', b: syncForm, c: ['werks-fact', 'werksName-factName'] }"
            :desc="syncForm.werksName" />
        </el-form-item>
        <el-form-item label="库位">
          <szd-input-form-item-col
            v-model="syncForm.lgort"
            :span="24"
            showHelp
            clearable
            :help="{ a: 'CORE_STOR', b: syncForm, c: ['lgort-stor', 'lgortName-storName'], d: [['fact', syncForm.werks]] }"
            :desc="syncForm.lgortName" />
        </el-form-item>
        <el-form-item label="物料">
          <szd-input-form-item-col
            v-model="syncForm.matnr"
            :span="24"
            clearable
            showHelp
            :help="{ a: 'Z000_MDM_MTL', b: syncForm, c: ['matnr-mtlCode', 'matnrName-mtlName'] }"
            :desc="syncForm.matnrName" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="syncDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSync" :loading="syncLoading">开始同步</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { queryStock, syncStock } from "../api/mtlStock.js";

  export default {
    name: "Z000_MM_KCCX_01B",
    data() {
      return {
        syncDialogVisible: false,
        syncLoading: false,
        syncForm: {
          werks: "",
          werksName: "",
          lgort: "",
          lgortName: "",
          matnr: "",
          matnrName: ""
        },
        queryParams: {
          ranges: {},
          orders: []
        },
        schLay: {
          layout: { report: "Z000_MM_KCCX_01B", span: 8, more: 3 },
          schCat: [
            { field: "matnr", label: "物料编码", type: "string", help: { a: "SASM_MDM_MTL_BASE" } },
            { field: "maktx", label: "物料描述", type: "string" },
            { field: "werks", label: "工厂", type: "string", help: { a: "CORE_FACT" } },
            { field: "lgort", label: "库存地点", type: "string", help: { a: "CORE_STOR", c: "stor" } }
          ]
        },
        tableParams: {
          layout: {
            report: "Z000_MM_KCCX_01B",
            showSummary: false,
            merge: "0",
            height: "calc(100vh - 220px)"
          },
          toolbar: {
            btns: [{ fCode: "sync", label: "同步", icon: "Refresh" }]
          },
          fieldCat: [
            { field: "matnr", label: "物料编号", minWidth: 100 },
            { field: "maktx", label: "物料描述", minWidth: 100 },
            { field: "unitName", label: "单位", minWidth: 80 },
            { field: "werksName", label: "工厂", minWidth: 80 },
            { field: "lgortName", label: "库存地点", minWidth: 100 },
            {
              field: "labst",
              label: "非限制库存",
              minWidth: 100,
              type: "number",
              numDec: 3
            },
            {
              field: "speme",
              label: "冻结的库存",
              minWidth: 100,
              type: "number",
              numDec: 3
            }
          ],
          page: {
            total: 0,
            pageNum: 1,
            pageSize: 50
          },
          oper: {}
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
          case "$reset":
            this.handleReset();
            break;
        }
      },

      // 重置查询条件
      handleReset() {
        this.queryParams.ranges = {};
        this.dataList = [];
        this.tableParams.page.total = 0;
      },

      // 查询列表
      getList() {
        this.queryParams.pageNum = this.tableParams.page.pageNum;
        this.queryParams.pageSize = this.tableParams.page.pageSize;
        queryStock(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.tableParams.page.total = response.total;
        });
      },

      // 表格请求回调
      handleRequestImp(obj) {
        this.getList();
      },
      // 表格按钮回调
      handleEventImp(obj) {
        switch (obj.fCode) {
          case "sync":
            this.syncForm = { werks: "", werksName: "", lgort: "", lgortName: "", matnr: "", matnrName: "" };
            this.syncDialogVisible = true;
            break;
        }
      },
      handleSync() {
        if (!this.syncForm.werks) {
          this.$mW("工厂不能为空");
          return;
        }
        this.szdPop("确定要同步SAP库存数据吗？", true)
          .then(() => {
            this.syncLoading = true;
            syncStock({ matnr: this.syncForm.matnr, werks: this.syncForm.werks, lgort: this.syncForm.lgort })
              .then(response => {
                this.syncLoading = false;
                if (response.code === 200 && response.data.status === "S") {
                  this.$mS(response.data.msg || "同步完成");
                  this.syncDialogVisible = false;
                  this.getList();
                } else {
                  this.$mE(response.data.msg || response.msg || "同步失败");
                }
              })
              .catch(() => {
                this.syncLoading = false;
                this.$mE("同步失败");
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
          let response = await queryStock(_query);
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

<style scoped></style>
