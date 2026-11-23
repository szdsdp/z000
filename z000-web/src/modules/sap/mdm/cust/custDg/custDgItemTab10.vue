<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <!-- 功能按钮 -->
    <div class="main-table-title" style="height: 35px">
      <div class="main-table-title-left" style="width: 50%">
      </div>
      <div class="main-table-title-right" style="width: 50%; align-items: center">
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="新增"
          type="primary"
          link
          @click="addRow"
          icon="Plus"
          :szdopt="SR.szdopt" />
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="删除"
          type="primary"
          link
          @click="delRow"
          icon="Delete"
          :szdopt="SR.szdopt" />
      </div>
    </div>
    <!-- 表单输出 -->
    <el-table ref="refTable" border stripe :data="objData.cmpyKList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">
          {{ scope.row.itemNo }}
        </template>
      </el-table-column>
      <el-table-column label="公司代码" prop="cmpy" min-width="200">
        <template v-slot:header>
          <szd-table-col-header required label="公司代码" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.cmpy"
            baseField="CORE_CMPY"
            :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="客户统驭科目" prop="accs" min-width="200">
        <template v-slot:header>
          <szd-table-col-header required label="客户统驭科目" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.accs"
            baseField="Z000_MDM_ACCS_K"
            :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="客户付款条件" prop="payTerms" min-width="250">
        <template v-slot:header>
          <szd-table-col-header required label="客户付款条件" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.payTerms"
            baseField="Z000_MDM_PAYTERMS_K"
            :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="客户付款方式" prop="payType" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="客户付款方式" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.payType"
            baseField="Z000_MDM_PAYTYPE"
            :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {
        listSel: []
      };
    },

    methods: {
      //选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      addRow() {
        this.objData.cmpyKList.push({
          itemNo: 1,
          cmpy: "",
          payTerms: "",
          payType: "",
        });
        this.setItemNo();
      },
      //删除行
      delRow() {
        this.objData.cmpyKList = this.objData.cmpyKList.filter(item => !this.listSel.some(ele => ele === item));
      },
      //设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.cmpyKList.length; i++) {
          this.objData.cmpyKList[i].itemNo = i + 1;
        }
      },
    }
  };
</script>
