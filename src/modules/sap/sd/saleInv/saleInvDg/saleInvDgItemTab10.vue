<template>
  <div class="szd__tb__wrapper" style="padding-top: 5px">
    <div class="szd__tb__top" v-if="['A', 'B', 'C'].includes(objData.header.bussStatus) && SR.szdopt === 'U'" style="justify-content: space-between">
      <div><szd-checkbox-all v-model:data="objData.itemList" :szdopt="SR.szdopt" @selection-change="handleSelRow" /></div>
      <div><szd-button label="新增" :szdopt="SR.szdopt" @click="addRow" /><szd-button label="删除" :szdopt="SR.szdopt" @click="delRow" /></div>
    </div>
    <div class="szd__tb__main">
      <szd-card-a v-for="row in objData.itemList" :key="row.itemNo" :footer="false">
        <template #header><szd-card-row-title v-model="row.$SZDCHECKBOX" :index="row.itemNo" checkbox :btnDelete="SR.szdopt == 'U'" @delete="handleRowDelete(row)" /></template>
        <template #main>
          <szd-input label="交货单单号" v-model="row.deliveryBussDocId" disabled />
          <szd-input label="交货单号" v-model="row.vbelnVl" disabled required :szdopt="SR.szdopt" />
        </template>
      </szd-card-a>
    </div>
  </div>
</template>
<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() { return { listSel: [] }; },
    methods: {
      // 选择行
      handleSelRow(sel) { this.listSel = sel; },
      // 新增行
      addRow() { this.objData.itemList.push({ itemNo: "", vbelnVl: "", deliveryBussDocId: "" }); this.setItemNo(); },
      // 设置行号
      setItemNo() { for (let i = 0; i < this.objData.itemList.length; i++) this.objData.itemList[i].itemNo = i + 1; },
      // 批量删除行
      delRow() { if (!this.listSel.length) { this.$mW("请至少选择一行"); return; } this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item)); this.setItemNo(); },
      // 单行删除
      handleRowDelete(row) { this.objData.itemList = this.objData.itemList.filter(item => item !== row); this.setItemNo(); }
    }
  };
</script>
