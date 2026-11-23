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
          <szd-input label="订单号" v-model="row.orderNo" disabled required :szdopt="SR.szdopt" />
          <szd-input label="订单项目编号" v-model="row.orderItemNo" disabled required :szdopt="SR.szdopt" />
          <szd-input label="物料编码" v-model="row.mtlCode" :szdopt="SR.szdopt" disabled />
          <szd-input label="物料名称" v-model="row.mtlName" :szdopt="SR.szdopt" disabled />
          <szd-select label="订单单位" v-model="row.unit" baseField="CORE_UNIT" required :szdopt="SR.szdopt" />
          <szd-input
            label="税码"
            v-model="row.taxCode"
            required
            showHelp
            :help="{ a: 'CORE_TAXR', b: row, c: ['taxCode-taxrJ'] }"
            :szdopt="SR.szdopt" />
          <szd-input label="金额" v-model="row.amt" required :szdopt="SR.szdopt" type="number" @change="$emit('update-gross')" />
          <szd-input label="税收金额" v-model="row.taxAmount" :szdopt="SR.szdopt" type="number" @change="$emit('update-gross')" />
          <szd-input label="数量" v-model="row.quantity" required :szdopt="SR.szdopt" type="number" @change="handleQtyChange(row)" />
          <szd-input label="项目文本" v-model="row.itemText" :szdopt="SR.szdopt" :maxlength="50" />
        </template>
      </szd-card-a>
    </div>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    emits: ["update-gross"],
    data() {
      return {
        listSel: []
      };
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
          orderNo: "",
          orderItemNo: "",
          mtlCode: "",
          mtlName: "",
          unit: "",
          taxCode: "",
          amt: "",
          taxAmount: "",
          quantity: "",
          itemText: "",
          mmenge: "0",
          fmenge: "0"
        });
        this.setItemNo();
      },
      // 设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      },
      // 数量变更校验：不能超过剩余未发票校验数量
      handleQtyChange(row) {
        const mmenge = parseFloat(row.mmenge) || 0;
        const fmenge = parseFloat(row.fmenge) || 0;
        const remaining = mmenge - fmenge;
        const qty = parseFloat(row.quantity) || 0;
        if (qty > remaining) {
          this.$mW(`第${row.itemNo}行数量不能超过剩余未发票校验数量(${remaining})`);
          this.$nextTick(() => {
            row.quantity = "";
          });
        }
      },
      // 批量删除行
      delRow() {
        if (this.listSel.length === 0) {
          this.$mW("请至少选择一行需要删除的数据");
          return;
        }
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
        this.$emit("update-gross");
      },
      // 单行删除
      handleRowDelete(row) {
        this.objData.itemList = this.objData.itemList.filter(item => item !== row);
        this.setItemNo();
        this.$emit("update-gross");
      }
    }
  };
</script>
