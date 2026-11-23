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
    <el-table ref="refTable" border stripe :data="objData.adjustList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column label="行项目" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">
          {{ scope.row.itemNo }}
        </template>
      </el-table-column>
      <el-table-column label="会计科目" prop="accs" min-width="200">
        <template v-slot:header>
          <szd-table-col-header label="会计科目" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.accs"
            showHelp
            :help="{
              a: 'CORE_ACCS',
              b: scope.row,
              c: ['accs-accs', 'accsName-accsName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.accsName" />
        </template>
      </el-table-column>
      <el-table-column label="借贷" prop="drCr" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="借贷" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.drCr" baseField="CORE_VCH_ACCSH" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="金额" prop="amt" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="金额" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.amt" :szdopt="SR.szdopt" type="number" numDot :numDec="2" />
        </template>
      </el-table-column>
      <el-table-column label="供应商" prop="supp" min-width="200">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.supp"
            showHelp
            :help="{
              a: 'CORE_SUPP',
              b: scope.row,
              c: ['supp-supp', 'suppName-suppName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.suppName" />
        </template>
      </el-table-column>
      <el-table-column label="客户" prop="cust" min-width="200">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.cust"
            showHelp
            :help="{
              a: 'CORE_CUST',
              b: scope.row,
              c: ['cust-cust', 'custName-custName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.custName" />
        </template>
      </el-table-column>
      <el-table-column label="特别总账标识" prop="sgl" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.sgl" :szdopt="SR.szdopt" :maxlength="10" />
        </template>
      </el-table-column>
      <el-table-column label="摘要" prop="summary" min-width="150">
        <template v-slot="scope">
          <szd-input v-model="scope.row.summary" :szdopt="SR.szdopt" :maxlength="200" />
        </template>
      </el-table-column>
      <el-table-column label="成本中心" prop="cstc" min-width="200">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.cstc"
            showHelp
            :help="{
              a: 'CORE_CSTC',
              b: scope.row,
              c: ['cstc-cstc', 'cstcName-cstcName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.cstcName" />
        </template>
      </el-table-column>
      <el-table-column label="利润中心" prop="prfc" min-width="200">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.prfc"
            showHelp
            :help="{
              a: 'CORE_PRFC',
              b: scope.row,
              c: ['prfc-prfc', 'prfcName-prfcName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.prfcName" />
        </template>
      </el-table-column>
      <el-table-column label="业务范围" prop="busc" min-width="150">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.busc"
            showHelp
            :help="{
              a: 'CORE_BUSC',
              b: scope.row,
              c: ['busc-busc', 'buscName-buscName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.buscName" />
        </template>
      </el-table-column>
      <el-table-column label="分配编号" prop="assignNo" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.assignNo" :szdopt="SR.szdopt" :maxlength="50" />
        </template>
      </el-table-column>
      <el-table-column label="原因代码" prop="reas" min-width="120">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.reas"
            showHelp
            :help="{
              a: 'CORE_REAS',
              b: scope.row,
              c: ['reas-reas', 'reasName-reasName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.reasName" />
        </template>
      </el-table-column>
      <el-table-column label="采购凭证" prop="poDoc" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.poDoc" :szdopt="SR.szdopt" :maxlength="50" />
        </template>
      </el-table-column>
      <el-table-column label="采购凭证项目" prop="poDocItem" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.poDocItem" :szdopt="SR.szdopt" type="number" numDot :numDec="0" />
        </template>
      </el-table-column>
      <el-table-column label="内部订单" prop="orderCode" min-width="200">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.orderCode"
            showHelp
            :help="{
              a: 'SEXP_ORDE',
              b: scope.row,
              c: ['orderCode-orderCode', 'orderName-orderName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.orderName" />
        </template>
      </el-table-column>
      <el-table-column label="段" prop="segm" min-width="150">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.segm"
            showHelp
            :help="{
              a: 'CORE_SEGM',
              b: scope.row,
              c: ['segm-segm', 'segmName-segmName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.segmName" />
        </template>
      </el-table-column>
      <el-table-column label="功能范围" prop="fusc" min-width="150">
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.fusc"
            showHelp
            :help="{
              a: 'CORE_FUSC',
              b: scope.row,
              c: ['fusc-fusc', 'fuscName-fuscName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.fuscName" />
        </template>
      </el-table-column>
      <el-table-column label="固定资产" prop="assetNo" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.assetNo" :szdopt="SR.szdopt" :maxlength="50" />
        </template>
      </el-table-column>
      <el-table-column label="资产次级编号" prop="assetNoSub" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.assetNoSub" :szdopt="SR.szdopt" :maxlength="10" />
        </template>
      </el-table-column>
      <el-table-column label="WBS项目号" prop="wbsNo" min-width="120">
        <template v-slot="scope">
          <szd-input v-model="scope.row.wbsNo" :szdopt="SR.szdopt" :maxlength="50" />
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
      handleSelRow(selection) {
        this.listSel = selection;
      },
      addRow() {
        this.objData.adjustList.push({
          itemNo: "",
          accs: "",
          accsName: "",
          drCr: "",
          amt: "",
          supp: "",
          suppName: "",
          cust: "",
          custName: "",
          sgl: "",
          summary: "",
          cstc: "",
          cstcName: "",
          prfc: "",
          prfcName: "",
          busc: "",
          buscName: "",
          assignNo: "",
          reas: "",
          reasName: "",
          poDoc: "",
          poDocItem: "",
          orderCode: "",
          orderName: "",
          segm: "",
          segmName: "",
          fusc: "",
          fuscName: "",
          assetNo: "",
          assetNoSub: "",
          wbsNo: ""
        });
        this.setItemNo();
      },
      delRow() {
        this.objData.adjustList = this.objData.adjustList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      setItemNo() {
        for (let i = 0; i < this.objData.adjustList.length; i++) {
          this.objData.adjustList[i].itemNo = i + 1;
        }
      }
    }
  };
</script>
