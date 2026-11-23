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
      @selection-change="handleSelRow"
      row-key="accNo"
      border
      stripe />
    <!-- 信息维护 -->
    <bankAccInfo ref="bankAccInfo" @updResult="updResult" />
  </div>
</template>

<script>
  import { listBankAcc, delBankAcc } from "./api/bankAcc.js";
  import bankAccInfo from "./bankAccInfo.vue";

  export default {
    components: {
      bankAccInfo
    },
    data() {
      return {
        //查询参数
        queryParams: {
          ranges: {}
        },
        schLay: {
          layout: { report: "Z000_MDM_BANK_ACC", span: 8, more: 3 },
          schCat: [
            { field: "accNo", label: "银行账户", type: "string", whereOpt: "SP" },
            { field: "accName", label: "账户名称", type: "string", whereOpt: "SP" },
            { field: "cmpy", label: "公司代码", type: "string", help: { a: "CORE_CMPY", c: "cmpy" } },
            { field: "curr", label: "币种", type: "string", help: { a: "CORE_CURR" } }
          ]
        },
        tabLay: {
          layout: { report: "Z000_MDM_BANK_ACC", selection: "2" },
          toolbar: {
            btns: [
              { fCode: "add", label: "新增", icon: "Plus" },
              { fCode: "import", label: "导入", icon: "Upload" },
              { fCode: "del", label: "删除", icon: "Delete" }
            ]
          },
          fieldCat: [
            { field: "accNo", label: "银行账户", minWidth: 150 },
            { field: "accName", label: "账户名称", minWidth: 200 },
            { field: "cmpyName", label: "公司代码", minWidth: 120 },
            { field: "currName", label: "币种", minWidth: 100 },
            { field: "accsName", label: "会计科目", minWidth: 150 },
            { field: "bankCode", label: "联行号", minWidth: 150 },
            {
              field: "operation",
              label: "操作",
              type: "button",
              fixed: "right",
              align: "center",
              ctrlParam: [
                { fCode: "edit", label: "修改", icon: "Edit", type: "link" },
                { fCode: "del", label: "删除", icon: "Delete", type: "link" }
              ],
              width: 150
            }
          ],
          // 分页
          page: { total: 0, pageNum: 1, pageSize: 20 }
        },
        //数据列表
        dataList: [],
        //列表多选
        listSel: []
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
          case "import":
            this.handleImport();
            break;
          case "del":
            this.handleDel();
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
          case "edit":
            this.handleUpdRow(obj.row);
            break;
          case "del":
            this.handleDelRow(obj.row);
            break;
        }
      },
      //查询列表
      getList() {
        this.queryParams.pageNum = this.tabLay.page.pageNum;
        this.queryParams.pageSize = this.tabLay.page.pageSize;
        listBankAcc(this.queryParams).then(response => {
          if (response.code === 200) {
            this.dataList = response.rows;
            this.tabLay.page.total = response.total;
          }
        });
      },
      //新增
      handleAdd() {
        this.$refs.bankAccInfo.callBankAccInfo({ option: "C", accNo: "" });
      },
      //修改
      handleUpdRow(row) {
        this.$refs.bankAccInfo.callBankAccInfo({ option: "U", accNo: row.accNo });
      },
      //导入
      handleImport() {
        this.szdUpload({
          uploadUrl: "/z000/mdm/bankAcc/import",
          TMCode: "Z000_MDM_BANK_ACC"
        }).then(res => {
          this.getList();
        });
      },
      //处理返回结果
      updResult(resultData) {
        if (resultData.retCode === "S") {
          this.getList();
        }
      },
      //多选行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      //批量删除
      handleDel() {
        if (this.listSel.length === 0) {
          return this.$mW("请至少选择一行需要处理的数据");
        }
        let idArr = [];
        this.listSel.forEach((item, index) => {
          idArr.push(item.accNo);
        });
        this.deletePross(idArr);
      },
      //单行删除
      handleDelRow(row) {
        let idArr = [];
        idArr.push(row.accNo);
        this.deletePross(idArr);
      },
      //删除处理
      deletePross(idArr) {
        this.szdPopDel().then(() => {
          delBankAcc(idArr).then(res => {
            if (res.code === 200) {
              this.getList();
              this.$mS("删除成功");
            }
          });
        });
      }
    }
  };
</script>
