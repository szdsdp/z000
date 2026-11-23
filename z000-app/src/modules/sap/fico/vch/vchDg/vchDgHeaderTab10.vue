<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>业务单号: {{ objData.header.bussDocId || "未提交" }}</template>
      <template #main>
        <szd-input label="业务类型" v-model="objData.header.bstp" :desc="objData.header.bstpName" disabled />
        <szd-date label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" />
        <szd-select label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled />
        <szd-select label="货币" v-model="objData.header.curr" baseField="CORE_CURR" required :szdopt="SR.szdopt" />
        <szd-input label="申请金额" v-model="objData.header.amtApply" type="number" disabled />
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
        <szd-input label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" required :maxlength="255" />
        <szd-textarea label="备注" v-model="objData.header.remark" :szdopt="SR.szdopt" :maxlength="150" />
      </template>
    </szd-card-a>
  </div>
</template>
<script>
  export default { props: ["objData", "SR", "wfOper"] };
</script>
