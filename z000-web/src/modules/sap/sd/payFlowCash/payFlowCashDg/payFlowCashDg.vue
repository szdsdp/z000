<template>
  <el-dialog
    :title="dg.title"
    v-model="dg.open"
    append-to-body
    :close-on-click-modal="false"
    @close="handleCloseForm"
    class="app-dialog-container"
    align-center
    draggable>
    <div v-szd-dialog="[1000, 500]" />
    <el-form class="app-el-form">
      <el-row>
        <szd-input-form-item-col
          label="公司代码"
          v-model="objData.cmpy"
          required
          :disabled="dg.option !== 'C'"
          @help="szdSh({ a: 'CORE_CMPY', b: objData, c: ['cmpy', 'cmpyName'] })"
          showHelp
          :span="12"
          :descSpan="12"
          :desc="objData.cmpyName" />
        <szd-input-form-item-col
          label="本方账户"
          v-model="objData.myAccNo"
          required
          :disabled="dg.option !== 'C'"
          @help="szdSh({ a: 'Z000_BANK_ACC', b: objData, c: ['myAccNo-accNo', 'curr-curr'] })"
          showHelp
          :span="12" />
        <szd-input-form-item-col label="银行回单号" v-model="objData.bankDoc" disabled :span="12" />
        <szd-input-form-item-col label="银行流水号" v-model="objData.bankFlowNo" required :disabled="dg.option !== 'C'" :span="12" />
        <szd-input-form-item-col label="对方账户" v-model="objData.payAccNo" required :span="12" />
        <szd-input-form-item-col label="对方户名" v-model="objData.payAccName" required :span="12" />
        <szd-input-form-item-col label="金额" v-model="objData.amt" type="number" :numDec="2" numDot required :span="12" />
        <szd-select-form-item-col label="币种" v-model="objData.curr" baseField="CORE_CURR" required :span="12" />
        <szd-date-form-item-col label="交易日期" v-model="objData.tranDate" type="date" required :span="12" />
        <szd-time-form-item-col label="交易时间" v-model="objData.tranTime" :span="12" />
        <szd-input-form-item-col label="银行摘要" :span="12" v-model="objData.summary" />
        <szd-input-form-item-col
          label="客户编码"
          v-model="objData.cust"
          @help="szdSh({ a: 'CORE_CUST', b: objData, c: ['cust', 'custName'] })"
          showHelp
          :desc="objData.custName"
          :span="12"
          :descSpan="12" />
      </el-row>
    </el-form>

    <template #footer>
      <szd-button v-if="dg.option !== 'V'" label="确定" type="primary" @click="submitForm" />
      <szd-button label="取消" type="primary" @click="closeForm" />
    </template>
  </el-dialog>
</template>

<script>
  import { getInfo, save, update } from "../api/payFlowCash.js";

  export default {
    data() {
      return {
        dg: {
          title: "",
          open: false,
          option: "V", //C创建 U更改 V显示
          retcode: "" //返回码，S成功(刷新) Q或空取消操作 E 操作错误
        },
        objData: {}
      };
    },
    methods: {
      //接受参数
      acceptData(infoParam) {
        this.objData = {};
        this.dg.option = infoParam.option;
        this.dg.bankDoc = infoParam.bankDoc;
        if (this.dg.option !== "C") {
          this.getInfos();
        } else {
          this.dg.open = true;
        }
        this.getDesc();
      },

      //获取后台数据
      getInfos() {
        getInfo({ bankDoc: this.dg.bankDoc }).then(response => {
          if (response.code === 200) {
            this.objData = response.data;
          }
          this.dg.open = true;
        });
      },

      //获取描述
      getDesc() {
        this.dg.title = this.szdTool.getTitle(this.dg.option) + "电汇回款";
      },

      // 保存数据
      submitForm() {
        if (this.submitFormCheck()) {
          if (this.dg.option === "C") {
            save(this.objData).then(response => {
              if (response.code === 200) {
                this.$mS("新增数据成功");
                this.dg.option = "U";
                this.dg.retcode = "S";
                this.closeForm();
              }
            });
          } else {
            update(this.objData).then(response => {
              if (response.code === 200) {
                this.$mS("修改数据成功");
                this.dg.retcode = "S";
                this.closeForm();
              }
            });
          }
        }
      },

      //检查提交数据
      submitFormCheck() {
        if (this.szdCheck.nullMsg$t(this.objData.cmpy, "公司代码不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.myAccNo, "本方账户不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.bankFlowNo, "银行流水号不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.payAccNo, "对方账户不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.payAccName, "对方户名不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.amt, "金额不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.curr, "币种不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.tranDate, "交易日期不能为空")) {
          return false;
        }
        return true;
      },

      //关闭窗口
      closeForm() {
        this.dg.open = false;
      },
      handleCloseForm() {
        this.$emit("updResult", this.dg);
      }
    }
  };
</script>
