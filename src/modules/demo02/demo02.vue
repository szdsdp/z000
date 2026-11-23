<template>
  <div class="app-container flex-y" style="padding-top: 10px">
    <szd-list :data="dataList" :total="page.total" @load="handleLoad" @refresh="handleRefresh">
      <template #default="scope">
        <szd-card-a>
          <!--  表头   -->
          <template #header>
            {{ scope.row.bussDocId || "未提交" }}
          </template>
          <!--  内容 -->
          <template #main>
            <span>{{ scope.row.createByName }} | {{ scope.row.cmpyName }}</span>
            <span>{{ scope.row.title || "" }}</span>
            <span>{{ scope.row.bstpName }} | {{ scope.row.amtApply || "0.00" }}</span>
            <span>{{ scope.row.createTime }}</span>
          </template>
          <!-- 表尾 -->
          <template #footer>
            <szd-button label="详情" type="primary" @click="handleDetail(scope.row)" />
            <szd-button label="影像" type="primary" @click="handleEis(scope.row)" />
          </template>
        </szd-card-a>
      </template>
    </szd-list>
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
      handleRefresh() {
        console.log("1-handleRefresh");
        this.page.pageNum = 1;
        this.getList();
      },
      handleLoad() {
        console.log("1-handleLoad");
        this.page.pageNum += 1;
        this.getList();
      },
      //详情
      handleDetail(rowData) {
        this.szdWf.goDoc(rowData.routerCode, rowData.bussId, rowData.createBy, "U", rowData.program);
      },

      //影像
      handleEis(row) {}
    }
  };
</script>