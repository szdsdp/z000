<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col label="订单号" :span="8" v-model="objData.header.orderNo" disabled />
      <szd-input-form-item-col label="订单类型" :span="8" v-model="objData.header.orderType" disabled />
      <szd-input-form-item-col
        label="物料编码"
        :span="8"
        v-model="objData.header.mtlCode"
        disabled
        :descSpan="14"
        :desc="objData.header.mtlName"
        clearable
        showHelp
        @help="szdSh({ a: 'Z000_MDM_MTL', b: objData.header, c: ['mtlCode', 'mtlName'] })" />
      <szd-input-form-item-col
        label="工厂"
        :span="8"
        v-model="objData.header.fact"
        disabled
        :descSpan="14"
        :desc="objData.header.factName"
        clearable
        showHelp
        @help="szdSh({ a: 'CORE_FACT', b: objData.header, c: ['fact', 'factName'] })" />
      <szd-input-form-item-col label="订单描述" :span="8" v-model="objData.header.orderDesc" disabled />
      <szd-input-form-item-col label="生产版本" :span="8" v-model="objData.header.prodVer" disabled />
      <szd-input-form-item-col label="订单数量" :span="8" v-model="objData.header.orderQty" disabled />
      <szd-select-form-item-col label="单位" :span="8" v-model="objData.header.unit" baseField="CORE_UNIT" disabled clearable />
      <szd-input-form-item-col label="已发货数量" :span="8" v-model="objData.header.shippedQty" disabled />
      <szd-input-form-item-col label="已收货数量" :span="8" v-model="objData.header.receivedQty" disabled />
      <szd-input-form-item-col label="订单状态" :span="8" v-model="objData.header.orderStatus" disabled />
      <szd-input-form-item-col label="工艺路线编号" :span="8" v-model="objData.header.routingNo" disabled />
      <szd-input-form-item-col label="预留编号" :span="8" v-model="objData.header.resNo" disabled />
      <szd-date-form-item-col label="订单开始日期" :span="8" v-model="objData.header.startDate" disabled />
      <szd-date-form-item-col label="基本完成日期" :span="8" v-model="objData.header.finishDate" disabled />
    </el-row>
  </el-form>
</template>

<script>
  export default { props: ["objData", "SR"] };
</script>
