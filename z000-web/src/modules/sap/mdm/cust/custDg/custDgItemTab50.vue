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
    <el-table ref="refTable" border stripe :data="objData.creditList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">
          {{ scope.row.itemNo }}
        </template>
      </el-table-column>
      <el-table-column label="信用段" prop="creditSgmnt" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="信用段" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.creditSgmnt"
            baseField="Z000_MDM_CREDIT_SGMNT"
            :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="信用额度" prop="creditLimit" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="信用额度" />
        </template>
        <template v-slot="scope">
          <szd-input
            v-model="scope.row.creditLimit"
            type="number"
            numDot
            :numDec="2"
            :numMin="0"
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
        this.objData.creditList.push({
          itemNo: 1,
          creditSgmnt: "",
          creditL_limit: 0,
        });
        this.setItemNo();
      },
      //删除行
      delRow() {
        this.objData.creditList = this.objData.creditList.filter(item => !this.listSel.some(ele => ele === item));
      },
      //设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.creditList.length; i++) {
          this.objData.creditList[i].itemNo = i + 1;
        }
      },
    }
  };
</script>
