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
      <szd-input-form-item-col label="物料编码" :span="8" v-model="objData.header.mtlCode" disabled :szdopt="SR.szdopt" />
      <szd-input-form-item-col label="物料名称" v-model="objData.header.mtlName" :szdopt="SR.szdopt" required :span="8" :maxlength="40" />
      <szd-select-form-item-col
        label="行业领域"
        v-model="objData.header.indSector"
        baseField="Z000_MDM_IND_SECTOR"
        :szdopt="SR.szdopt"
        required
        :span="8"
        clearable />
      <szd-select-form-item-col
        label="物料类型"
        v-model="objData.header.mtlType"
        baseField="Z000_MDM_MTL_TYPE"
        :szdopt="SR.szdopt"
        required
        :span="8"
        clearable />
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
    props: ["objData", "SR"]
  };
</script>
