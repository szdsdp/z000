<template>
  <div class="app-sub-container" style="padding: 0">
    <el-form class="app-el-form">
      <el-row>
        <szd-input-form-item-col label="业务单号" v-model="objData.header.bussDocId" disabled :span="8" />
        <szd-input-form-item-col label="业务类型" v-model="objData.header.bstp" disabled :span="8" :descSpan="14" :desc="objData.header.bstpName" />
        <szd-select-form-item-col label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled :span="8" />
        <szd-input-form-item-col label="公司" v-model="objData.header.cmpy" disabled :span="8" :descSpan="14" :desc="objData.header.cmpyName" />
        <szd-input-form-item-col label="回款日期" v-model="objData.header.bussDate" disabled :span="8" />
        <szd-select-form-item-col label="货币" v-model="objData.header.curr" baseField="CORE_CURR" disabled :span="8" />
        <szd-input-form-item-col label="对方账户" v-model="objData.header.payAccNo" disabled :span="8" />
        <szd-input-form-item-col label="对方户名" v-model="objData.header.payAccName" disabled :span="8" />
        <szd-input-form-item-col label="银行摘要" v-model="objData.header.summary" disabled :span="8" />
        <szd-select-form-item-col
          label="回款性质"
          v-model="objData.header.payNature"
          baseField="Z000_PAY_NATURE"
          required
          :szdopt="SR.szdopt"
          :span="8"
          @change="changePayNature" />
        <szd-input-form-item-col
          label="利润中心"
          v-model="objData.header.prfc"
          showHelp
          :help="{ a: 'CORE_PRFC', b: objData.header, c: ['prfc', 'prfcName'] }"
          required
          :szdopt="SR.szdopt"
          :span="8"
          :descSpan="14"
          :desc="objData.header.prfcName" />
        <szd-select-form-item-col label="支付方式" v-model="objData.header.payWay" baseField="Z000_PAY_WAY" required disabled :span="8" />
        <szd-input-form-item-col
          label="本次认领金额"
          v-model="objData.header.amtReceipt"
          type="number"
          numDot
          :numDec="2"
          :numMax="100000000000"
          :numMin="0"
          required
          :szdopt="SR.szdopt"
          :span="8"
          @change="handleAmtReceiptChange" />
        <szd-input-form-item-col label="回款金额" v-model="objData.header.amt" type="number" numDot :numDec="2" disabled :span="8" />
        <szd-input-form-item-col
          v-if="['A', 'B'].includes(objData.header.bussStatus)"
          label="剩余认领金额"
          v-model="objData.header.amtRemain"
          type="number"
          numDot
          :numDec="2"
          disabled
          :span="8" />
        <szd-input-form-item-col
          label="客户"
          v-model="objData.header.cust"
          showHelp
          @help="szdSh({ a: 'CORE_CUST', b: objData.header, c: ['cust', 'custName'] })"
          disabled
          :span="8"
          :descSpan="14"
          :desc="objData.header.custName" />
        <szd-input-form-item-col label="项目文本" v-model="objData.header.text" required :maxlength="50" :szdopt="SR.szdopt" :span="8" />
        <szd-input-form-item-col label="业务标题" v-model="objData.header.title" required :maxlength="50" :szdopt="SR.szdopt" :span="8" />
        <szd-textarea-form-item-col
          label="备注"
          v-model="objData.header.remark"
          :maxlength="150"
          :rows="2"
          :szdopt="SR.szdopt"
          :span="24"
          label-width="8.35%" />
      </el-row>
    </el-form>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    data() { return {}; },
    methods: {
      // 本次认领金额变更
      handleAmtReceiptChange() {
        this.$nextTick(() => {
          if (!this.objData.header.amtReceipt) this.objData.header.amtReceipt = 0;
          if (!this.objData.header.amtRemain) this.objData.header.amtRemain = 0;
          if (this.objData.header.amtReceipt > this.objData.header.amtRemain || !this.objData.header.amtRemain) {
            this.objData.header.amtReceipt = this.objData.header.amtRemain;
            this.$mW("本次认领金额不能大于剩余认领金额");
          }
        });
      },
      // 回款性质变更
      changePayNature() {
        let custName = this.objData.header.custName ? this.objData.header.custName : "";
        if (this.objData.header.payNature === "10" || this.objData.header.payNature === "30") {
          if (this.objData.header.payWay === "B") {
            this.objData.header.text = "应收_" + custName;
          } else {
            this.objData.header.text = "应收票据_" + custName;
          }
        } else if (["20", "40"].includes(this.objData.header.payNature)) {
          if (this.objData.header.payWay === "B") {
            this.objData.header.text = "预收_" + custName;
          } else {
            this.objData.header.text = "预收票据_" + custName;
          }
        }
      }
    }
  };
</script>
