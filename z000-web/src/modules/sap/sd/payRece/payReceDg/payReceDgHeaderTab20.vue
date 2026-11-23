<template>
  <div class="app-sub-container" style="padding: 0">
    <el-form class="app-el-form">
      <el-row>
        <szd-date-form-item-col label="业务日期" v-model="objData.header.bussDate" type="date" disabled :span="8" />
        <szd-input-form-item-col label="成本中心" v-model="objData.header.cstc" disabled :span="8" :descSpan="14" :desc="objData.header.cstcName" />
        <szd-input-form-item-col label="申请人" v-model="objData.header.userId" disabled :span="8" :descSpan="14" :desc="objData.header.userName" />
      </el-row>
      <el-row>
        <szd-input-form-item-col label="凭证号" v-model="objData.header.vchNum" disabled :span="8" dblclick @dblclick="handleGoToSap(objData.header)" />
        <szd-input-form-item-col label="凭证财年" v-model="objData.header.vchYear" disabled :span="8" />
      </el-row>
      <el-row>
        <szd-input-form-item-col label="冲销凭证号" v-model="objData.header.vchNumReverse" disabled :span="8" dblclick @dblclick="handleGoToSapReverse(objData.header)" />
        <szd-input-form-item-col label="冲销凭证财年" v-model="objData.header.vchYearReverse" disabled :span="8" />
      </el-row>
      <el-row>
        <szd-input-form-item-col label="SAP消息ID" v-model="objData.header.sapMessId" disabled :span="8" />
        <szd-input-form-item-col label="SAP消息" v-model="objData.header.sapMsg" disabled :span="8" />
      </el-row>
      <el-row>
        <szd-input-form-item-col label="创建人" v-model="objData.header.createBy" disabled :span="8" :descSpan="14" :desc="objData.header.createByName" />
        <szd-date-form-item-col label="创建时间" v-model="objData.header.createTime" type="datetime" disabled :span="8" />
      </el-row>
    </el-form>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() { return {}; },
    methods: {
      // 跳转SAP（凭证）
      handleGoToSap(header) {
        if (header) {
          this.szdSapTool.goSapTCode("FB03", {
            "RF05L-BELNR": header.vchNum,
            "RF05L-GJAHR": header.vchYear,
            "RF05L-BUKRS": header.cmpy
          });
        }
      },
      // 跳转SAP（冲销凭证）
      handleGoToSapReverse(header) {
        if (header) {
          this.szdSapTool.goSapTCode("FB03", {
            "RF05L-BELNR": header.vchNumReverse,
            "RF05L-GJAHR": header.vchYearReverse,
            "RF05L-BUKRS": header.cmpy
          });
        }
      }
    }
  };
</script>
