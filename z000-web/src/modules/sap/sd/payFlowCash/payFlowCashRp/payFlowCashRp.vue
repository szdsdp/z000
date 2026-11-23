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
      @handleRowEvent="handleRowEventImp"
      @selection-change="handleSelRow"
      :callMethod="handleCallMethod"
      row-key="bankDoc"
      border
      stripe />

    <payFlowCashDg ref="payFlowCashDg" @updResult="updResult" />
  </div>
</template>

<script>
  import { listPayFlow, delPayFlow, updateCust } from "../api/payFlowCash.js";
  import { createPayReceiptDoc } from "../../payRece/api/payRece.js";
  import payFlowCashDg from "../payFlowCashDg/payFlowCashDg.vue";

  export default {
    name: "Z000_SD_PAY_FLOW_CASH_01B",
    components: { payFlowCashDg },
    data() {
      return {
        queryParams: {
          ranges: {},
          orders: []
        },
        schLay: {
          layout: { report: "Z000_SD_PAY_FLOW_CASH_01B", span: 8, more: 3 },
          schCat: [
            { field: "tranDate", label: "交易日期", type: "date" },
            { field: "payAccNo", label: "对方账户", type: "string" },
            { field: "payAccName", label: "对方户名", type: "string" },
            { field: "bankDoc", label: "银行回单", type: "string" },
            { field: "cmpy", label: "公司代码", type: "string", help: { a: "CORE_CMPY" } },
            { field: "bankFlowNo", label: "银行流水号", type: "string" },
            { field: "myAccNo", label: "本方账户", type: "string", help: { a: "Z000_BANK_ACC" } },
            { field: "amt", label: "金额", type: "number", numDec: 2 },
            { field: "cust", label: "客户", type: "string", help: { a: "CORE_CUST" } }
          ]
        },
        tableParams: {
          layout: { report: "Z000_SD_PAY_FLOW_CASH_01B", showSummary: true, merge: "0", selection: "2", optionCell: "optionCell" },
          toolbar: {
            btns: [
              { fCode: "add", label: "新增", icon: "Plus" },
              { fCode: "import", label: "导入", icon: "Upload" },
              { fCode: "claim", label: "认领", icon: "Check" },
              { fCode: "cust", label: "客户", icon: "OfficeBuilding" }
            ]
          },
          fieldCat: [
            { field: "bankDoc", label: "银行回单", minWidth: 150 },
            { field: "myAccNo", label: "本方账户", minWidth: 150 },
            { field: "payAccNo", label: "对方账户", minWidth: 180 },
            { field: "payAccName", label: "对方户名", minWidth: 250 },
            { field: "custName", label: "客户", minWidth: 200 },
            { field: "amt", label: "回款金额", type: "number", numDec: 2, minWidth: 120, doSum: "1" },
            { field: "amtProcess", label: "认领中金额", type: "number", numDec: 2, minWidth: 120, doSum: "1" },
            { field: "amtReceipt", label: "已认领金额", type: "number", numDec: 2, minWidth: 120, doSum: "1" },
            { field: "amtRemain", label: "剩余金额", type: "number", numDec: 2, minWidth: 120, doSum: "1" },
            { field: "curr", label: "币种", minWidth: 100 },
            { field: "tranDate", label: "交易日期", minWidth: 120, type: "date" },
            { field: "tranTime", label: "交易时间", minWidth: 120, type: "time" },
            { field: "summary", label: "银行摘要", minWidth: 150 },
            { field: "cmpyName", label: "公司", minWidth: 150 },
            { field: "bankFlowNo", label: "银行流水号", minWidth: 150 },
            {
              field: "operation",
              label: "操作",
              type: "button",
              fixed: "right",
              align: "center",
              ctrlParam: [
                { fCode: "claim", label: "认领", icon: "Check", type: "link" },
                { fCode: "cust", label: "客户", icon: "OfficeBuilding", type: "link" },
                { fCode: "edit", label: "编辑", icon: "Edit", type: "link" },
                { fCode: "del", label: "删除", icon: "Delete", type: "link" }
              ],
              width: 200
            }
          ],
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
      // 表格请求回调
      handleRequestImp(obj) {
        this.getList();
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
          case "claim":
            this.handleMoreClaim();
            break;
          case "cust":
            this.handleMoreCust();
            break;
        }
      },
      // 行回调
      handleRowEventImp(obj) {
        switch (obj.fCode) {
          case "del":
            this.handleDelRow(obj.row);
            break;
          case "edit":
            this.handleUpdRow(obj.row, "U");
            break;
          case "claim":
            this.handleRowClaim(obj.row);
            break;
          case "cust":
            this.handleRowCust(obj.row);
            break;
        }
      },

      //查询数据
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },

      //查询列表
      getList() {
        this.queryParams.pageNum = this.tableParams.page.pageNum;
        this.queryParams.pageSize = this.tableParams.page.pageSize;
        listPayFlow(this.queryParams).then(response => {
          if (response.code === 200) {
            this.dataList = response.rows;
            this.tableParams.page.total = response.total;
            this.tableParams.oper = response.oper;
          }
          if (this.dataList && this.dataList.length > 0) {
            this.dataList.forEach(row => {
              row.optionCell = { operation: { edit: {}, del: {} } };
            });
          }
        });
      },

      // 新增
      handleAdd() {
        this.$refs.payFlowCashDg.acceptData({ bankDoc: "", option: "C" });
      },

      //行操作
      handleUpdRow(row, option) {
        if (row.amtReceipt !== 0) {
          return this.$mW("已存在认款金额不能修改");
        }
        this.$refs.payFlowCashDg.acceptData({ bankDoc: row.bankDoc, option: option });
      },

      //单行删除
      handleDelRow(row) {
        if (row.amtReceipt !== 0) {
          return this.$mW("已存在认款金额不能删除");
        }
        this.szdPopDel().then(() => {
          delPayFlow({ bankDoc: row.bankDoc }).then(res => {
            if (res.code === 200) {
              this.getList();
              this.$mS("删除成功");
            }
          });
        });
      },

      //处理返回结果
      updResult(resultData) {
        if (resultData.retcode === "S") {
          this.handleQuery();
        }
      },

      // 导入
      handleImport() {
        this.szdUpload({
          uploadUrl: "/z000/sd/payFlowCash/import",
          TMCode: "Z000_PAY_CASH"
        }).then(res => {
          this.getList();
        });
      },

      // 选中
      handleSelRow(selection) {
        this.listSel = selection;
      },

      //选择和更新客户-批量
      handleMoreCust() {
        let sysIdArr = [];
        for (let i = 0; i < this.listSel.length; i++) {
          if (this.listSel[i].amtReceipt === 0) {
            sysIdArr.push(this.listSel[i].bankDoc);
          }
        }
        if (sysIdArr.length === 0) {
          return this.$mW("请选择未认领的回款流水数据");
        }
        this.selectCust(sysIdArr);
      },
      //选择和更新客户
      handleRowCust(row) {
        if (row.amtReceipt !== 0) {
          return this.$mW("已存在认款金额不能更改客户");
        }
        this.selectCust([row.bankDoc]);
      },
      //选择和更新客户信息
      selectCust(sysIdArr) {
        this.szdSh({ a: "CORE_CUST" }).then(resSh => {
          if (resSh.data && resSh.data.length > 0) {
            updateCust({ cust: resSh.data[0].cust, sysId: sysIdArr }).then(res => {
              if (res.code === 200) {
                this.szdWf.retMsg(res.data);
              }
              this.getList();
            });
          }
        });
      },

      //选择和认领-批量
      handleMoreClaim() {
        let sysIdArr = [];
        for (let i = 0; i < this.listSel.length; i++) {
          sysIdArr.push(this.listSel[i].bankDoc);
        }
        if (sysIdArr.length === 0) {
          return this.$mW("请选择需要认领的回款数据");
        }
        this.doClaim(sysIdArr);
      },
      //选择和认领
      handleRowClaim(row) {
        this.doClaim([row.bankDoc]);
      },
      //执行认领
      doClaim(sysIdArr) {
        for (let i = 0; i < sysIdArr.length; i++) {
          const bankDoc = sysIdArr[i];
          createPayReceiptDoc({
            bankDoc: bankDoc,
            bstp: "Z000_SD_HK01",
            payWay: "B",
            curr: this.dataList.find(r => r.bankDoc === bankDoc)?.curr || "CNY"
          }).then(res => {
            if (res.code === 200 && res.data) {
              this.getList();
              this.$mS("认领单创建成功");
            }
          });
        }
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
          let response = await listPayFlow(_query);
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
