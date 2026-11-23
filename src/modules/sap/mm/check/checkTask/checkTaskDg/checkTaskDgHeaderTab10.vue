<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col label="业务单号" :span="8" v-model="objData.header.bussDocId" disabled />
      <szd-input-form-item-col
        label="盘点计划单号"
        :span="8"
        v-model="objData.header.checkPlanBussDocId"
        disabled
        dblclick
        @dblclick="handleGoToPlan" />
      <szd-select-form-item-col label="单据状态" :span="8" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled clearable />
      <szd-input-form-item-col label="业务类型" v-model="objData.header.bstp" disabled :span="8" :descSpan="14" :desc="objData.header.bstpName" />
      <szd-input-form-item-col label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :span="8" :maxlength="255" />
      <szd-select-form-item-col label="任务状态" :span="8" v-model="objData.header.taskStatus" baseField="Z000_KCPD_TASK_STATUS" disabled clearable />
      <szd-input-form-item-col label="工厂" :span="8" v-model="objData.header.fact" disabled :descSpan="14" :desc="objData.header.factName" />
      <szd-input-form-item-col label="库位" :span="8" v-model="objData.header.store" disabled :descSpan="14" :desc="objData.header.storeName" />
      <szd-input-form-item-col label="公司代码" :span="8" v-model="objData.header.cmpy" disabled :descSpan="14" :desc="objData.header.cmpyName" />
      <szd-input-form-item-col label="部门" :span="8" v-model="objData.header.cstc" disabled :descSpan="14" :desc="objData.header.cstcName" />
      <szd-input-form-item-col
        label="创建人"
        :span="8"
        v-model="objData.header.createBy"
        disabled
        :descSpan="14"
        :desc="objData.header.createByName" />
      <szd-input-form-item-col label="创建时间" :span="8" v-model="objData.header.createTime" disabled />
      <szd-date-form-item-col label="业务日期" v-model="objData.header.bussDate" :span="8" disabled />
      <szd-select-form-item-col label="是否盲盘" :span="8" v-model="objData.header.blindFlag" baseField="Z000_TF_FLAG" disabled clearable />
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
    props: ["objData", "SR", "wfOper"],
    data() {
      return {};
    },
    methods: {
      //跳转单据
      handleGoToPlan() {
        this.szdWf.goDoc("z000_mm_kcpd_01a", this.objData.header.planId, "V");
      }
    }
  };
</script>
