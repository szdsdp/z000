<template>
  <div class="szd__tb__wrapper" style="padding-top: 5px">
    <!-- 功能按钮 -->
    <div class="szd__tb__top" v-if="['A', 'B', 'C'].includes(objData.header.bussStatus) && SR.szdopt === 'U'" style="justify-content: space-between">
      <div>
        <szd-checkbox-all v-model:data="objData.itemList" :szdopt="SR.szdopt" @selection-change="handleSelRow" />
      </div>
      <div>
        <szd-button v-if="objData.header.bstp === 'Z000_MM_CGRK01'" label="新增" :szdopt="SR.szdopt" @click="addRow" />
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
          <szd-input label="前置单据业务单号" v-model="row.refBussDocId" :szdopt="SR.szdopt" disabled />
          <szd-input label="订单号" v-model="row.orderNo" disabled required :szdopt="SR.szdopt" />
          <szd-input label="订单项目编号" v-model="row.orderItemNo" disabled required :szdopt="SR.szdopt" />
          <szd-input label="订单行数量" v-model="row.orderQty" :szdopt="SR.szdopt" disabled />
          <szd-input
            label="物料编码"
            v-model="row.mtlCode"
            required
            showHelp
            :help="{ a: 'Z000_MDM_MTL', b: row, c: ['mtlCode-mtlCode', 'mtlName-mtlName'] }"
            :szdopt="SR.szdopt"
            :desc="row.mtlName" />
          <szd-select label="单位" v-model="row.unit" baseField="CORE_UNIT" required :szdopt="SR.szdopt" />
          <szd-input
            label="工厂"
            v-model="row.fact"
            required
            showHelp
            :help="{ a: 'CORE_FACT', b: row, c: ['fact-fact', 'factName-factName'] }"
            :szdopt="SR.szdopt"
            :desc="row.factName" />
          <szd-input
            label="库存地点"
            v-model="row.store"
            required
            showHelp
            :help="{ a: 'CORE_STOR', b: row, c: ['store-stor', 'storeName-storName'], d: [['fact', row.fact]] }"
            :szdopt="SR.szdopt"
            :desc="row.storeName" />
          <szd-input label="数量" v-model="row.quantity" required :szdopt="SR.szdopt" type="number" />
          <szd-input label="项目文本" v-model="row.itemText" :szdopt="SR.szdopt" :maxlength="50" />
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
      // 选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 新增行
      addRow() {
        this.objData.itemList.push({
          itemNo: "",
          refBussDocId: "",
          orderNo: "",
          orderItemNo: "",
          orderQty: "",
          mtlCode: "",
          mtlName: "",
          unit: "",
          fact: "",
          factName: "",
          store: "",
          storeName: "",
          quantity: "",
          itemText: ""
        });
        this.setItemNo();
      },
      // 设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
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
      },
      // 单行删除
      handleRowDelete(row) {
        this.objData.itemList = this.objData.itemList.filter(item => item !== row);
        this.setItemNo();
      }
    }
  };
</script>
