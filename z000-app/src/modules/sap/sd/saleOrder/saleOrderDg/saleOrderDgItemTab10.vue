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
          <szd-input label="物料编码" v-model="row.mtlCode" required showHelp :help="{ a: 'Z000_MDM_MTL', b: row, c: ['mtlCode-mtlCode', 'mtlName-mtlName'] }" :szdopt="SR.szdopt" :desc="row.mtlName" />
          <szd-input label="数量" v-model="row.entryQnt" required :szdopt="SR.szdopt" @change="onEntryQntChange(row)" />
          <szd-select label="单位" v-model="row.unit" baseField="CORE_UNIT" required :szdopt="SR.szdopt" />
          <szd-date label="交货日期" v-model="row.deliveryDate" required :szdopt="SR.szdopt" @change="onDeliveryDateChange(row)" />
          <var-button type="primary" size="small" @click="openSchedPopup(row)" style="margin: 8px 0">交货计划</var-button>
        </template>
      </szd-card-a>
    </div>
    <var-popup v-model:show="schedPopup.visible" position="bottom" :overlay="true" :style="{ height: '60vh' }">
      <div class="sched-popup">
        <div class="sched-popup-header"><h4>交货计划 - 行项目 {{ schedPopup.itemNo }}</h4><var-button size="small" text @click="schedPopup.visible = false">关闭</var-button></div>
        <div class="sched-popup-actions" v-if="['A', 'B', 'C'].includes(objData.header.bussStatus) && SR.szdopt === 'U'">
          <szd-button label="新增行" @click="addSchedRow" /><szd-button label="删除行" @click="delSchedRow" />
        </div>
        <div class="sched-popup-main">
          <szd-card-a v-for="sched in currentSchedList" :key="sched.schedNo" :footer="false">
            <template #header><szd-card-row-title v-model="sched.$SZDCHECKBOX" :index="sched.schedNo" checkbox @delete="handleSchedRowDelete(sched)" /></template>
            <template #main>
              <szd-date label="计划到货日期" v-model="sched.reqDate" required :szdopt="SR.szdopt" />
              <szd-input label="数量" v-model="sched.reqQty" required :szdopt="SR.szdopt" />
            </template>
          </szd-card-a>
          <szd-nothing v-if="currentSchedList.length === 0" text="暂无交货计划" />
        </div>
      </div>
    </var-popup>
  </div>
</template>
<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() { return { listSel: [], schedPopup: { visible: false, itemNo: "" } }; },
    computed: {
      currentSchedList() {
        if (!this.schedPopup.itemNo) {
          return [];
        }
        if (!this.objData.schedList) {
          this.objData.schedList = [];
        }
        return this.objData.schedList.filter(s => s.itemNo === this.schedPopup.itemNo);
      }
    },
    methods: {
      // 选择行
      handleSelRow(sel) { this.listSel = sel; },
      // 新增行
      addRow() { this.objData.itemList.push({ itemNo: "", mtlCode: "", mtlName: "", entryQnt: "", unit: "", deliveryDate: "" }); this.setItemNo(); },
      // 设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      },
      // 批量删除行
      delRow() {
        if (!this.listSel.length) {
          this.$mW("请至少选择一行");
          return;
        }
        this.listSel.forEach(item => {
          if (this.objData.schedList) {
            this.objData.schedList = this.objData.schedList.filter(s => s.itemNo !== item.itemNo);
          }
        });
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      // 单行删除
      handleRowDelete(row) {
        if (this.objData.schedList) {
          this.objData.schedList = this.objData.schedList.filter(s => s.itemNo !== row.itemNo);
        }
        this.objData.itemList = this.objData.itemList.filter(item => item !== row);
        this.setItemNo();
      },
      // 交货日期变更时自动同步交货计划
      onDeliveryDateChange(row) {
        if (!this.objData.schedList) {
          this.objData.schedList = [];
        }
        const scheds = this.objData.schedList.filter(s => s.itemNo === row.itemNo);
        if (!row.deliveryDate) {
          if (scheds.length === 1) {
            this.objData.schedList = this.objData.schedList.filter(s => s.itemNo !== row.itemNo);
          }
          return;
        }
        if (scheds.length === 0) {
          this.objData.schedList.push({ itemNo: row.itemNo, schedNo: 1, reqDate: row.deliveryDate, reqQty: row.entryQnt || "" });
        } else if (scheds.length === 1) {
          scheds[0].reqDate = row.deliveryDate;
        }
      },
      // 数量变更时自动同步交货计划数量
      onEntryQntChange(row) {
        if (!this.objData.schedList) {
          return;
        }
        const scheds = this.objData.schedList.filter(s => s.itemNo === row.itemNo);
        if (scheds.length === 1) {
          scheds[0].reqQty = row.entryQnt || "";
        }
      },
      // 打开交货计划弹窗
      openSchedPopup(row) {
        this.schedPopup.itemNo = row.itemNo;
        if (!this.objData.schedList) {
          this.objData.schedList = [];
        }
        if (this.objData.schedList.filter(s => s.itemNo === row.itemNo).length === 0) {
          this.objData.schedList.push({ itemNo: row.itemNo, schedNo: 1, reqDate: row.deliveryDate || "", reqQty: row.entryQnt || "" });
        }
        this.schedPopup.visible = true;
      },
      // 新增交货计划行
      addSchedRow() {
        if (!this.objData.schedList) {
          this.objData.schedList = [];
        }
        const existing = this.currentSchedList;
        const maxNo = existing.reduce((m, s) => {
          const n = parseInt(s.schedNo) || 0;
          return n > m ? n : m;
        }, 0);
        this.objData.schedList.push({ itemNo: this.schedPopup.itemNo, schedNo: maxNo + 1, reqDate: "", reqQty: "" });
      },
      // 删除选中的交货计划行
      delSchedRow() {
        if (!this.objData.schedList) {
          return;
        }
        this.objData.schedList = this.objData.schedList.filter(s => !this.currentSchedList.some(ele => ele === s && ele.$SZDCHECKBOX));
      },
      // 单行删除交货计划
      handleSchedRowDelete(sched) {
        if (!this.objData.schedList) {
          return;
        }
        this.objData.schedList = this.objData.schedList.filter(s => s !== sched);
      }
    }
  };
</script>
<style scoped>
  .sched-popup { padding: 16px; height: 100%; display: flex; flex-direction: column; background: #fff; }
  .sched-popup-header { display: flex; justify-content: space-between; align-items: center; padding-bottom: 12px; border-bottom: 1px solid #eee; }
  .sched-popup-header h4 { margin: 0; font-size: 16px; }
  .sched-popup-actions { display: flex; justify-content: flex-end; gap: 8px; padding: 8px 0; }
  .sched-popup-main { flex: 1; overflow-y: auto; padding-top: 5px; }
</style>
