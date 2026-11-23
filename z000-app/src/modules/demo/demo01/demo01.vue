<template>
  <!-- 样式类说明
   szd__tb__wrapper 高度100%, 不允许滚动条 
   szd__popup__top 固定高度，一般用于功能按钮
   szd__popup__main 内容区域，占满剩余高度 -->
  <div class="szd__tb__wrapper">
    <!-- 查询条件 -->
    <div class="szd__popup__top">
      <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSeniorSearchEventImp" />
    </div>
    <!-- 显示数据  -->
    <div class="szd__popup__main" style="padding: 5px 0">
      <!-- 卡片区域 -->
      <szd-card-a v-for="row in dataList">
        <!-- 表头 -->
        <template #header>
          <szd-title-label :text="row.bussDocId || '未提交'" />
        </template>
        <!--  内容 -->
        <template #main>
          <szd-card-row-span v-if="row.title" label="业务标题" :text="row.title" />
          <szd-card-row-span label="业务类型" :text="row.bstpName" />
          <szd-card-row-span label="公司" :text="row.cmpyName" />
          <szd-card-row-span v-if="row.amtApply && row.amtApply != 0" label="金额" :text="row.amtApply" :numDec="2" />
          <szd-card-row-span label="创建人" :text="row.createByName" />
          <szd-card-row-span label="创建时间" :text="row.createTime" />
          <!-- 或以下写法 -->
          <span class="card-text-row">
            <div class="title">审批人:</div>
            <div class="content">{{ row.userName }} {{ row.agentName ? " | " + row.agentName : "" }}</div>
          </span>
        </template>
        <!-- 表尾 -->
        <template #footer>
          <szd-button label="详情" type="primary" @click="handleView(row)" />
          <szd-button label="测试" type="primary" @click="handleTest(row)" />
        </template>
      </szd-card-a>
    </div>
    <!-- 分页 -->
    <szd-pagination :total="page.total" v-model:page="page.pageNum" v-model:limit="page.pageSize" @pagination="getList" />
  </div>
</template>

<script>
  import { listApply } from "./demo01Api";

  export default {
    data() {
      return {
        //查询参数
        queryParams: {
          ranges: {}
        },
        schLay: {
          layout: {},
          schCat: [
            { field: "bussDocId", label: "业务单号", type: "string" },
            { field: "title", label: "任务标题", type: "string" },
            { field: "cmpy", label: "公司代码", type: "string", help: { a: "CORE_CMPY" } },
            { field: "bstp", label: "业务类型", type: "string", help: { a: "CORE_BSTP" } },
            { field: "createTime", label: "制单日期", type: "date" },
            { field: "status", label: "单据状态", type: "string", help: { a: "CORE_WF_DOST" } }
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
      handleSeniorSearchEventImp(obj) {
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
        this.szdWf.goDoc(row.routerCode, row.bussId, "U");
      },

      //测试
      handleTest(row) {
        this.$mS("点击的测试按钮");
      }
    }
  };
</script>