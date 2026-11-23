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
      row-key="mtlCode"
      border
      stripe />

    <!-- 同步弹窗 -->
    <el-dialog title="同步SAP物料主数据" v-model="syncDialogVisible" width="400px" :close-on-click-modal="false">
      <el-form label-width="100px">
        <el-form-item label="开始日期">
          <szd-date-form-item-col v-model="syncForm.bdate" type="date" placeholder="请选择开始日期" :span="24" />
        </el-form-item>
        <el-form-item label="结束日期">
          <szd-date-form-item-col v-model="syncForm.edate" type="date" placeholder="请选择结束日期" :span="24" />
        </el-form-item>
        <el-form-item label="物料编码">
          <szd-input v-model="syncForm.matnr" showHelp :help="{ a: 'Z000_MDM_MTL' }" szdopt="U" placeholder="请输入物料编码" />
        </el-form-item>
        <el-form-item label="物料类型">
          <szd-select v-model="syncForm.mtart" baseField="Z000_MDM_MTL_TYPE" szdopt="U" clearable placeholder="请选择物料类型" />
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
  import { listApply, syncMtl } from "../api/mtl.js";

  export default {
    name: "Z000_MDM_MTL_01B",
    data() {
      return {
        queryParams: {
          ranges: {},
          orders: []
        },
        schLay: {
          layout: { report: "Z000_MDM_MTL_01B", span: 8, more: 3 },
          schCat: [
            { field: "mtlCode", label: "物料编码", type: "string", whereOpt: "SP" },
            { field: "mtlName", label: "物料名称", type: "string", whereOpt: "SP" },
            { field: "mtlType", label: "物料类型", type: "string" },
            { field: "mtlGroup", label: "物料组", type: "string" }
          ]
        },
        tableParams: {
          layout: { report: "Z000_MDM_MTL_01B", merge: "0", selection: "0", sort: "2" },
          toolbar: {
            btns: [{ fCode: "sync", label: "同步", icon: "Refresh" }]
          },
          fieldCat: [
            { field: "mtlCode", label: "物料编码", minWidth: 150, sort: true },
            { field: "mtlName", label: "物料名称", minWidth: 250 },
            { field: "unitName", label: "基本单位", minWidth: 100 },
            { field: "mtlType", label: "物料类型", minWidth: 120 },
            { field: "prodGroupName", label: "产品组", minWidth: 120 },
            { field: "mtlGroup", label: "物料组", minWidth: 120 }
          ],
          page: {
            total: 0,
            pageNum: 1,
            pageSize: 20
          }
        },
        dataList: [],
        syncDialogVisible: false,
        syncLoading: false,
        syncForm: {
          bdate: "",
          edate: "",
          matnr: "",
          mtart: ""
        }
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
            this.syncForm = { bdate: "", edate: "", matnr: "", mtart: "" };
            this.syncDialogVisible = true;
            break;
        }
      },
      // 表格请求回调
      handleRequestImp(obj) {
        this.getList(obj.sort);
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
      handleSync() {
        this.szdPop("确定要同步SAP物料主数据吗？同步可能需要一些时间。", true)
          .then(() => {
            this.syncLoading = true;
            syncMtl({ bdate: this.syncForm.bdate, edate: this.syncForm.edate, matnr: this.syncForm.matnr, mtart: this.syncForm.mtart })
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
