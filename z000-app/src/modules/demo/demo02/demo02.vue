<template>
  <div class="szd__tb__wrapper">
    <!-- 查询条件 -->
    <div class="szd__popup__top">
      <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSeniorSearchEventImp" />
    </div>
    <!-- 显示数据  -->
    <div class="szd__popup__main" style="padding: 5px 0">
      <szd-list :data="dataList" :total="page.total" @load="handleLoad" @refresh="handleRefresh">
        <template #default="scope">
          <!-- 卡片区域 -->
          <szd-card-a>
            <!--  表头   -->
            <template #header>
              <szd-title-label :text="scope.row.bussDocId || '未提交'" />
            </template>
            <!--  内容 -->
            <template #main>
              <szd-card-row-span v-if="scope.row.title" label="业务标题" :text="scope.row.title" />
              <szd-card-row-span label="业务类型" :text="scope.row.bstpName" />
              <szd-card-row-span label="公司" :text="scope.row.cmpyName" />
              <szd-card-row-span v-if="scope.row.amtApply && scope.row.amtApply != 0" label="金额" :text="scope.row.amtApply" :numDec="2" />
              <szd-card-row-span label="创建人" :text="scope.row.createByName" />
              <szd-card-row-span label="创建时间" :text="scope.row.createTime" />
              <!-- 或以下写法 -->
              <span class="card-text-row">
                <div class="title">审批人:</div>
                <div class="content">{{ scope.row.userName }} {{ scope.row.agentName ? " | " + scope.row.agentName : "" }}</div>
              </span>
            </template>
            <!-- 表尾 -->
            <template #footer>
              <szd-button label="详情" type="primary" @click="handleDetail(scope.row)" />
              <szd-button label="测试" type="primary" @click="handleTest(scope.row)" />
            </template>
          </szd-card-a>
        </template>
      </szd-list>
    </div>
  </div>
</template>

<script>
  import { listApply } from "./demo02Api";

  export default {
    data() {
      return {
        //查询参数
        queryParams: {
          ranges: {}
        },
        schLay: {
          layout: { report: "CORE_PER_02A", span: 8, more: 3 },
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
        if (this.page.pageNum === 1) this.dataList = [];
        this.queryParams.pageNum = this.page.pageNum;
        this.queryParams.pageSize = this.page.pageSize;
        listApply(this.queryParams).then(response => {
          if (response.code === 200) {
            this.dataList.push(...response.rows);
            this.page.total = response.total;
          }
        });
      },

      //下拉刷新
      handleRefresh() {
        this.page.pageNum = 1;
        this.getList();
      },

      //滚动加载
      handleLoad() {
        this.page.pageNum += 1;
        this.getList();
      },

      //详情
      handleDetail(row) {
        this.szdWf.goDoc(row.routerCode, row.bussId, "U", row.program);
      },

      //测试
      handleTest(row) {
        this.$mS("点击的测试按钮");
      }
    }
  };
</script>