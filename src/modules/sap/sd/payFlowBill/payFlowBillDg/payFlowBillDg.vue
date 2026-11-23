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
          label="持票账户"
          v-model="objData.holderAcc"
          required
          :disabled="dg.option !== 'C'"
          @help="szdSh({ a: 'Z000_BANK_ACC', b: objData, c: ['holderAcc-accNo'] })"
          showHelp
          :span="12" />
        <szd-input-form-item-col label="系统票号" v-model="objData.sysBillNo" disabled :span="12" />
        <szd-input-form-item-col label="票包号" v-model="objData.billNo" required :disabled="dg.option !== 'C'" :span="12" />
        <szd-input-form-item-col label="子票区间从" v-model="objData.subBillFrom" required :disabled="dg.option !== 'C'" :span="12" />
        <szd-input-form-item-col label="子票区间至" v-model="objData.subBillTo" required :disabled="dg.option !== 'C'" :span="12" />
        <szd-date-form-item-col label="出票日期" v-model="objData.issueDate" type="date" required :span="12" />
        <szd-date-form-item-col label="到期日期" v-model="objData.dueDate" type="date" required :span="12" />
        <szd-select-form-item-col label="票据类型" v-model="objData.billType" required baseField="Z000_BILL_TYPE" :span="12" />
        <szd-select-form-item-col label="票据范围" v-model="objData.billScope" required baseField="Z000_BILL_RANGE" :span="12" />

        <szd-input-form-item-col label="金额" v-model="objData.amt" required type="number" :numDec="2" numDot :span="12" />
        <szd-input-form-item-col label="承兑人" v-model="objData.acceptor" required :span="12" />
        <szd-date-form-item-col label="签收日期" v-model="objData.signDate" type="date" required :span="12" />

        <szd-input-form-item-col label="前手账号" :span="12" v-model="objData.endorserAcc" />
        <szd-input-form-item-col label="前手账号名称" :span="12" v-model="objData.endorser" required />
        <szd-input-form-item-col label="出票人" :span="12" v-model="objData.drawer" />
        <szd-input-form-item-col label="收票人" :span="12" v-model="objData.payee" />
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
  import { getInfo, save, update } from "../api/payFlowBill.js";

  export default {
    components: {},
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
        this.dg.sysBillNo = infoParam.sysBillNo;
        if (this.dg.option !== "C") {
          this.getInfos();
        } else {
          this.dg.open = true;
        }
        this.getDesc();
      },

      //获取后台数据
      getInfos() {
        getInfo({ sysBillNo: this.dg.sysBillNo }).then(response => {
          if (response.code === 200) {
            this.objData = response.data;
          }
          this.dg.open = true;
        });
      },

      //获取描述
      getDesc() {
        this.dg.title = this.szdTool.getTitle(this.dg.option) + "票据承兑";
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
        if (this.szdCheck.nullMsg$t(this.objData.holderAcc, "持票账户不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.billNo, "票包号不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.subBillFrom, "子票区间从不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.subBillTo, "子票区间至不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.issueDate, "出票日期不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.dueDate, "到期日期不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.billType, "票据类型不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.billScope, "票据范围不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.amt, "金额不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.acceptor, "承兑人不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.signDate, "签收日期不能为空")) {
          return false;
        }
        if (this.szdCheck.nullMsg$t(this.objData.endorser, "前手账号名称不能为空")) {
          return false;
        }
        if (this.objData.subBillFrom > this.objData.subBillTo) {
          this.$mE("子票区间从不能大于子票区间至");
          return false;
        }
        if (this.objData.issueDate > this.objData.dueDate) {
          this.$mE("出票日期不能大于到期日期");
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
