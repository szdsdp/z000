<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <div class="main-table-title" style="height: 35px">
      <div class="main-table-title-left" style="width: 50%"></div>
      <div class="main-table-title-right" style="width: 50%; align-items: center">
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="新增"
          type="primary"
          link
          @click="addRow"
          icon="Plus"
          :szdopt="SR.szdopt" />
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
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
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">{{ scope.row.itemNo }}</template>
      </el-table-column>
      <el-table-column label="物料编号" prop="mtlCode" min-width="250">
        <template v-slot:header>
          <szd-table-col-header required label="物料编号" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.mtlCode"
            showHelp
            :help="{
              a: 'Z000_MDM_MTL',
              b: scope.row,
              c: ['mtlCode', 'mtlName','unit']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.mtlName" />
        </template>
      </el-table-column>

      <el-table-column label="采购申请数量" prop="applyNum" min-width="130">
        <template v-slot:header>
          <szd-table-col-header required label="采购申请数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.applyNum" :szdopt="SR.szdopt" :maxlength="13" />
        </template>
      </el-table-column>
      <el-table-column label="计量单位" prop="unit" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="计量单位" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.unit" baseField="CORE_UNIT" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="工厂" prop="fact" min-width="200">
        <template v-slot:header>
          <szd-table-col-header required label="工厂" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.fact"
            showHelp
            :help="{
              a: 'CORE_FACT',
              b: scope.row,
              c: ['fact-fact', 'factName-factName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.factName" />
        </template>
      </el-table-column>
      <el-table-column label="需求日期" prop="needDate" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="需求日期" required />
        </template>
        <template v-slot="scope">
          <szd-date v-model="scope.row.needDate" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="交货日期" prop="deliveryDate" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="交货日期" required />
        </template>
        <template v-slot="scope">
          <szd-date v-model="scope.row.deliveryDate" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="库存地点" prop="store" min-width="200">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.store"
            showHelp
            :help="{
              a: 'CORE_STOR',
              b: scope.row,
              c: ['store-stor', 'storeName-storName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.storeName" />
        </template>
      </el-table-column>
      <el-table-column label="物料组" prop="mtlGroup" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="物料组" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.mtlGroup" baseField="Z000_MDM_MTL_GROUP" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="采购组" prop="purGroup" min-width="120">
        <template v-slot="scope">
          <szd-select v-model="scope.row.purGroup" baseField="Z000_MM_CGDD_CGZ" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="需求跟踪号" prop="needNo" min-width="140">
        <template v-slot:header>
          <szd-table-col-header label="需求跟踪号" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.needNo" :szdopt="SR.szdopt" :maxlength="10" />
        </template>
      </el-table-column>
      <el-table-column label="需求者姓名" prop="needUser" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="需求者姓名" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.needUser" :szdopt="SR.szdopt" :maxlength="12" />
        </template>
      </el-table-column>
      <el-table-column label="短文本" prop="itemText" min-width="200">
        <template v-slot="scope">
          <szd-input v-model="scope.row.itemText" :szdopt="SR.szdopt" :maxlength="40" />
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
      handleSelRow(selection) {
        this.listSel = selection;
      },
      addRow() {
        this.objData.itemList.push({
          itemNo: "",
          purGroup: "",
          needUser: "",
          itemText: "",
          mtlCode: "",
          fact: "",
          store: "",
          needNo: "",
          mtlGroup: "",
          applyNum: "",
          unit: "",
          needDate: "",
          deliveryDate: ""
        });
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
