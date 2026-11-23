<template>
  <div class="app-sub-container" style="padding: 0">
    <el-form class="app-el-form">
      <!-- 电汇信息 -->
      <el-row v-if="objData.header.payWay === 'B'">
        <szd-input-form-item-col label="银行回单号" v-model="objData.cash.bankDoc" disabled :span="8" />
        <szd-input-form-item-col label="银行流水号" v-model="objData.cash.bankFlowNo" disabled :span="8" />
        <szd-input-form-item-col label="本方账户" v-model="objData.cash.myAccNo" disabled :span="8" />
        <szd-input-form-item-col label="对方账户" v-model="objData.cash.payAccNo" disabled :span="8" />
        <szd-input-form-item-col label="对方户名" v-model="objData.cash.payAccName" disabled :span="8" />
        <szd-input-form-item-col label="金额" v-model="objData.cash.amt" type="number" :numDec="2" numDot disabled :span="8" />
        <szd-input-form-item-col label="币种" v-model="objData.cash.curr" disabled :span="8" />
        <szd-date-form-item-col label="交易日期" v-model="objData.cash.tranDate" disabled :span="8" />
        <szd-time-form-item-col label="交易时间" v-model="objData.cash.tranTime" disabled :span="8" />
        <szd-input-form-item-col label="银行摘要" v-model="objData.cash.summary" disabled :span="8" />
      </el-row>
      <!-- 票据信息 -->
      <el-row v-else>
        <szd-input-form-item-col label="系统票号" v-model="objData.bill.bankDoc" disabled :span="8" />
        <szd-input-form-item-col label="持票账户" v-model="objData.bill.holderAcc" disabled :span="8" />
        <szd-input-form-item-col label="票据类型" v-model="objData.bill.billType" disabled :span="8" />
        <szd-input-form-item-col label="票包号" v-model="objData.bill.billNo" disabled :span="8" />
        <szd-input-form-item-col label="子票区间从" v-model="objData.bill.subBillFrom" disabled :span="8" />
        <szd-input-form-item-col label="子票区间至" v-model="objData.bill.subBillTo" disabled :span="8" />
        <szd-date-form-item-col label="出票日期" v-model="objData.bill.issueDate" disabled :span="8" />
        <szd-date-form-item-col label="到期日期" v-model="objData.bill.dueDate" disabled :span="8" />
        <szd-input-form-item-col label="票据范围" v-model="objData.bill.billScope" disabled :span="8" />
        <szd-input-form-item-col label="金额" v-model="objData.bill.amt" type="number" :numDec="2" numDot disabled :span="8" />
        <szd-input-form-item-col label="承兑人" v-model="objData.bill.acceptor" disabled :span="8" />
        <szd-date-form-item-col label="签收日期" v-model="objData.bill.signDate" disabled :span="8" />
        <szd-input-form-item-col label="前手账号" v-model="objData.bill.endorserAcc" disabled :span="8" />
        <szd-input-form-item-col label="前手账号名称" v-model="objData.bill.endorser" disabled :span="8" />
        <szd-input-form-item-col label="出票人" v-model="objData.bill.drawer" disabled :span="8" />
        <szd-input-form-item-col label="收票人" v-model="objData.bill.payee" disabled :span="8" />
        <szd-input-form-item-col label="银行摘要" v-model="objData.bill.summary" disabled :span="8" />
      </el-row>
    </el-form>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    data() { return {}; },
    methods: {}
  };
</script>
