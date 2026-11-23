<template>
  <div class="szd__tb__wrapper">
    <!-- 查询条件 -->
    <div class="szd__tb__top" style="justify-content: space-between">
      <szd-checkbox-all v-model:data="dataList" @selection-change="handleSelRow" />
      <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleSchEventImp" />
    </div>

    <!-- 查询数据 -->
    <div class="szd__tb__main" style="padding: 5px 0">
      <szd-card-a v-for="row in dataList">
        <!--  表头   -->
        <template #header>
          <div style="flex: 1">
            <szd-title-label :text="'公司代码:' + row.cmpy" />
          </div>
          <div style="width: 35px">
            <var-checkbox v-model="row.$SZDCHECKBOX" />
          </div>
        </template>
        <!--  内容 -->
        <template #main>
          <szd-card-row-span label="公司名称" :text="row.cmpyName" />
          <szd-card-row-span label="公司简称" :text="row.cmpyShort" />
          <szd-card-row-span label="账套" :text="row.accSetName" />
        </template>
        <!-- 表尾 -->
        <template #footer>
          <szd-button label="删除" type="primary" @click="handleDelRow(row)" />
          <szd-button label="详情" type="primary" @click="handleUpdRow(row)" />
        </template>
      </szd-card-a>
    </div>
    <!-- 分页 -->
    <szd-pagination :total="page.total" v-model:page="page.pageNum" v-model:limit="page.pageSize" @pagination="getList" />

    <!-- 信息维护 -->
    <mdmCmpyInfo ref="mdmCmpyInfo" @updResult="updResult" />
  </div>
</template>

<script>
  import { listCompany, delCompany } from "../api/mdmCmpy.js";
  import mdmCmpyInfo from "./mdmCmpyInfo.vue";

  export default {
    components: {
      mdmCmpyInfo
    },
    data() {
      return {
        //查询参数
        queryParams: {
          ranges: {}
        },
        schLay: {
          layout: {},
          schBtn: [
            { fCode: "add", label: "新增" },
            { fCode: "del", label: "删除" }
          ],
          schCat: [
            { field: "cmpy", label: "公司代码", type: "string", help: { a: "CORE_CMPY", c: "cmpy" } },
            { field: "cmpyName", label: "公司名称", type: "string", whereOpt: "SP" },
            { field: "cmpyShort", label: "公司简称", type: "string", whereOpt: "SP" },
            { field: "accSet", label: "所属账套", type: "string", help: { a: "CORE_VCH_ACCS" } }
          ]
        },
        // 分页
        page: { total: 0, pageNum: 1, pageSize: 20 },
        //数据列表
        dataList: [],
        //列表多选
        listSel: []
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
          case "add":
            this.handleAdd();
            break;
          case "del":
            this.handleDel();
            break;
        }
      },

      //查询列表
      getList() {
        this.queryParams.pageNum = this.page.pageNum;
        this.queryParams.pageSize = this.page.pageSize;
        listCompany(this.queryParams).then(response => {
          if (response.code === 200) {
            this.dataList = response.rows;
            this.page.total = response.total;
          }
        });
      },
      //新增
      handleAdd() {
        this.$refs.mdmCmpyInfo.callMdmCmpyInfo({ option: "C", cmpy: "" });
      },
      //修改
      handleUpdRow(row) {
        this.$refs.mdmCmpyInfo.callMdmCmpyInfo({ option: "U", cmpy: row.cmpy });
      },
      //处理返回结果
      updResult(resultData) {
        if (resultData.retCode === "S") {
          this.getList();
        }
      },
      //多选行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      //批量删除
      handleDel() {
        if (this.listSel.length === 0) {
          return this.$mW("请至少选择一行需要处理的数据");
        }
        let idArr = [];
        this.listSel.forEach((item, index) => {
          idArr.push(item.cmpy);
        });
        this.deletePross(idArr);
      },
      //单行删除
      handleDelRow(row) {
        let idArr = [];
        idArr.push(row.cmpy);
        this.deletePross(idArr);
      },
      //删除处理
      deletePross(idArr) {
        this.szdPopDel().then(() => {
          delCompany(idArr).then(res => {
            if (res.code === 200) {
              this.getList();
              this.$mS("删除成功");
            }
          });
        });
      }
    }
  };
</script>