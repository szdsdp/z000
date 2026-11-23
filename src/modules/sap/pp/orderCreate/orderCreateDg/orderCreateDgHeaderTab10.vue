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
        label="物料编码"
        :span="8"
        v-model="objData.header.mtlCode"
        :szdopt="SR.szdopt"
        required
        :descSpan="14"
        :desc="objData.header.mtlName"
        clearable
        showHelp
        @help="szdSh({ a: 'Z000_MDM_MTL', b: objData.header, c: ['mtlCode', 'mtlName','unit'] })" />
      <szd-select-form-item-col label="单位" v-model="objData.header.unit" baseField="CORE_UNIT" :szdopt="SR.szdopt" required clearable :span="8" />
      <szd-input-form-item-col
        label="工厂"
        :span="8"
        v-model="objData.header.fact"
        :szdopt="SR.szdopt"
        required
        :descSpan="14"
        :desc="objData.header.factName"
        clearable
        showHelp
        @help="szdSh({ a: 'CORE_FACT', b: objData.header, c: ['fact-fact', 'factName-factName'] })" />
      <szd-select-form-item-col
        label="订单类型"
        v-model="objData.header.orderType"
        baseField="Z000_PP_SCDDCJ_ORDER_TYPE"
        :szdopt="SR.szdopt"
        required
        clearable
        :span="8" />
      <szd-date-form-item-col label="基本开始日期" v-model="objData.header.startDate" :szdopt="SR.szdopt" required :span="8" />
      <szd-date-form-item-col label="基本完成日期" v-model="objData.header.endDate" :szdopt="SR.szdopt" required :span="8" />
      <szd-input-form-item-col
        label="订单数量总计"
        v-model="objData.header.quantity"
        :szdopt="SR.szdopt"
        required
        :span="8"
        type="number"
        numDot
        :numDec="2" />
      <szd-input-form-item-col label="订单号" v-model="objData.header.aufnr" disabled :span="8" />
      <szd-select-form-item-col label="是否已下达" v-model="objData.header.released" baseField="Z000_TF_FLAG" disabled :span="8" />
      <szd-select-form-item-col label="是否已关闭" v-model="objData.header.closed" baseField="Z000_TF_FLAG" disabled :span="8" />
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
