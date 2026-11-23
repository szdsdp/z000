<template>
  <el-dialog
    :title="dg.title"
    v-model="dg.open"
    append-to-body
    :close-on-click-modal="false"
    @close="handleCloseForm"
    class="app-dialog-container"
    align-center
    destroy-on-close
    draggable>
    <div v-szd-dialog="[500, 300]" />
    <el-form class="app-el-form">
      <el-row>
        <szd-input-form-item-col label="银行账户" v-model="objData.accNo" :disabled="dg.option !== 'C'" required :span="24" :maxlength="50" />
        <szd-input-form-item-col label="账户名称" v-model="objData.accName" :span="24" :maxlength="100" />
        <szd-input-form-item-col
          label="公司代码"
          v-model="objData.cmpy"
          showHelp
          clearable
          :help="{ a: 'CORE_CMPY', b: objData, c: ['cmpy-svalue', 'cmpyName-sname'] }"
          required
          :span="24"
          :descSpan="12"
          :desc="objData.cmpyName" />
        <szd-input-form-item-col
          label="币种"
          v-model="objData.curr"
          showHelp
          clearable
          :help="{ a: 'CORE_CURR', b: objData, c: ['curr-svalue', 'currName-sname'] }"
          :span="24"
          :descSpan="12"
          :desc="objData.currName" />
        <szd-input-form-item-col
          required
          label="会计科目"
          v-model="objData.accs"
          showHelp
          clearable
          :help="{ a: 'CORE_ACCS', b: objData, c: ['accs', 'accsName'] }"
          :span="24"
          :descSpan="12"
          :desc="objData.accsName" />
        <szd-input-form-item-col label="联行号" v-model="objData.bankCode" :span="24" :maxlength="50" />
      </el-row>
    </el-form>

    <template #footer>
      <szd-button label="保存" type="primary" @click="submitForm" />
      <szd-button label="关闭" type="primary" @click="closeForm" />
    </template>
  </el-dialog>
</template>
<script>
  import { getBankAcc, addBankAcc, updBankAcc } from "./api/bankAcc.js";

  export default {
    data() {
      return {
        dg: {
          title: "",
          open: false,
          option: "V", //C创建 U更改 V显示
          retCode: "", //返回码，S成功(刷新) Q或空取消操作 E 操作错误
          accNo: ""
        },
        objData: {}
      };
    },
    methods: {
      //接受参数
      callBankAccInfo(infoParam) {
        this.initData();
        this.dg.option = infoParam.option;
        this.dg.accNo = infoParam.accNo;
        this.dg.retCode = "";
        if (this.dg.option !== "C") {
          this.getBankAccInfo();
        } else {
          this.dg.open = true;
        }
        this.getDesc();
      },
      //获取后台数据
      getBankAccInfo() {
        getBankAcc(this.dg.accNo).then(response => {
          if (response.code === 200) {
            this.objData = response.data;
          }
          this.dg.open = true;
        });
      },
      //获取描述
      getDesc() {
        this.dg.title = this.szdTool.getTitle(this.dg.option) + this.$t("本方账户");
      },
      //保存数据
      submitForm() {
        if (this.submitFormCheck()) {
          if (this.dg.option === "C") {
            addBankAcc(this.objData).then(response => {
              if (response.code === 200) {
                this.$mS("新增数据成功");
                this.dg.accNo = this.objData.accNo;
                this.dg.option = "U";
                this.dg.retCode = "S";
                this.closeForm();
              }
            });
          } else {
            updBankAcc(this.objData).then(response => {
              if (response.code === 200) {
                this.$mS("修改数据成功");
                this.dg.retCode = "S";
                this.closeForm();
              }
            });
          }
        }
      },
      //检查提交数据
      submitFormCheck() {
        if (!this.objData.accNo || this.objData.accNo.trim() === "") {
          this.$mE("银行账户不能为空");
          return false;
        }
        if (!this.objData.cmpy || this.objData.cmpy.trim() === "") {
          this.$mE("公司代码不能为空");
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
      },
      //初始化数据
      initData() {
        this.objData = {
          accNo: "",
          accName: "",
          cmpy: "",
          cmpyName: "",
          curr: "",
          currName: "",
          accs: "",
          accsName: "",
          bankCode: ""
        };
      }
    }
  };
</script>
