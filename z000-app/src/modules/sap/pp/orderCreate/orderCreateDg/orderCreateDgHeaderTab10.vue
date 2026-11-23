<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <!-- 业务单号显示在卡片头部 -->
      <template #header>业务单号: {{ objData.header.bussDocId || "未提交" }}</template>
      <template #main>
        <szd-input label="业务类型" v-model="objData.header.bstp" :desc="objData.header.bstpName" disabled />
        <szd-date label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" />
        <szd-select label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled />
        <szd-input label="创建人" v-model="objData.header.createBy" :desc="objData.header.createByName" disabled />
        <szd-input label="创建时间" v-model="objData.header.createTime" disabled />
        <szd-input
          label="公司代码"
          v-model="objData.header.cmpy"
          :szdopt="SR.szdopt"
          required
          :desc="objData.header.cmpyName"
          readonly
          showHelp
          @help="szdSh({ a: 'CORE_CMPY', b: objData.header, c: ['cmpy-cmpy', 'cmpyName-cmpyName'] })" />
        <szd-input label="成本中心" v-model="objData.header.cstc" disabled required :desc="objData.header.cstcName" />
        <szd-input
          label="物料编码"
          v-model="objData.header.mtlCode"
          :szdopt="SR.szdopt"
          required
          :desc="objData.header.mtlName"
          showHelp
          @help="szdSh({ a: 'Z000_MDM_MTL', b: objData.header, c: ['mtlCode', 'mtlName', 'unit'] })" />
        <szd-select label="单位" v-model="objData.header.unit" baseField="CORE_UNIT" :szdopt="SR.szdopt" required />
        <szd-input
          label="工厂"
          v-model="objData.header.fact"
          :szdopt="SR.szdopt"
          required
          :desc="objData.header.factName"
          readonly
          showHelp
          @help="szdSh({ a: 'CORE_FACT', b: objData.header, c: ['fact-fact', 'factName-factName'] })" />
        <szd-select label="订单类型" v-model="objData.header.orderType" baseField="Z000_PP_SCDDCJ_ORDER_TYPE" :szdopt="SR.szdopt" required />
        <szd-date label="基本开始日期" v-model="objData.header.startDate" :szdopt="SR.szdopt" required />
        <szd-date label="基本完成日期" v-model="objData.header.endDate" :szdopt="SR.szdopt" required />
        <szd-input label="订单数量总计" v-model="objData.header.quantity" :szdopt="SR.szdopt" required type="number" />
        <szd-input label="订单号" v-model="objData.header.aufnr" disabled />
        <szd-select label="是否已下达" v-model="objData.header.released" baseField="Z000_TF_FLAG" disabled />
        <szd-select label="是否已关闭" v-model="objData.header.closed" baseField="Z000_TF_FLAG" disabled />
        <szd-input label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :maxlength="255" />
        <szd-textarea label="备注" v-model="objData.header.remark" :szdopt="SR.szdopt" :maxlength="150" />
      </template>
    </szd-card-a>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR"],
    components: {},
    data() {
      return {};
    },
    methods: {}
  };
</script>
