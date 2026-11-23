<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-select-form-item-col
        label="基本计量单位"
        v-model="objData.baseView.unitBase"
        baseField="CORE_UNIT"
        :szdopt="SR.szdopt"
        required
        :span="8"
        clearable />
      <szd-select-form-item-col
        label="物料组"
        v-model="objData.baseView.mtlGroup"
        baseField="Z000_MDM_MTL_GROUP"
        :szdopt="SR.szdopt"
        :span="8"
        clearable />
      <szd-input-form-item-col label="大小/量纲" v-model="objData.baseView.sizeDim" :szdopt="SR.szdopt" :span="8" :maxlength="25" />
      <szd-select-form-item-col label="订单单位" v-model="objData.baseView.unitPo" baseField="CORE_UNIT" :szdopt="SR.szdopt" :span="8" clearable />
      <szd-input-form-item-col label="净重" v-model="objData.baseView.netWt" :szdopt="SR.szdopt" :span="8" :maxlength="13" />
      <szd-select-form-item-col label="重量单位" v-model="objData.baseView.unitWt" baseField="CORE_UNIT" :szdopt="SR.szdopt" :span="8" clearable />
    </el-row>
  </el-form>
</template>

<script>
  export default {
    props: ["objData", "SR"]
  };
</script>
