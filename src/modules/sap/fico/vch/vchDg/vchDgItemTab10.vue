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
            label="业务小类"
            v-model="row.bsubl"
            required
            showHelp
            :help="{ a: 'CORE_BSUB01', b: row, c: ['bsubl-bsub', 'bsublName-bsubName'] }"
            :szdopt="SR.szdopt"
            :desc="row.bsublName" />
          <szd-select label="抵扣" v-model="row.deduFlag" baseField="Z000_DEDU" :szdopt="SR.szdopt" />
          <szd-select label="税率" v-model="row.taxr" baseField="CORE_TAXR" required :szdopt="SR.szdopt" @change="calcAmtFromTaxr(row)" />
          <szd-input label="税额" v-model="row.amtTax" :szdopt="SR.szdopt" type="number" @change="calcAmtSum(row)" />
          <szd-input label="不含税金额" v-model="row.amtNoTax" required :szdopt="SR.szdopt" type="number" @change="calcAmtFromTaxr(row)" />
          <szd-input label="含税金额" v-model="row.amt" disabled type="number" />
        </template>
      </szd-card-a>
    </div>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return { listSel: [] };
    },
    methods: {
      // 选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 新增行
      addRow() {
        this.objData.itemList.push({
          itemNo: "",
          bsubl: "",
          bsublName: "",
          deduFlag: "",
          taxr: "",
          amt: "",
          amtTax: "",
          amtNoTax: ""
        });
        this.setItemNo();
      },
      // 批量删除行
      delRow() {
        if (this.listSel.length === 0) {
          this.$mW("请至少选择一行需要删除的数据");
          return;
        }
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
        this.calcAmtHeader();
      },
      // 单行删除
      handleRowDelete(row) {
        this.objData.itemList = this.objData.itemList.filter(item => item !== row);
        this.setItemNo();
        this.calcAmtHeader();
      },
      // 设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      },
      // 税率/不含税金额变更 → 税额 = 不含税金额 × 税率% → 含税金额 = 不含税金额 + 税额
      calcAmtFromTaxr(row) {
        const amtNoTax = Number(row.amtNoTax) || 0;
        const taxr = Number(row.taxr) || 0;
        if (amtNoTax > 0 && taxr > 0) {
          row.amtTax = ((amtNoTax * taxr) / 100).toFixed(2);
        }
        const amtTax = Number(row.amtTax) || 0;
        if (amtNoTax > 0 || amtTax > 0) {
          row.amt = (amtNoTax + amtTax).toFixed(2);
        } else {
          row.amt = "";
        }
        this.calcAmtHeader();
      },
      // 手动修改税额 → 含税金额 = 不含税金额 + 税额
      calcAmtSum(row) {
        const amtNoTax = Number(row.amtNoTax) || 0;
        const amtTax = Number(row.amtTax) || 0;
        if (amtNoTax > 0 || amtTax > 0) {
          row.amt = (amtNoTax + amtTax).toFixed(2);
        } else {
          row.amt = "";
        }
        this.calcAmtHeader();
      },
      // 抬头申请金额汇总（按含税金额汇总）
      calcAmtHeader() {
        let total = 0;
        for (let i = 0; i < this.objData.itemList.length; i++) {
          total += Number(this.objData.itemList[i].amt) || 0;
        }
        this.objData.header.amtApply = total.toFixed(2);
      }
    }
  };
</script>
