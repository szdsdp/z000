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
          <szd-input label="短文本" v-model="row.itemText" required :szdopt="SR.szdopt" :maxlength="40" />
          <szd-input
            label="工厂"
            v-model="row.fact"
            required
            showHelp
            :help="{ a: 'CORE_FACT', b: row, c: ['fact-fact', 'factName-factName'] }"
            :szdopt="SR.szdopt"
            :desc="row.factName" />
          <szd-input label="目标数量" v-model="row.quantity" required :szdopt="SR.szdopt" :maxlength="13" />
          <szd-select label="订单单位" v-model="row.unit" baseField="CORE_UNIT" required :szdopt="SR.szdopt" />
          <szd-input label="净价" v-model="row.price" required :szdopt="SR.szdopt" :maxlength="28" />
          <szd-input label="价格单位" v-model="row.priceUnit" :szdopt="SR.szdopt" :maxlength="5" />
          <szd-input label="库存地点" v-model="row.store" :szdopt="SR.szdopt" :maxlength="4" />
          <szd-input label="发货存储地点" v-model="row.sendStore" :szdopt="SR.szdopt" :maxlength="4" />
          <szd-select label="物料组" v-model="row.mtlGroup" baseField="Z000_MDM_MTL_GROUP" :szdopt="SR.szdopt" />
          <szd-input label="需求跟踪号" v-model="row.needNo" :szdopt="SR.szdopt" :maxlength="10" />
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
          mtlCode: "",
          mtlName: "",
          itemText: "",
          fact: "",
          factName: "",
          quantity: "",
          unit: "",
          price: "",
          priceUnit: "",
          store: "",
          sendStore: "",
          mtlGroup: "",
          needNo: ""
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
