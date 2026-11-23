<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <div class="main-table-title" style="height: 35px">
      <div class="main-table-title-left" style="width: 50%"></div>
      <div class="main-table-title-right" style="width: 50%; align-items: center">
        <szd-button
          v-if="['A'].includes(objData.header.bussStatus)"
          label="新增"
          type="primary"
          link
          @click="addRow"
          icon="Plus"
          :szdopt="SR.szdopt" />
        <szd-button
          v-if="['A'].includes(objData.header.bussStatus)"
          label="删除"
          type="primary"
          link
          @click="delRow"
          icon="Delete"
          :szdopt="SR.szdopt" />
      </div>
    </div>
    <el-table ref="refTable" border stripe :data="objData.itemList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column label="行项目" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">{{ scope.row.itemNo }}</template>
      </el-table-column>
      <el-table-column label="工厂" prop="fact" min-width="200">
        <template v-slot:header><szd-table-col-header required label="工厂" /></template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.fact"
            showHelp
            :help="{ a: 'CORE_FACT', b: scope.row, c: ['fact-fact', 'factName-factName'] }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.factName" />
        </template>
      </el-table-column>
      <el-table-column label="库位" prop="store" min-width="200">
        <template v-slot:header><szd-table-col-header required label="库位" /></template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.store"
            showHelp
            :help="{ a: 'CORE_STOR', b: scope.row, c: ['store-stor', 'storeName-storName'], d: [['fact', scope.row.fact]] }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.storeName" />
        </template>
      </el-table-column>
      <el-table-column label="任务状态" prop="taskStatus" min-width="120">
        <template v-slot:header><szd-table-col-header label="任务状态" /></template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.taskStatus" baseField="Z000_KCPD_TASK_STATUS" :szdopt="'V'" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  props: ["objData", "SR", "wfOper"],
  data() {
    return { listSel: [] };
  },
  methods: {
    handleSelRow(selection) { this.listSel = selection; },
    addRow() {
      this.objData.itemList.push({ itemNo: "", fact: "", factName: "", store: "", storeName: "", taskStatus: "" });
      this.setItemNo();
    },
    delRow() {
      this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
      this.setItemNo();
    },
    setItemNo() {
      for (let i = 0; i < this.objData.itemList.length; i++) {
        this.objData.itemList[i].itemNo = i + 1;
      }
    }
  }
};
</script>
