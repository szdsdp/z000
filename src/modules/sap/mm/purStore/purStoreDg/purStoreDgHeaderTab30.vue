<template>
  <!-- 关联单据 -->
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <el-table border stripe :data="objData.relateList" class="flex-1">
      <el-table-column label="序号" type="index" width="150" align="center" />
      <el-table-column label="单据号" prop="bussDocId" min-width="150" align="center">
        <template v-slot="scope">
          <el-button type="primary" link @click="handleGoToNav(scope.row)">
            {{ scope.row.bussDocId }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column :label="$t('单据类型')" prop="bussDocType" min-width="200" align="center" />
      <el-table-column :label="$t('业务状态')" prop="bussStatusName" min-width="120" align="center" />
    </el-table>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    data() {
      return {};
    },
    methods: {
      handleGoToNav(row) {
        this.szdWf.goDoc(row.routerCode, row.bussId, "V");
      }
    }
  };
</script>
