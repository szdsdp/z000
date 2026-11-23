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
      row-key="sysBillNo"
      border
      stripe />

    <payFlowBillDg ref="payFlowBillDg" @updResult="updResult" />
  </div>
</template>

<script>
  import { listBill, delBill, updateCust } from "../api/payFlowBill.js";
  import { createPayReceiptDoc } from "../../payRece/api/payRece.js";
  import payFlowBillDg from "../payFlowBillDg/payFlowBillDg.vue";

  export default {
    name: "Z000_SD_PAY_FLOW_BILL_01B",
    components: { payFlowBillDg },
    data() {
      return {
        queryParams: {
          ranges: {},
          orders: []
        },
        schLay: {
          layout: { report: "Z000_SD_PAY_FLOW_BILL_01B", span: 8, more: 3 },
          schCat: [
            { field: "signDate", label: "签收日期", type: "date" },
            { field: "endorserAcc", label: "前手账号", type: "string" },
            { field: "endorser", label: "前手账号名称", type: "string" },
            { field: "billNo", label: "票包号", type: "string" },
            { field: "sysBillNo", label: "系统票号", type: "string" },
            { field: "holderAcc", label: "持票账户", type: "string", help: { a: "Z000_BANK_ACC" } },
            { field: "amt", label: "金额", type: "number", numDec: 2 },
            { field: "billType", label: "票据类型", type: "string", help: { a: "Z000_BILL_TYPE" } },
            { field: "billScope", label: "票据范围", type: "string", help: { a: "Z000_BILL_RANGE" } },
            { field: "issueDate", label: "出票日期", type: "date" },
            { field: "dueDate", label: "到期日期", type: "date" },
            { field: "cmpy", label: "公司代码", type: "string", help: { a: "CORE_CMPY" } },
            { field: "cust", label: "客户", type: "string", help: { a: "CORE_CUST" } }
          ]
        },
        tableParams: {
          layout: {
            report: "Z000_SD_PAY_FLOW_BILL_01B",
            showSummary: true,
            merge: "0",
            selection: "2",
            optionCell: "optionCell"
          },
          toolbar: {
            btns: [
              { fCode: "add", label: "新增", icon: "Plus" },
              { fCode: "import", label: "导入", icon: "Upload" },
              { fCode: "claim", label: "认领", icon: "Check" },
              { fCode: "cust", label: "客户", icon: "OfficeBuilding" }
            ]
          },
          fieldCat: [
            { field: "sysBillNo", label: "系统票号", minWidth: 150 },
            { field: "billNo", label: "票包号", minWidth: 250 },
            { field: "subBillFrom", label: "子票区间从", minWidth: 120, align: "right" },
            { field: "subBillTo", label: "子票区间至", minWidth: 120, align: "right" },
            { field: "holderAcc", label: "持票账户", minWidth: 150 },
            { field: "endorserAcc", label: "前手账号", minWidth: 150 },
            { field: "endorser", label: "前手账号名称", minWidth: 200 },
            { field: "drawer", label: "出票人", minWidth: 200 },
            { field: "payee", label: "收票人", minWidth: 200 },
            { field: "custName", label: "客户", minWidth: 200 },
            { field: "amt", label: "金额", type: "number", numDec: 2, minWidth: 120, doSum: "1" },
            { field: "amtProcess", label: "认领中金额", type: "number", numDec: 2, minWidth: 120, doSum: "1" },
            { field: "amtReceipt", label: "已认领金额", type: "number", numDec: 2, minWidth: 120, doSum: "1" },
            { field: "amtRemain", label: "剩余金额", type: "number", numDec: 2, minWidth: 120, doSum: "1" },
            { field: "signDate", label: "签收日期", minWidth: 120 },
            { field: "billTypeName", label: "票据类型", minWidth: 120 },
            { field: "billScopeName", label: "票据范围", minWidth: 120 },
            { field: "acceptor", label: "承兑人", minWidth: 250 },
            { field: "issueDate", label: "出票日期", minWidth: 120 },
            { field: "dueDate", label: "到期日期", minWidth: 120 },
            { field: "summary", label: "银行摘要", minWidth: 150 },
            { field: "cmpyName", label: "公司代码", minWidth: 120 },
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
        listBill(this.queryParams).then(response => {
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
        this.$refs.payFlowBillDg.acceptData({ sysBillNo: "", option: "C" });
      },

      // 行操作
      handleUpdRow(row, option) {
        if (row.amtReceipt !== 0) {
          return this.$mW("已存在认款金额不能修改");
        }
        this.$refs.payFlowBillDg.acceptData({ sysBillNo: row.sysBillNo, option: option });
      },

      //单行删除
      handleDelRow(row) {
        if (row.amtReceipt !== 0) {
          return this.$mW("已存在认款金额不能删除");
        }
        this.szdPopDel().then(() => {
          delBill({ sysBillNo: row.sysBillNo }).then(res => {
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
          uploadUrl: "/z000/sd/payFlowBill/import",
          TMCode: "Z000_PAY_BILL"
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
            sysIdArr.push(this.listSel[i].sysBillNo);
          }
        }
        if (sysIdArr.length === 0) {
          return this.$mW("请选择未认领的票据数据");
        }
        this.selectCust(sysIdArr);
      },
      //选择和更新客户
      handleRowCust(row) {
        if (row.amtReceipt !== 0) {
          return this.$mW("已存在认款金额不能更改客户");
        }
        this.selectCust([row.sysBillNo]);
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
          sysIdArr.push(this.listSel[i].sysBillNo);
        }
        if (sysIdArr.length === 0) {
          return this.$mW("请选择需要认领的票据数据");
        }
        this.doClaim(sysIdArr);
      },
      //选择和认领
      handleRowClaim(row) {
        this.doClaim([row.sysBillNo]);
      },
      //执行认领
      doClaim(sysIdArr) {
        for (let i = 0; i < sysIdArr.length; i++) {
          const sysBillNo = sysIdArr[i];
          createPayReceiptDoc({
            sysBillNo: sysBillNo,
            bstp: "Z000_SD_HK01",
            payWay: this.dataList.find(r => r.sysBillNo === sysBillNo)?.billType || "P",
            curr: "CNY"
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
          let response = await listBill(_query);
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
