<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <!-- 功能按钮 -->
    <div class="main-table-title" style="height: 35px">
      <div class="main-table-title-left" style="width: 50%">
      </div>
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
    <el-table ref="refTable" border stripe :data="objData.saleList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">
          {{ scope.row.itemNo }}
        </template>
      </el-table-column>
      <el-table-column label="销售组织" prop="saleOrg" min-width="200">
        <template v-slot:header>
          <szd-table-col-header required label="销售组织" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.saleOrg"
            baseField="Z000_MDM_ORG_SALE"
            :szdopt="SR.szdopt" clearable/>
        </template>
      </el-table-column>
      <el-table-column label="分销渠道" prop="vtweg" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="分销渠道" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.vtweg" :szdopt="SR.szdopt" :maxlength="2" />
        </template>
      </el-table-column>
      <el-table-column label="产品组" prop="spart" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="产品组" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.spart" :szdopt="SR.szdopt" :maxlength="2" />
        </template>
      </el-table-column>
      <el-table-column label="币种" prop="curr" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="币种" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.curr"
            baseField="CORE_CURR"
            :szdopt="SR.szdopt" clearable/>
        </template>
      </el-table-column>
      <el-table-column prop="priceTax" label="定价含税" min-width="120" align="center">
        <template v-slot:header>
          <szd-table-col-header label="定价含税" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.priceTax"
            baseField="Z000_MDM_PRICE_TAX"
            :szdopt="SR.szdopt" clearable/>
        </template>
      </el-table-column>
      <el-table-column label="客户分类" prop="custClass" min-width="150">
        <template v-slot:header>
          <szd-table-col-header label="客户分类" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.custClass"
            baseField="Z000_MDM_CUST_CLASS"
            :szdopt="SR.szdopt" clearable/>
        </template>
      </el-table-column>
      <el-table-column label="客户二级分类" prop="custClassSub" min-width="150">
        <template v-slot:header>
          <szd-table-col-header label="客户二级分类" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.custClassSub"
            baseField="Z000_MDM_CUST_CLASS_SUB"
            :szdopt="SR.szdopt" clearable />
        </template>
      </el-table-column>
      <el-table-column label="付款条件" prop="payTerms" min-width="250">
        <template v-slot:header>
          <szd-table-col-header label="付款条件" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.payTerms"
            baseField="Z000_MDM_PAYTERMS_K"
            :szdopt="SR.szdopt" clearable/>
        </template>
      </el-table-column>
      <el-table-column label="科目分配组" prop="accsGrp" min-width="150">
        <template v-slot:header>
          <szd-table-col-header label="科目分配组" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.accsGrp"
            baseField="Z000_MDM_ACCS_GRP"
            :szdopt="SR.szdopt"
            clearable
            @change="accsGrpChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="国际贸易条款" prop="incoterms" min-width="150">
        <template v-slot:header>
          <szd-table-col-header label="国际贸易条款" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.incoterms"
            baseField="Z000_MDM_INCOTERMS"
            :szdopt="SR.szdopt"
            :disabled="scope.row.accsGrp === '10'" clearable/>
        </template>
      </el-table-column>
      <el-table-column label="国际贸易条款1" prop="incoterms1" min-width="150">
        <template v-slot:header>
          <szd-table-col-header label="国际贸易条款1" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.incoterms1" :szdopt="SR.szdopt" :maxlength="32"
                     :disabled="scope.row.accsGrp === '10'" />
        </template>
      </el-table-column>
      <el-table-column label="销售部门" prop="saleDept" min-width="150">
        <template v-slot:header>
          <szd-table-col-header label="销售部门" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.saleDept"
            baseField="Z000_MDM_SALE_DEPT"
            :szdopt="SR.szdopt" clearable/>
        </template>
      </el-table-column>
<!--      <el-table-column label="销售大区" prop="saleRegion" min-width="200">-->
<!--        <template v-slot:header>-->
<!--          <szd-table-col-header required label="销售大区" />-->
<!--        </template>-->
<!--        <template v-slot="scope">-->
<!--          <szd-select-->
<!--            v-model="scope.row.saleRegion"-->
<!--            baseField="Z000_MDM_REGION"-->
<!--            :szdopt="SR.szdopt" />-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="装运条件" prop="shippingTerms" min-width="150">
        <template v-slot:header>
          <szd-table-col-header label="装运条件" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.shippingTerms"
            baseField="Z000_MDM_SHIPPING_TERMS"
            :szdopt="SR.szdopt" clearable/>
        </template>
      </el-table-column>
      <el-table-column label="信用控制范围" prop="creditCtrlArea" min-width="200">
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.creditCtrlArea"
            baseField="Z000_MDM_CREDIT_CTRL"
            :szdopt="SR.szdopt" clearable/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('操作')" align="center" width="100" fixed="right">
        <template v-slot="scope">
          <szd-button label="业务伙伴" type="primary" link @click="handlePartner(scope.row)" icon="Edit" />
        </template>
      </el-table-column>
    </el-table>
  </div>
  <custDgItemTab30Sub10 ref="custDgItemTab30Sub10" :SR="SR" :wfOper="wfOper" />
</template>

<script>
  import custDgItemTab30Sub10 from "./custDgItemTab30Sub10.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      custDgItemTab30Sub10
    },
    data() {
      return {
        listSel: [],
        updParams: {
          option: "C"
        }
      };
    },

    methods: {
      //选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      addRow() {
        this.updParams.option = "C";
        this.objData.saleList.push({
        });
        this.setItemNo();
      },
      //删除行
      delRow() {
        this.objData.saleList = this.objData.saleList.filter(item => !this.listSel.some(ele => ele === item));
      },
      //设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.saleList.length; i++) {
          this.objData.saleList[i].itemNo = i + 1;
        }
      },
      accsGrpChange(row) {
        if (row.accsGrp === "10") {
          row.incoterms = "EXW";
          row.incoterms1 = "工厂交货";
        } else {
          row.incoterms = "";
          row.incoterms1 = "";
        }
      },
      handlePartner(row) {
        row.applyId = this.objData.header.applyId;
        this.$refs.custDgItemTab30Sub10.acceptData(row);
      }
    }
  };
</script>
