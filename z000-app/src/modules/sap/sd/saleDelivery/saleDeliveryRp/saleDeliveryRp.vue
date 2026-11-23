<template>
  <div class="szd__tb__wrapper">
    <div class="szd__tb__top">
      <szd-button label="新增" type="primary" @click="handleClickAdd" style="margin-right: 5px" />
      <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSchEventImp" />
    </div>
    <div class="szd__popup__main" style="padding: 5px 0">
      <szd-card-a v-for="row in dataList" :key="row.bussId">
        <template #header>{{ row.bussDocId || "未提交" }}</template>
        <template #main>
          <szd-card-row-span label="业务标题" :text="row.title" v-if="row.title" />
          <szd-card-row-span label="单据状态" :text="row.bussStatusName" />
          <szd-card-row-span label="公司" :text="row.cmpyName" />
          <szd-card-row-span label="部门" :text="row.cstcName" />
          <szd-card-row-span label="业务日期" :text="row.bussDate" />
          <szd-card-row-span label="创建人" :text="row.createByName" />
          <szd-card-row-span label="创建时间" :text="row.createTime" />
        </template>
        <template #footer>
          <szd-button label="详情" type="primary" @click="handleRow(row, 'V')" />
          <szd-button label="修改" type="primary" @click="handleRow(row, 'U')" />
        </template>
      </szd-card-a>
    </div>
    <szd-pagination :total="page.total" v-model:page="page.pageNum" v-model:limit="page.pageSize" @pagination="getList" />
  </div>
</template>
<script>
  import { listApply } from "../api/saleDelivery.js";

  export default {
    name: "Z000_SD_XSJH_01B",
    data() {
      return {
        routerCodeDg: "Z000_SD_XSJH_01A",
        queryParams: { ranges: {} },
        schLay: {
          layout: { report: "Z000_SD_XSJH_01B", span: 8, more: 3 },
          schCat: [
            { field: "bussDocId", label: "业务单号", type: "string" },
            {
              field: "bussDate",
              label: "业务日期",
              type: "date"
            },
            { field: "bussStatus", label: "单据状态", type: "string", help: { a: "Z000_DOC_STAU" } },
            { field: "cmpy", label: "公司代码", type: "string", help: { a: "CORE_CMPY" } },
            { field: "cstc", label: "部门", type: "string", help: { a: "CORE_CSTC" } },
            {
              field: "createTime",
              label: "创建日期",
              type: "date"
            }
          ]
        },
        // 分页
        page: { total: 0, pageNum: 1, pageSize: 20 },
        // 数据列表
        dataList: []
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
        if (obj === "$search") {
          this.getList();
        }
      },
      // 查询列表
      getList() {
        this.queryParams.pageNum = this.page.pageNum;
        this.queryParams.pageSize = this.page.pageSize;
        listApply(this.queryParams).then(r => {
          this.dataList = r.rows;
          this.page.total = r.total;
        });
      },
      // 查看/修改
      handleRow(row, option) {
        this.szdWf.goDoc(this.routerCodeDg, row.bussId, option);
      }
    }
  };
</script>
