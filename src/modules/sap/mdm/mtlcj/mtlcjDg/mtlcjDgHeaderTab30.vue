<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col
        label="工厂"
        :span="8"
        v-model="objData.purView.fact"
        :szdopt="SR.szdopt"
        :descSpan="10"
        :desc="objData.purView.factName"
        readonly
        clearable
        showHelp
        @help="szdSh({ a: 'CORE_FACT', b: objData.purView, c: ['fact-fact', 'factName-factName'] })" />
      <szd-select-form-item-col label="ABC标识" v-model="objData.purView.abcId" baseField="Z000_MDM_ABC_ID" :szdopt="SR.szdopt" :span="8" clearable />
      <szd-select-form-item-col
        label="采购组"
        v-model="objData.purView.purGroup"
        baseField="Z000_MM_CGDD_CGZ"
        :szdopt="SR.szdopt"
        :span="8"
        clearable />
      <szd-select-form-item-col label="发货单位" v-model="objData.purView.issueUnit" baseField="CORE_UNIT" :szdopt="SR.szdopt" :span="8" clearable />
      <szd-input-form-item-col label="安全库存" v-model="objData.purView.safetyStk" :szdopt="SR.szdopt" :span="8" :maxlength="13" />
      <szd-input-form-item-col label="最小批量" v-model="objData.purView.minLotsize" :szdopt="SR.szdopt" :span="8" :maxlength="13" />
      <szd-input-form-item-col label="最大批量大小" v-model="objData.purView.maxLotsize" :szdopt="SR.szdopt" :span="8" :maxlength="13" />
      <szd-input-form-item-col label="固定批量大小" v-model="objData.purView.fixedLot" :szdopt="SR.szdopt" :span="8" :maxlength="13" />
      <szd-input-form-item-col label="舍入值" v-model="objData.purView.roundVal" :szdopt="SR.szdopt" :span="8" :maxlength="13" />
      <szd-input-form-item-col label="最大库存水平" v-model="objData.purView.maxStock" :szdopt="SR.szdopt" :span="8" :maxlength="13" />
    </el-row>
  </el-form>
</template>

<script>
  export default {
    props: ["objData", "SR"]
  };
</script>
