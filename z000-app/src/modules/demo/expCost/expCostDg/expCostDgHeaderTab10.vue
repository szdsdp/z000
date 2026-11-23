<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a>
      <template #main>
        <szd-input label="业务单号" v-model="objData.header.bussDocId" disabled />
        <szd-input
          label="公司"
          v-model="objData.header.cmpy"
          showHelp
          :help="{ a: 'CORE_CMPY', b: objData.header, c: ['cmpy-cmpy', 'cmpyName-cmpyName'] }"
          required
          :szdopt="SR.szdopt"
          :desc="objData.header.cmpyName" />
        <szd-input
          label="部门"
          v-model="objData.header.cstc"
          showHelp
          :help="{ a: 'CORE_CSTC', b: objData.header, c: ['cstc-cstc', 'cstcName-cstcName'], d: [['cmpy', objData.header.cmpy]] }"
          required
          :szdopt="SR.szdopt"
          :desc="objData.header.cstcName" />
        <szd-input
          label="报账人"
          v-model="objData.header.userId"
          showHelp
          :help="{ a: 'CORE_USER', b: objData.header, c: ['userId', 'userName'] }"
          required
          :szdopt="SR.szdopt"
          :desc="objData.header.userName" />
        <szd-input label="业务类型" v-model="objData.header.bstp" required :szdopt="SR.szdopt" :desc="objData.header.bstpName" />
        <szd-input
          v-if="wfOper.dyWfId || objData.header.wfDyId"
          label="动态流程"
          v-model="objData.header.wfDyId"
          showHelp
          @help="szdWf.shDyId(objData.header.bstp, objData.header)"
          readonly
          :disabled="!['A', 'B'].includes(objData.header.bussStatus)"
          :szdopt="SR.szdopt"
          :desc="objData.header.wfDyName" />
        <szd-date label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" />
        <szd-select label="货币" v-model="objData.header.curr" baseField="CORE_CURR" required :szdopt="SR.szdopt" />
        <szd-select label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled />
        <szd-input label="业务标题" v-model="objData.header.title" :maxlength="50" required :szdopt="SR.szdopt" />
        <szd-input label="申请金额" v-model="objData.header.amtApply" disabled type="number" numDot :numDec="2" />
        <szd-textarea label="备注" v-model="objData.header.remark" :maxlength="150" :rows="2" :szdopt="SR.szdopt" />
      </template>
    </szd-card-a>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {};
    },
    methods: {}
  };
</script>