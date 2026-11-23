<template>
  <div class="szd__tb__wrapper" style="padding-top: 5px">
    <!-- 功能按钮 -->
    <div class="szd__tb__top" v-if="['A'].includes(objData.header.bussStatus) && SR.szdopt === 'U'" style="justify-content: space-between">
      <div>
        <szd-checkbox-all v-model:data="objData.itemList" :szdopt="SR.szdopt" @selection-change="handleSelRow" />
      </div>
      <div>
        <szd-button label="新增" :szdopt="SR.szdopt" @click="addRow" />
        <szd-button label="删除" :szdopt="SR.szdopt" @click="delRow" />
      </div>
    </div>
    <!-- 表格输出 -->
    <div class="szd__tb__main">
      <szd-card-a v-for="row in objData.itemList" :key="row.itemNo" :footer="false">
        <template #header>
          <szd-card-row-title v-model="row.$SZDCHECKBOX" :index="row.itemNo" checkbox :btnDelete="SR.szdopt == 'U'" @delete="handleRowDelete(row)" />
        </template>
        <template #main>
          <szd-input
            label="工厂"
            v-model="row.fact"
            required
            showHelp
            :help="{ a: 'CORE_FACT', b: row, c: ['fact-fact', 'factName-factName'] }"
            :szdopt="SR.szdopt"
            :desc="row.factName" />
          <szd-input
            label="库位"
            v-model="row.store"
            required
            showHelp
            :help="{ a: 'CORE_STOR', b: row, c: ['store-stor', 'storeName-storName'], d: [['fact', row.fact]] }"
            :szdopt="SR.szdopt"
            :desc="row.storeName" />
          <szd-select label="任务状态" v-model="row.taskStatus" baseField="Z000_KCPD_TASK_STATUS" :szdopt="'V'" />
        </template>
      </szd-card-a>
    </div>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    components: {},
    data() {
      return {
        listSel: []
      };
    },
    methods: {
      // 新增行
      addRow() {
        this.objData.itemList.push({ itemNo: "", fact: "", factName: "", store: "", storeName: "", taskStatus: "" });
        this.setItemNo();
      },
      // 设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      },
      // 选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 批量删除行
      delRow() {
        if (this.listSel.length === 0) {
          this.$mW("请至少选择一行需要删除的数据");
          return;
        }
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      // 单行删除
      handleRowDelete(row) {
        this.objData.itemList = this.objData.itemList.filter(item => item !== row);
        this.setItemNo();
      }
    }
  };
</script>
