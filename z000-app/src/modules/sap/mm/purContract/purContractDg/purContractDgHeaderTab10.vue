<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <!-- 业务单号显示在卡片头部 -->
      <template #header>业务单号: {{ objData.header.bussDocId || "未提交" }}</template>
      <template #main>
        <szd-select label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled />
        <szd-date label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" />
        <szd-select label="类型" v-model="objData.header.type" baseField="Z000_MM_CGHT_LX" :szdopt="SR.szdopt" required />
        <szd-input
          label="供应商"
          v-model="objData.header.supp"
          :szdopt="SR.szdopt"
          required
          :desc="objData.header.suppName"
          readonly
          showHelp
          @help="szdSh({ a: 'CORE_SUPP', b: objData.header, c: ['supp-supp', 'suppName-suppName'] })" />
        <szd-select label="采购组织" v-model="objData.header.purOrg" baseField="Z000_MDM_ORG_PUR" :szdopt="SR.szdopt" required />
        <szd-select label="采购组" v-model="objData.header.purGroup" baseField="Z000_MM_CGDD_CGZ" :szdopt="SR.szdopt" required />
        <szd-select label="货币" v-model="objData.header.curr" baseField="CORE_CURR" :szdopt="SR.szdopt" required />
        <szd-date label="有效开始日期" v-model="objData.header.startDate" :szdopt="SR.szdopt" required />
        <szd-date label="有效结束日期" v-model="objData.header.endDate" :szdopt="SR.szdopt" required />
        <szd-input
          label="公司代码"
          v-model="objData.header.cmpy"
          :szdopt="SR.szdopt"
          required
          :desc="objData.header.cmpyName"
          readonly
          showHelp
          @help="szdSh({ a: 'CORE_CMPY', b: objData.header, c: ['cmpy-cmpy', 'cmpyName-cmpyName'] })" />
        <szd-input
          label="部门"
          v-model="objData.header.cstc"
          :szdopt="SR.szdopt"
          required
          :desc="objData.header.cstcName"
          readonly
          showHelp
          @help="szdSh({ a: 'CORE_CSTC', b: objData.header, c: ['cstc-cstc', 'cstcName-cstcName'] })" />
        <szd-input label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :maxlength="255" />
        <szd-input label="创建人" v-model="objData.header.createBy" :desc="objData.header.createByName" disabled />
        <szd-input label="创建时间" v-model="objData.header.createTime" disabled />
        <szd-input label="业务类型" v-model="objData.header.bstp" :desc="objData.header.bstpName" disabled />
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
