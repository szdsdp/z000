<template>
  <el-dialog
    :title="dg.title"
    v-model="dg.open"
    append-to-body
    :close-on-click-modal="false"
    class="app-dialog-container"
    align-center
    destroy-on-close
    draggable>
    <div v-szd-dialog="[1000, 600]" />
    <el-form class="app-el-form">
      <el-row>
        <szd-input-form-item-col
          label="业务小类"
          v-model="item.bsub"
          showHelp
          clearable
          :help="{
            a: 'CORE_BSUB01',
            b: item,
            c: ['bsub', 'bsubName'],
            d: [['bstp', objData.header.bstp]]
          }"
          required
          :szdopt="SR.szdopt"
          :span="12"
          :descSpan="12"
          :desc="item.bsubName" />
      </el-row>
      <!-- 申请金额 -->
      <el-row>
        <szd-select-form-item-col clearable label="税率" v-model="item.taxr" baseField="CORE_TAXR" :showKey="false" required :szdopt="SR.szdopt" :span="12" />
        <szd-input-form-item-col
          label="金额"
          v-model="item.amt"
          type="number"
          numDot
          :numDec="2"
          :numMax="100000000000"
          :numMin="0"
          required
          :szdopt="SR.szdopt"
          :span="12"
          @change="calcAmt(objData, SR, item)" />
      </el-row>
      <el-row>
        <szd-input-form-item-col
          label="税额"
          v-model="item.amtTax"
          type="number"
          numDot
          :numDec="2"
          :numMax="100000000000"
          :numMin="0"
          required
          :disabled="item.taxr === '0'"
          :szdopt="SR.szdopt"
          :span="12"
          @change="calcAmtTax(objData, SR, item)" />
        <szd-input-form-item-col label="金额(不含税)" v-model="item.amtNoTax" type="number" numDot :numDec="2" disabled :span="12" />
      </el-row>
      <el-row>
        <szd-textarea-form-item-col label="备注" v-model="item.remark" :maxlength="150" :rows="2" szdopt="SR.szdopt" :span="24" label-width="12.5%" />
      </el-row>
    </el-form>
    <template #footer>
      <szd-button label="关闭" type="primary" @click="closeForm" />
    </template>
  </el-dialog>
</template>

<script>
  import { calcAmt, calcAmtTax } from "./js/expCostDgCalc";

  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {
        dg: {
          title: "",
          open: false
        },
        item: {}
      };
    },
    methods: {
      calcAmt,
      calcAmtTax,
      //接受参数
      initExpCostItemInfo(itemParam) {
        this.dg.open = true;
        this.dg.title = "项目信息  行项目：" + itemParam.itemNo;
        this.item = itemParam;
      },

      //关闭
      closeForm() {
        this.dg.open = false;
      }
    }
  };
</script>