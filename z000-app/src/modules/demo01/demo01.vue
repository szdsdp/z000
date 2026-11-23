<template>
  <div class="szd__tb__wrapper">
    <!-- 查询条件 -->
    <div class="szd__popup__top">
      <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSeniorSearchEventImp" />
    </div>
    <!-- 查询数据 -->
    <div class="szd__popup__main" style="padding: 5px 0">
      <szd-card-a v-for="row in dataList">
        <!--  表头   -->
        <template #header>
          {{ row.bussDocId || "未提交" }}
        </template>
        <!--  内容 -->
        <template #main>
          <span>{{ row.createByName }} | {{ row.cmpyName }}</span>
          <span>{{ row.title || "" }}</span>
          <span>{{ row.bstpName }} | {{ row.amtApply || "0.00" }}</span>
          <span>{{ row.createTime }}</span>
        </template>
        <!-- 表尾 -->
        <template #footer>
          <szd-button label="详情" type="primary" @click="handleView(row)" />
          <szd-button label="影像" type="primary" @click="handleEis(row)" />
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
        // 分页
        page: {
          total: 0,
          pageNum: 1,
          pageSize: 20
        },
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
      handleView(rowData) {
        this.szdWf.goDoc(rowData.routerCode, rowData.bussId, rowData.createBy, "U", rowData.program);
      },

      //影像
      handleEis(row) {}
    }
  };
</script>