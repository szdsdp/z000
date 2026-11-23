<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>任务单号: {{ objData.header.bussDocId || "未提交" }}</template>
      <template #main>
        <szd-input label="盘点计划单号" v-model="objData.header.checkPlanBussDocId" disabled />
        <szd-select label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled />
        <szd-input label="业务类型" v-model="objData.header.bstp" :desc="objData.header.bstpName" disabled />
        <szd-input label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :maxlength="255" />
        <szd-select label="任务状态" v-model="objData.header.taskStatus" baseField="Z000_KCPD_TASK_STATUS" disabled />
        <szd-input label="工厂" v-model="objData.header.fact" :desc="objData.header.factName" disabled />
        <szd-input label="库位" v-model="objData.header.store" :desc="objData.header.storeName" disabled />
        <szd-input label="公司代码" v-model="objData.header.cmpy" :desc="objData.header.cmpyName" disabled />
        <szd-input label="部门" v-model="objData.header.cstc" :desc="objData.header.cstcName" disabled />
        <szd-input label="创建人" v-model="objData.header.createBy" :desc="objData.header.createByName" disabled />
        <szd-input label="创建时间" v-model="objData.header.createTime" disabled />
        <szd-date label="业务日期" v-model="objData.header.bussDate" disabled />
        <szd-select label="是否盲盘" v-model="objData.header.blindFlag" baseField="Z000_TF_FLAG" disabled />
        <szd-textarea label="备注" v-model="objData.header.remark" :szdopt="SR.szdopt" :maxlength="150" />
      </template>
    </szd-card-a>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    components: {},
    data() {
      return {};
    },
    methods: {}
  };
</script>
