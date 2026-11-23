<template>
  <div class="szd__tb__wrapper" style="padding-top: 5px">
    <!-- 功能按钮 -->
    <div class="szd__tb__top" v-if="['A', 'B', 'C'].includes(objData.header.bussStatus) && SR.szdopt === 'U'" style="justify-content: space-between">
      <div>
        <szd-checkbox-all v-model:data="objData.itemList" :szdopt="SR.szdopt" @selection-change="handleSelRow" />
      </div>
      <div>
        <szd-button label="新增" :szdopt="SR.szdopt" @click="addRow" />
        <szd-button label="删除" :szdopt="SR.szdopt" @click="delRow" />
      </div>
    </div>
    <!-- 行项目卡片列表 -->
    <div class="szd__tb__main">
      <szd-card-a v-for="row in objData.itemList" :key="row.itemNo" :footer="false">
        <template #header>
          <szd-card-row-title v-model="row.$SZDCHECKBOX" :index="row.itemNo" checkbox :btnDelete="SR.szdopt == 'U'" @delete="handleRowDelete(row)" />
        </template>
        <template #main>
          <szd-input
            label="物料编码"
            v-model="row.mtlCode"
            required
            showHelp
            :help="{ a: 'Z000_MDM_MTL', b: row, c: ['mtlCode-mtlCode', 'mtlName-mtlName'] }"
            :szdopt="SR.szdopt"
            :desc="row.mtlName" />
          <szd-input
            label="工厂"
            v-model="row.fact"
            required
            showHelp
            :help="{ a: 'CORE_FACT', b: row, c: ['fact-fact', 'factName-factName'] }"
            :szdopt="SR.szdopt"
            :desc="row.factName" />
          <szd-input label="采购订单数量" v-model="row.purNum" required :szdopt="SR.szdopt" :maxlength="13" @change="onPurNumChange(row)" />
          <szd-select label="订单单位" v-model="row.unit" baseField="CORE_UNIT" required :szdopt="SR.szdopt" />
          <szd-input label="采购申请编号" v-model="row.applyNo" :szdopt="SR.szdopt" disabled />
          <szd-input label="采购申请项目" v-model="row.applyNoItem" :szdopt="SR.szdopt" disabled />
          <szd-date label="交货日期" v-model="row.deliveryDate" required :szdopt="SR.szdopt" @change="onDeliveryDateChange(row)" />
          <szd-button label="交货计划" :szdopt="SR.szdopt" @click="openSchedPopup(row)" />
        </template>
      </szd-card-a>
    </div>

    <!-- 交货计划弹窗 -->
    <var-popup v-model:show="schedPopup.visible" position="bottom" :overlay="true" :style="{ height: '60vh' }">
      <div class="sched-popup">
        <div class="sched-popup-header">
          <h4>交货计划 - 行项目 {{ schedPopup.itemNo }}</h4>
          <var-button size="small" text @click="schedPopup.visible = false">关闭</var-button>
        </div>
        <!-- 交货计划操作按钮 -->
        <div class="sched-popup-actions" v-if="['A', 'B', 'C'].includes(objData.header.bussStatus) && SR.szdopt === 'U'">
          <szd-button label="新增行" :szdopt="SR.szdopt" @click="addSchedRow" />
          <szd-button label="删除行" :szdopt="SR.szdopt" @click="delSchedRow" />
        </div>
        <!-- 交货计划列表 -->
        <div class="sched-popup-main">
          <szd-card-a v-for="sched in currentSchedList" :key="sched.schedLine" :footer="false">
            <template #header>
              <szd-card-row-title v-model="sched.$SZDCHECKBOX" :index="sched.schedLine" checkbox @delete="handleSchedRowDelete(sched)" />
            </template>
            <template #main>
              <szd-date label="交货日期" v-model="sched.deliveryDate" required :szdopt="SR.szdopt" />
              <szd-input label="已计划数量" v-model="sched.quantity" required :szdopt="SR.szdopt" :maxlength="13" />
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
    components: {},
    data() {
      return {
        listSel: [],
        // 交货计划弹窗
        schedPopup: {
          visible: false,
          itemNo: ""
        },
        schedSel: []
      };
    },
    computed: {
      // 当前选中行项目的交货计划列表
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
      // ========== 行项目管理 ==========
      // 新增行
      addRow() {
        this.objData.itemList.push({
          itemNo: "",
          mtlCode: "",
          mtlName: "",
          fact: "",
          factName: "",
          purNum: "",
          unit: "",
          applyNo: "",
          applyNoItem: "",
          deliveryDate: ""
        });
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
      // 批量删除行（含关联交货计划）
      delRow() {
        if (this.listSel.length === 0) {
          this.$mW("请至少选择一行需要删除的数据");
          return;
        }
        // 删除关联的交货计划
        this.listSel.forEach(item => {
          if (this.objData.schedList) {
            this.objData.schedList = this.objData.schedList.filter(s => s.itemNo !== item.itemNo);
          }
        });
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      // 单行删除（含关联交货计划）
      handleRowDelete(row) {
        if (this.objData.schedList) {
          this.objData.schedList = this.objData.schedList.filter(s => s.itemNo !== row.itemNo);
        }
        this.objData.itemList = this.objData.itemList.filter(item => item !== row);
        this.setItemNo();
      },

      // ========== 交货日期/数量变更自动同步交货计划 ==========
      // 交货日期变更时自动同步交货计划
      onDeliveryDateChange(row) {
        if (!this.objData.schedList) {
          this.objData.schedList = [];
        }
        const scheds = this.objData.schedList.filter(s => s.itemNo === row.itemNo);
        // 清空交货日期 → 仅1条计划时自动删除
        if (!row.deliveryDate) {
          if (scheds.length === 1) {
            this.objData.schedList = this.objData.schedList.filter(s => s.itemNo !== row.itemNo);
          }
          return;
        }
        // 无计划 → 自动创建一条
        if (scheds.length === 0) {
          this.objData.schedList.push({
            orderId: this.objData.header.orderId || "",
            itemNo: row.itemNo,
            schedLine: 1,
            deliveryDate: row.deliveryDate,
            quantity: row.purNum || ""
          });
        } else if (scheds.length === 1) {
          // 仅1条计划 → 自动更新日期
          scheds[0].deliveryDate = row.deliveryDate;
        }
        // 多条计划时不自动处理
      },
      // 数量变更时自动同步交货计划数量
      onPurNumChange(row) {
        if (!this.objData.schedList) {
          return;
        }
        const scheds = this.objData.schedList.filter(s => s.itemNo === row.itemNo);
        // 仅1条计划时自动更新数量
        if (scheds.length === 1) {
          scheds[0].quantity = row.purNum || "";
        }
      },

      // ========== 交货计划弹窗 ==========
      // 打开交货计划弹窗
      openSchedPopup(row) {
        this.schedPopup.itemNo = row.itemNo;
        // 如果该行项目还没有交货计划，默认新增一行
        if (!this.objData.schedList) {
          this.objData.schedList = [];
        }
        const existing = this.objData.schedList.filter(s => s.itemNo === row.itemNo);
        if (existing.length === 0) {
          this.objData.schedList.push({
            orderId: this.objData.header.orderId || "",
            itemNo: row.itemNo,
            schedLine: 1,
            deliveryDate: row.deliveryDate || "",
            quantity: row.purNum || ""
          });
        }
        this.schedPopup.visible = true;
      },
      // 新增交货计划行
      addSchedRow() {
        if (!this.objData.schedList) {
          this.objData.schedList = [];
        }
        const existing = this.currentSchedList;
        const maxLine = existing.reduce((max, s) => {
          const n = parseInt(s.schedLine) || 0;
          return n > max ? n : max;
        }, 0);
        this.objData.schedList.push({
          orderId: this.objData.header.orderId || "",
          itemNo: this.schedPopup.itemNo,
          schedLine: maxLine + 1,
          deliveryDate: "",
          quantity: ""
        });
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
  .sched-popup {
    padding: 16px;
    height: 100%;
    display: flex;
    flex-direction: column;
    background: #fff;
  }

  .sched-popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 12px;
    border-bottom: 1px solid #eee;
  }

  .sched-popup-header h4 {
    margin: 0;
    font-size: 16px;
  }

  .sched-popup-actions {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    padding: 8px 0;
  }

  .sched-popup-main {
    flex: 1;
    overflow-y: auto;
    padding-top: 5px;
  }
</style>
