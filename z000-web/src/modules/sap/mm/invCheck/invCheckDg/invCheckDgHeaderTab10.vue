<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col label="业务单号" :span="8" v-model="objData.header.bussDocId" disabled />
      <szd-input-form-item-col
        label="物料凭证业务单号"
        :span="8"
        v-model="objData.header.storeBussDocId"
        disabled
        dblclick
        @dblclick="handleGoToStore" />
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
      <szd-date-form-item-col label="凭证日期" v-model="objData.header.vchDate" :szdopt="SR.szdopt" required :span="8" />
      <szd-date-form-item-col label="过帐日期" v-model="objData.header.postDate" :szdopt="SR.szdopt" required :span="8" />
      <szd-input-form-item-col label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" required :span="8" :maxlength="255" />
      <szd-select-form-item-col label="货币码" v-model="objData.header.curr" baseField="CORE_CURR" :szdopt="SR.szdopt" required :span="8" clearable />
      <szd-input-form-item-col label="总发票金额" v-model="objData.header.invAmt" disabled :span="8" type="number" numDot :numDec="2" />
      <szd-select-form-item-col
        label="付款条件"
        v-model="objData.header.terms"
        baseField="Z000_MM_FPJY_PMTTRMS"
        required
        clearable
        :szdopt="SR.szdopt"
        :span="8" />
      <szd-date-form-item-col label="基准日期" v-model="objData.header.blineDate" :szdopt="SR.szdopt" :span="8" required />
      <szd-select-form-item-col
        label="付款方式"
        v-model="objData.header.payWay"
        baseField="Z000_MM_FPJY_PYMTMETH"
        required
        clearable
        :szdopt="SR.szdopt"
        :span="8" />
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
    <el-row>
      <szd-upload-view-form-item-col
        label-width="11%"
        required
        :szdopt="SR.szdopt"
        v-model="objData.header.fileObjId"
        label="附件"
        fileCate="LICENS"
        listType="picture-card"
        acceptTxt="附件"
        :cardWidth="100"
        :cardHeight="100"
        :limit="10"
        :span="18" />
    </el-row>
  </el-form>
</template>
<script>
  export default {
    props: ["objData", "SR"],
    components: {},
    data() {
      return {};
    },
    methods: {
      // 跳转物料凭证
      handleGoToStore() {
        if (this.objData.header.storeBussDocId) {
          this.szdWf.goDocId({ bstp: "Z000_MM_CGRK01", bussDocId: this.objData.header.storeBussDocId, option: "V" });
        }
      }
    }
  };
</script>
