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
      :callMethod="handleCallMethod"
      row-key="belnrFi"
      border
      stripe />
  </div>
</template>

<script>
  import { queryCustRecon } from "../api/custRecon.js";

  export default {
    name: "Z000_SD_DZCX_01B",
    data() {
      return {
        queryParams: { ranges: {}, orders: [] },
        schLay: {
          layout: { report: "Z000_SD_DZCX_01B", span: 8, more: 3 },
          schCat: [
            { field: "dateRange", label: "日期", type: "date", required: true, whereOpt: "BT" },
            {
              field: "bukrs",
              label: "公司",
              type: "string",
              required: true,
              noTo: true,
              help: { a: "CORE_CMPY", c: "cmpy" },
              whereOpt: "SP"
            },
            {
              field: "kunnr",
              label: "客户",
              type: "string",
              required: true,
              noTo: true,
              help: { a: "CORE_CUST", c: "cust" }
            }
          ]
        },
        tableParams: {
          layout: {
            report: "Z000_SD_DZCX_01B",
            showSummary: false,
            merge: "0",
            height: "calc(100vh - 220px)"
          },
          toolbar: { btns: [] },
          fieldCat: [
            { field: "bukrs", label: "公司代码", minWidth: 100 },
            { field: "kunnr", label: "客户", minWidth: 100 },
            { field: "kunnrTxt", label: "客户描述", minWidth: 200 },
            { field: "belnrFi", label: "会计凭证", minWidth: 120 },
            { field: "vbeln", label: "发票凭证", minWidth: 120 },
            { field: "blartFi", label: "凭证类型", minWidth: 100 },
            { field: "fkart", label: "开票类型", minWidth: 100 },
            { field: "budatFi", label: "过账日期", minWidth: 100 },
            { field: "fkdat", label: "发票日期", minWidth: 100 },
            { field: "waers", label: "货币", minWidth: 80 },
            {
              field: "dmbtrY",
              label: "原币金额(含税)",
              minWidth: 140,
              type: "number",
              numDec: 2
            },
            {
              field: "dmbtrL",
              label: "本币金额(含税)",
              minWidth: 140,
              type: "number",
              numDec: 2
            },
            {
              field: "dmbtrSd",
              label: "发票金额(含税)",
              minWidth: 140,
              type: "number",
              numDec: 2
            },
            {
              field: "dmbtrFi",
              label: "财务金额(含税)",
              minWidth: 140,
              type: "number",
              numDec: 2
            },
            {
              field: "dmbtr",
              label: "收入",
              minWidth: 140,
              type: "number",
              numDec: 2
            },
            {
              field: "dmbtrYe",
              label: "余额",
              minWidth: 140,
              type: "number",
              numDec: 2
            },
            { field: "bktxt", label: "抬头文本", minWidth: 200 },
            { field: "bzirk", label: "销售部", minWidth: 100 },
            { field: "spart", label: "产品组", minWidth: 100 },
            { field: "gjahrFi", label: "年度", minWidth: 80 },
            { field: "xblnrFi", label: "参照", minWidth: 150 },
            { field: "awkeyFi", label: "参考码", minWidth: 150 },
            { field: "stblgFi", label: "冲销凭证", minWidth: 120 },
            { field: "stjahFi", label: "冲销年度", minWidth: 100 },
            {
              field: "kursf",
              label: "汇率",
              minWidth: 100,
              type: "number",
              numDec: 4
            },
            { field: "buzei", label: "行项目", minWidth: 80 },
            { field: "fksto", label: "发票冲销标识", minWidth: 130 },
            { field: "sfakn", label: "被冲销发票凭证", minWidth: 130 }
          ],
          page: { total: 0, pageNum: 1, pageSize: 20 },
          oper: {}
        },
        dataList: []
      };
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

      getList() {
        // 深拷贝查询参数，避免影响搜索区域展示
        const submitParams = JSON.parse(JSON.stringify(this.queryParams));
        // 将日期范围字段拆分为后台需要的 bdate 和 edate
        const ranges = submitParams.ranges;
        if (ranges.dateRange && ranges.dateRange[0]) {
          const dr = ranges.dateRange[0];
          ranges.bdate = [{ type: "date", option: "BT", low: dr.low || "" }];
          ranges.edate = [{ type: "date", option: "EQ", low: dr.high || "" }];
          delete ranges.dateRange;
        }
        submitParams.pageNum = this.tableParams.page.pageNum;
        submitParams.pageSize = this.tableParams.page.pageSize;
        queryCustRecon(submitParams).then(response => {
          this.dataList = response.rows;
          this.tableParams.page.total = response.total;
        });
      },
      // 表格请求回调
      handleRequestImp(obj) {
        this.getList();
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
          let response = await queryCustRecon(_query);
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
