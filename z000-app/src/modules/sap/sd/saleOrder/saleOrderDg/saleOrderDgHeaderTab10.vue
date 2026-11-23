<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>业务单号: {{ objData.header.bussDocId || "未提交" }}</template>
      <template #main>
        <szd-input label="业务类型" v-model="objData.header.bstp" :desc="objData.header.bstpName" disabled />
        <szd-date label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" />
        <szd-select label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled />
        <szd-input label="创建人" v-model="objData.header.createBy" :desc="objData.header.createByName" disabled />
        <szd-input label="创建时间" v-model="objData.header.createTime" disabled />
        <szd-input label="公司代码" v-model="objData.header.cmpy" :szdopt="SR.szdopt" required :desc="objData.header.cmpyName" readonly showHelp @help="szdSh({ a: 'CORE_CMPY', b: objData.header, c: ['cmpy-cmpy', 'cmpyName-cmpyName'] })" />
        <szd-input label="成本中心" v-model="objData.header.cstc" disabled required :desc="objData.header.cstcName" />
        <szd-input label="售达方" v-model="objData.header.soldTo" :szdopt="SR.szdopt" required :desc="objData.header.soldToName" readonly showHelp @help="szdSh({ a: 'CORE_CUST', b: objData.header, c: ['soldTo-cust', 'soldToName-custName'] })" />
        <szd-input label="送达方" v-model="objData.header.shipTo" :szdopt="SR.szdopt" :desc="objData.header.shipToName" readonly showHelp @help="szdSh({ a: 'CORE_CUST', b: objData.header, c: ['shipTo-cust', 'shipToName-custName'] })" />
        <szd-input label="开票方" v-model="objData.header.billTo" :szdopt="SR.szdopt" :desc="objData.header.billToName" readonly showHelp @help="szdSh({ a: 'CORE_CUST', b: objData.header, c: ['billTo-cust', 'billToName-custName'] })" />
        <szd-select label="订单类型" v-model="objData.header.orderType" baseField="Z000_SD_ORDER_TYPE" :szdopt="SR.szdopt" required />
        <szd-select label="销售机构" v-model="objData.header.salesOrg" baseField="Z000_MDM_ORG_SALE" :szdopt="SR.szdopt" required />
        <szd-select label="分销渠道" v-model="objData.header.distrChan" baseField="Z000_MDM_DISTR_CHAN" :szdopt="SR.szdopt" required />
        <szd-select label="产品组" v-model="objData.header.prodGroup" baseField="Z000_MDM_PROD_GROUP" :szdopt="SR.szdopt" required />
        <szd-input label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :maxlength="255" />
        <szd-textarea label="备注" v-model="objData.header.remark" :szdopt="SR.szdopt" :maxlength="150" />
      </template>
    </szd-card-a>
  </div>
</template>
<script>
  export default { props: ["objData", "SR"], components: {}, data() { return {}; }, methods: {} };
</script>
