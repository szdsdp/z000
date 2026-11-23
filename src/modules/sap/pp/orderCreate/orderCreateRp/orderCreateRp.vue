<template>
  <div class="szd__tb__wrapper">
    <!-- 查询条件 -->
    <div class="szd__tb__top">
      <szd-button label="新增" type="primary" @click="handleClickAdd" style="margin-right: 5px" />
      <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSchEventImp" />
    </div>

    <!-- 查询数据 -->
    <div class="szd__popup__main" style="padding: 5px 0">
      <szd-card-a v-for="row in dataList" :key="row.bussId">
        <!-- 表头 -->
        <template #header>
          {{ row.bussDocId || "未提交" }}
        </template>
        <!-- 内容 -->
        <template #main>
          <szd-card-row-span label="物料编码" :text="row.mtlCode" />
          <szd-card-row-span label="物料名称" :text="row.mtlName" v-if="row.mtlName" />
          <szd-card-row-span label="单据状态" :text="row.bussStatusName" />
          <szd-card-row-span label="订单类型" :text="row.orderTypeName" />
          <szd-card-row-span label="工厂" :text="row.factName" />
          <szd-card-row-span label="订单数量总计" :text="row.quantity" />
          <szd-card-row-span label="单位" :text="row.unitName" />
          <szd-card-row-span label="已下达" :text="row.releasedName" />
          <szd-card-row-span label="已关闭" :text="row.closedName" />
          <szd-card-row-span label="业务日期" :text="row.bussDate" />
          <szd-card-row-span label="创建人" :text="row.createByName" />
          <szd-card-row-span label="创建时间" :text="row.createTime" />
        </template>
        <!-- 表尾 -->
        <template #footer>
          <szd-button label="详情" type="primary" @click="handleRow(row, 'V')" />
          <szd-button label="修改" type="primary" @click="handleRow(row, 'U')" />
        </template>
      </szd-card-a>
    </div>
    <!-- 分页 -->
    <szd-pagination :total="page.total" v-model:page="page.pageNum" v-model:limit="page.pageSize" @pagination="getList" />
  </div>
</template>

<script>
  import { listApply } from "../api/orderCreate.js";

  export default {
    name: "Z000_PP_SCDDCJ_01B",
    data() {
      return {
        routerCodeDg: "Z000_PP_SCDDCJ_01A",
        queryParams: {
          ranges: {}
        },
        schLay: {
          layout: { report: "Z000_PP_SCDDCJ_01B", span: 8, more: 3 },
          schCat: [
            { field: "bussDocId", label: "业务单号", type: "string" },
            { field: "bussDate", label: "业务日期", type: "date" },
            { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } },
            { field: "mtlCode", label: "物料编码", type: "string" },
            { field: "fact", label: "工厂", type: "string", help: { a: "CORE_FACT" } },
            { field: "orderType", label: "订单类型", type: "string", help: { a: "Z000_PP_SCDDCJ_ORDER_TYPE" } },
            { field: "createTime", label: "创建日期", type: "date" }
          ]
        },
        // 分页
        page: {
          total: 0,
          pageNum: 1,
          pageSize: 20
        },
        // 数据列表
        dataList: [],
        listSel: []
      };
    },
    created() {
      this.getList();
    },
    methods: {
      // 新增
      handleClickAdd() {
        this.szdWf.goNav(this.routerCodeDg);
      },
      // 高级查询按钮回调
      handleSchEventImp(obj) {
        switch (obj) {
          case "$search":
            this.getList();
            break;
        }
      },
      // 查询列表
      getList() {
        this.queryParams.pageNum = this.page.pageNum;
        this.queryParams.pageSize = this.page.pageSize;
        listApply(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.page.total = response.total;
        });
      },
      // 查看/修改
      handleRow(row, option) {
        this.szdWf.goDoc(this.routerCodeDg, row.bussId, option);
      }
    }
  };
</script>
