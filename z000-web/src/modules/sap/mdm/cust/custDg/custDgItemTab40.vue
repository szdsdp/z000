<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <!-- 功能按钮 -->
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
    <!-- 表单输出 -->
    <el-table ref="refTable" border stripe :data="objData.purchaseList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">
          {{ scope.row.itemNo }}
        </template>
      </el-table-column>
      <el-table-column label="采购组织" prop="purchaseOrg" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="采购组织" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.purchaseOrg"
            baseField="Z000_MDM_ORG_PUR"
            :szdopt="SR.szdopt"
            :disabled="this.objData.header.partnerType === '5000'" />
        </template>
      </el-table-column>
      <el-table-column label="订单货币" prop="curr" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="订单货币" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.curr" baseField="CORE_CURR" :szdopt="SR.szdopt" :disabled="this.objData.header.partnerType === '5000'" />
        </template>
      </el-table-column>
      <el-table-column label="付款条件" prop="payTerms" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="付款条件" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.payTerms"
            baseField="Z000_MDM_PAYTERMS_D"
            :szdopt="SR.szdopt"
            :disabled="this.objData.header.partnerType === '5000'" clearable/>
        </template>
      </el-table-column>
      <el-table-column label="成交方式" prop="dealType" min-width="150" v-if="false">
        <template v-slot:header>
          <szd-table-col-header label="成交方式" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.dealType" baseField="Z000_MDM_INCOTERMS" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="联系人" prop="contact" min-width="150">
        <template v-slot:header>
          <szd-table-col-header label="联系人" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.contact" :szdopt="SR.szdopt" :maxlength="32" />
        </template>
      </el-table-column>
      <el-table-column label="联系电话" prop="contactPhone" min-width="150">
        <template v-slot:header>
          <szd-table-col-header label="联系电话" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.contactPhone" :szdopt="SR.szdopt" :maxlength="32" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {
        listSel: []
      };
    },

    methods: {
      //选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      addRow() {
        this.objData.purchaseList.push({
          itemNo: 1,
          curr: "CNY",
          dealType: "",
          contact: "",
          contactPhone: ""
        });
        this.setItemNo();
      },
      //删除行
      delRow() {
        this.objData.purchaseList = this.objData.purchaseList.filter(item => !this.listSel.some(ele => ele === item));
      },
      //设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.purchaseList.length; i++) {
          this.objData.purchaseList[i].itemNo = i + 1;
        }
      }
    }
  };
</script>
