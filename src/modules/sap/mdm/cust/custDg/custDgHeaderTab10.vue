<template>
  <el-form class="app-el-form">
    <el-row>
      <szd-input-form-item-col label="业务单号" :span="8" v-model="objData.header.bussDocId" disabled />
      <szd-checkbox-form-item-col
        label="客商区分"
        v-model="objData.header.custFlag"
        :szdopt="SR.szdopt"
        required
        :baseArr="[
          { svalue: 'K', sname: '客户' },
          { svalue: 'D', sname: '供应商' }
        ]"
        :span="8" />
      <szd-input-form-item-col
        label="客商编码"
        v-model="objData.header.custId"
        :szdopt="SR.szdopt"
        disabled
        :span="8"
        dblclick
        @dblclick="handleGoToSap(objData.header.custId)" />
    </el-row>
    <el-row>
      <szd-input-form-item-col label="客商全称" v-model="objData.header.custName" :szdopt="SR.szdopt" required :span="8" :maxlength="64" />
      <szd-input-form-item-col label="客商简称" v-model="objData.header.custNameShort" :szdopt="SR.szdopt" required :span="8" :maxlength="64" />
      <szd-input-form-item-col label="税号/身份证" v-model="objData.header.idNum" :szdopt="SR.szdopt" required :span="8" :maxlength="32" />
      <szd-select-form-item-col
        label="客商分组"
        v-model="objData.header.custGrp"
        baseField="Z000_MDM_CUST_BUSS_GROUP"
        :szdopt="SR.szdopt"
        required
        clearable
        :span="8" />
      <szd-select-form-item-col
        label="伙伴类型"
        v-model="objData.header.partnerType"
        baseField="Z000_MDM_CUST_BUSS_TYPE"
        :szdopt="SR.szdopt"
        required
        clearable
        :span="8" />
      <!--      <szd-input-form-item-col label="U8旧编码" v-model="objData.header.u8Code" :szdopt="SR.szdopt" :span="8" :maxlength="32" />-->
      <szd-input-form-item-col
        label="国家"
        :span="8"
        v-model="objData.header.ctry"
        :szdopt="SR.szdopt"
        required
        :descSpan="14"
        :desc="objData.header.ctryName"
        clearable
        readonly
        showHelp
        @help="szdSh({ a: 'CORE_CTRY', b: objData.header, c: ['ctry-ctry', 'ctryName-ctryName'] })" />
      <szd-input-form-item-col label="平台省份" v-model="objData.header.provinceCore" :span="8" v-show="false" />
      <szd-input-form-item-col
        label="省份"
        :span="8"
        v-model="objData.header.province"
        required
        :szdopt="SR.szdopt"
        :descSpan="14"
        :desc="objData.header.provinceName"
        readonly
        clearable
        showHelp
        @help="szdSh({ a: 'CORE_PROV', b: objData.header, c: ['provinceCore-prov', 'province-prov', 'provinceName-provName'] })" />
      <szd-input-form-item-col
        label="城市"
        :span="8"
        v-model="objData.header.city"
        required
        :szdopt="SR.szdopt"
        :descSpan="14"
        :desc="objData.header.cityName"
        showHelp
        readonly
        clearable
        @help="
          szdSh({ a: 'CORE_CITY', b: objData.header, c: ['city-cityId', 'cityName-cityName'], d: [['provinceId', objData.header.provinceCore]] })
        " />
      <szd-input-form-item-col label="邮政编码" v-model="objData.header.postCode" :szdopt="SR.szdopt" :span="8" :maxlength="200" required />
      <szd-input-form-item-col label="详细地址" v-model="objData.header.address" :szdopt="SR.szdopt" :span="8" :maxlength="200" required />
      <szd-input-form-item-col label="联系人" v-model="objData.header.contact" :szdopt="SR.szdopt" :span="8" :maxlength="32" required />
      <szd-input-form-item-col label="电话" v-model="objData.header.phone" :szdopt="SR.szdopt" :span="8" :maxlength="32" required />
      <szd-select-form-item-col
        label="税分类"
        v-model="objData.header.taxClass"
        baseField="Z000_MDM_TAX_CLASS"
        :szdopt="SR.szdopt"
        :span="8"
        required
        clearable />
      <szd-select-form-item-col
        label="信用控制"
        v-if="!['A', 'B'].includes(objData.header.bussStatus) && objData.header.custFlag.includes('K')"
        v-model="objData.header.creditFlag"
        baseField="CORE_SYS_ACTV"
        :szdopt="SR.szdopt"
        clearable
        :span="8" />
      <szd-select-form-item-col
        label="风险类"
        v-model="objData.header.riskClass"
        baseField="Z000_MDM_CUST_RISK"
        :szdopt="SR.szdopt"
        :span="8"
        required
        clearable />
    </el-row>
    <el-row>
      <szd-upload-view-form-item-col
        label-width="11%"
        required
        :szdopt="SR.szdopt"
        v-model="objData.header.fileObjId"
        label="营业执照"
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
      // 跳转SAP
      handleGoToSap(custId) {
        if (custId) {
          this.szdSapTool.goSapTCode(
            "BP",
            {
              "BUS_JOEL_SEARCH-PARTNER_NUMBER": custId
            },
            "=BUS_LOCATOR_SRCH_GO"
          );
        }
      }
    }
  };
</script>
