<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col label="业务单号" :span="8" v-model="objData.header.bussDocId" disabled />
      <szd-input-form-item-col label="业务类型" v-model="objData.header.bstp" disabled :span="8" :descSpan="14" :desc="objData.header.bstpName" />
      <szd-date-form-item-col label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" :span="8" />
      <szd-select-form-item-col label="单据状态" :span="8" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled clearable />
      <szd-input-form-item-col
        label="创建人"
        v-model="objData.header.createBy"
        disabled
        :szdopt="SR.szdopt"
        :span="8"
        :descSpan="14"
        :desc="objData.header.createByName" />
      <szd-input-form-item-col label="创建时间" :span="8" v-model="objData.header.createTime" disabled />
      <szd-input-form-item-col
        label="公司代码"
        :span="8"
        v-model="objData.header.cmpy"
        :szdopt="SR.szdopt"
        required
        :descSpan="14"
        :desc="objData.header.cmpyName"
        readonly
        clearable
        showHelp
        @help="szdSh({ a: 'CORE_CMPY', b: objData.header, c: ['cmpy-cmpy', 'cmpyName-cmpyName'] })" />
      <szd-input-form-item-col
        label="成本中心"
        v-model="objData.header.cstc"
        disabled
        required
        :szdopt="SR.szdopt"
        :span="8"
        :descSpan="14"
        :desc="objData.header.cstcName" />
      <szd-input-form-item-col
        label="售达方"
        :span="8"
        v-model="objData.header.soldTo"
        :szdopt="SR.szdopt"
        required
        clearable
        showHelp
        :descSpan="14"
        :desc="objData.header.soldToName"
        readonly
        @help="szdSh({ a: 'CORE_CUST', b: objData.header, c: ['soldTo-cust', 'soldToName-custName'] })" />
      <szd-input-form-item-col
        label="送达方"
        :span="8"
        v-model="objData.header.shipTo"
        :szdopt="SR.szdopt"
        clearable
        showHelp
        :descSpan="14"
        :desc="objData.header.shipToName"
        readonly
        @help="szdSh({ a: 'CORE_CUST', b: objData.header, c: ['shipTo-cust', 'shipToName-custName'] })" />
      <szd-input-form-item-col
        label="开票方"
        :span="8"
        v-model="objData.header.billTo"
        :szdopt="SR.szdopt"
        clearable
        showHelp
        :descSpan="14"
        :desc="objData.header.billToName"
        readonly
        @help="szdSh({ a: 'CORE_CUST', b: objData.header, c: ['billTo-cust', 'billToName-custName'] })" />
      <szd-select-form-item-col
        label="订单类型"
        v-model="objData.header.orderType"
        baseField="Z000_SD_ORDER_TYPE"
        :szdopt="SR.szdopt"
        required
        clearable
        :span="8" />
      <szd-select-form-item-col
        label="销售机构"
        v-model="objData.header.salesOrg"
        baseField="Z000_MDM_ORG_SALE"
        :szdopt="SR.szdopt"
        required
        clearable
        :span="8" />
      <szd-select-form-item-col label="分销渠道" :span="8" v-model="objData.header.distrChan" baseField="Z000_MDM_DISTR_CHAN" :szdopt="SR.szdopt" required clearable />
      <szd-select-form-item-col label="产品组" :span="8" v-model="objData.header.prodGroup" baseField="Z000_MDM_PROD_GROUP" :szdopt="SR.szdopt" required clearable />
      <szd-input-form-item-col label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :span="8" :maxlength="255" />
    </el-row>
    <el-row>
      <szd-textarea-form-item-col
        label="备注"
        :span="16"
        label-width="12.5%"
        v-model="objData.header.remark"
        :szdopt="SR.szdopt"
        :maxlength="150"
        :rows="2" />
    </el-row>
  </el-form>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    data() {
      return {};
    },
    methods: {}
  };
</script>
