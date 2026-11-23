<template>
  <div class="szd__tb__wrapper" style="padding-top: 5px">
    <!-- 功能按钮 -->
    <div class="szd__tb__top" v-if="['A', 'B', 'C'].includes(objData.header.bussStatus) && SR.szdopt === 'U'" style="justify-content: space-between">
      <div>
        <szd-button label="扫码" :szdopt="SR.szdopt" @click="handleScanOpen('A')" />
      </div>
    </div>
    <!-- 表格输出 -->
    <div class="szd__tb__main">
      <szd-card-a v-for="row in objData.itemList" :key="row.itemNo" :footer="false">
        <template #header>
          <szd-card-row-title :index="row.itemNo" />
        </template>
        <template #main>
          <szd-input label="物料编码" v-model="row.mtlCode" :desc="row.mtlName" disabled />
          <szd-input v-if="!isBlind" label="台账数量" v-model="row.sysQnt" disabled type="number" numDot :numDec="2" />
          <szd-input label="盘点数量" v-model="row.countQnt" :szdopt="SR.szdopt" required type="number" numDot :numDec="2" @change="calcDiff(row)" />
          <szd-input label="差异数量" v-model="row.diffQnt" disabled type="number" numDot :numDec="2" />
          <szd-select v-if="!isBlind" label="盘点状态" v-model="row.countStatus" baseField="Z000_KCPD_COUNT_STATUS" :szdopt="'V'" />
          <szd-input v-if="!isBlind" label="差异说明" v-model="row.diffDesc" :szdopt="SR.szdopt" :maxlength="200" />
          <szd-select label="单位" v-model="row.unit" baseField="CORE_UNIT" :szdopt="SR.szdopt" />
          <szd-input
            label="盘点人"
            v-model="row.counter"
            required
            showHelp
            :help="{ a: 'CORE_USER', b: row, c: ['counter-userId', 'counterName-userName'] }"
            :szdopt="SR.szdopt"
            :desc="row.counterName" />
          <szd-input label="备注" v-model="row.itemText" :szdopt="SR.szdopt" :maxlength="200" />
        </template>
      </szd-card-a>
    </div>
    <!-- 扫描组件 -->
    <szd-scan-popup v-model:show="dg.scanShow" @scan="handleScan" @scanEvery="handleScanEvery" :mode="dg.scanMode" />
  </div>
</template>

<script>
  import { updateItems } from "../api/checkTask.js";

  export default {
    props: ["objData", "SR", "wfOper"],
    inject: ["isBlind"],
    components: {},
    data() {
      return {
        dg: {
          scanShow: false,
          scanMode: ""
        },
        // 本次扫描累计变化行
        scanChangedItems: []
      };
    },
    methods: {
      // 打开扫描
      handleScanOpen(mode) {
        this.dg.scanMode = mode;
        this.scanChangedItems = [];
        this.dg.scanShow = true;
      },
      // 每次扫描调用：累加盘点数量，参数code为扫描结果字符串
      handleScanEvery(code) {
        if (!code) {
          return;
        }
        const matchedItem = this.objData.itemList.find(item => item.mtlCode && item.mtlCode === code);
        if (matchedItem) {
          // 盘点数量+1，同步计算差异
          const currentQty = parseFloat(matchedItem.countQnt) || 0;
          matchedItem.countQnt = (currentQty + 1).toString();
          this.calcDiff(matchedItem);
          // 记录变化行（itemNo去重）
          if (!this.scanChangedItems.some(r => r.itemNo === matchedItem.itemNo)) {
            this.scanChangedItems.push(matchedItem);
          }
        }
      },
      // 关闭扫描时调用：保存所有变化行
      handleScan() {
        if (this.scanChangedItems.length > 0) {
          updateItems(this.scanChangedItems).catch(() => {});
          this.$mS(`已保存${this.scanChangedItems.length}行盘点数据`);
        } else {
          this.$mW("未找到匹配的物料");
        }
      },
      // 计算差异数量
      calcDiff(row) {
        const sysQnt = parseFloat(row.sysQnt) || 0;
        const countQnt = parseFloat(row.countQnt) || 0;
        row.diffQnt = countQnt - sysQnt;
        if (row.diffQnt > 0) {
          row.countStatus = "02";
          row.moveType = "701";
        } else if (row.diffQnt < 0) {
          row.countStatus = "03";
          row.moveType = "702";
        } else {
          row.countStatus = "01";
          row.moveType = "";
        }
      }
    }
  };
</script>
