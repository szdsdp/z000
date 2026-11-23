<template>
  <div class="szd__tb__wrapper">
    <!-- 查询条件 -->
    <div class="szd__tb__top">
      <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSchEventImp" />
    </div>
    <!-- 显示数据  -->
    <div class="szd__tb__main" style="padding: 5px 0">
      <!-- 卡片区域 -->
      <szd-card-a v-for="row in dataList">
        <!-- 表头 -->
        <template #header>
          <szd-title-label :text="row.bussDocId || '未提交'" />
        </template>
        <!--  内容 -->
        <template #main>
          <szd-card-row-span label="业务类型" :text="row.bstpName" />
          <szd-card-row-span v-if="row.title" label="业务标题" :text="row.title" />
          <szd-card-row-span label="公司" :text="row.cmpyName" />
          <szd-card-row-span label="部门" :text="row.cstcName" />
          <szd-card-row-span label="报账人" :text="row.userName" />
          <szd-card-row-span label="金额" :text="row.amtApply" :numDec="2" />
          <szd-card-row-span label="业务日期" :text="row.bussDate" />
          <szd-card-row-span label="单据状态" :text="row.bussStatus" />
          <szd-card-row-span label="创建时间" :text="row.createTime" />
        </template>
        <!-- 表尾 -->
        <template #footer>
          <szd-button label="详情" type="primary" @click="handleView(row)" />
        </template>
      </szd-card-a>
      <!-- 未查询到数据 -->
      <szd-nothing v-if="!dataList || dataList.length === 0" />
    </div>
    <!-- 分页 -->
    <szd-pagination :total="page.total" v-model:page="page.pageNum" v-model:limit="page.pageSize" @pagination="getList" />
  </div>
</template>

<script>
  import { listApply } from "../api/expCost.js";

  export default {
    data() {
      return {
        routerCodeDg: "Z000_DEMO_EXP_01A",
        queryParams: {
          ranges: {}
        },
        schLay: {
          schCat: [
            { field: "cmpy", label: "公司", type: "string", help: { a: "CORE_CMPY", c: "cmpy" } },
            { field: "cstc", label: "部门", type: "string", help: { a: "CORE_CSTC", c: "cstc" } },
            { field: "userId", label: "报账人", type: "string", help: { a: "CORE_USER", c: "userId" } },
            { field: "bussDate", label: "业务日期", type: "date" },
            { field: "bstp", label: "业务类型", type: "string", help: { a: "CORE_BSTP", c: "bstp" } },
            { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } }
          ]
        },
        //分页
        page: { total: 0, pageNum: 1, pageSize: 20 },
        //数据列表
        dataList: []
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
            this.page.pageNum = 1;
            this.getList();
            break;
        }
      },

      //查询列表
      getList() {
        this.queryParams.pageNum = this.page.pageNum;
        this.queryParams.pageSize = this.page.pageSize;
        listApply(this.queryParams).then(response => {
          if (response.code === 200) {
            this.dataList = response.rows;
            this.page.total = response.total;
          }
        });
      },

      //详情
      handleView(row) {
        this.szdWf.goDoc(this.routerCodeDg, row.bussId, "U");
      }
    }
  };
</script>
