<template>
  <var-popup
    v-model:show="dg.open"
    position="bottom"
    close-on-click-overlay
    class="szd__popup__wrapper_h"
    style="height: 70vh"
    @close="handleCloseForm">
    <!-- 功能按钮 -->
    <div class="szd__popup__top">
      <var-button-group type="default">
        <szd-button label="保存" type="primary" @click="submitForm" />
        <szd-button label="关闭" type="primary" @click="closeForm" />
      </var-button-group>
    </div>
    <!-- 表单输出 -->
    <div class="szd__popup__main" style="padding: 5px 0">
      <szd-card-a>
        <!--  内容 -->
        <template #main>
          <szd-input label="公司代码" v-model="objData.cmpy" :disabled="dg.option !== 'C'" required :maxlength="10" />
          <szd-input label="公司名称" v-model="objData.cmpyName" required :maxlength="128" />
          <szd-input label="公司简称" v-model="objData.cmpyShort" :maxlength="64" />
          <szd-input
            label="所属账套"
            v-model="objData.accSet"
            showHelp
            :help="{ a: 'CORE_VCH_ACCS', b: objData, c: ['accSet-svalue', 'accSetName-sname'] }"
            required
            :desc="objData.accSetName" />
        </template>
      </szd-card-a>
    </div>
  </var-popup>
</template>
<script>
  import { getCompany, addCompany, updCompany } from "../api/mdmCmpy.js";

  export default {
    data() {
      return {
        dg: {
          title: "",
          open: false,
          option: "V", //C创建 U更改 V显示
          retCode: "", //返回码，S成功(刷新) Q或空取消操作 E 操作错误
          cmpy: ""
        },
        objData: {}
      };
    },
    methods: {
      //接受参数
      callMdmCmpyInfo(infoParam) {
        this.initData();
        this.dg.option = infoParam.option;
        this.dg.cmpy = infoParam.cmpy;
        this.dg.retCode = "";
        if (this.dg.option !== "C") {
          this.getCompanyInfo();
        } else {
          this.dg.open = true;
        }
        this.getDesc();
      },
      //获取后台数据
      getCompanyInfo() {
        getCompany(this.dg.cmpy).then(response => {
          if (response.code === 200) {
            this.objData = response.data;
          }
          this.dg.open = true;
        });
      },
      //获取描述
      getDesc() {
        this.dg.title = this.szdTool.getTitle(this.dg.option) + this.$t("公司");
      },
      //保存数据
      submitForm() {
        if (this.submitFormCheck()) {
          if (this.dg.option === "C") {
            addCompany(this.objData).then(response => {
              if (response.code === 200) {
                this.$mS("新增数据成功");
                this.dg.cmpy = this.objData.cmpy;
                this.dg.option = "U";
                this.dg.retCode = "S";
                this.closeForm();
              }
            });
          } else {
            updCompany(this.objData).then(response => {
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
        if (!this.objData.cmpy || this.objData.cmpy.trim() === "") {
          this.$mE("公司代码不能为空");
          return false;
        }
        if (!this.objData.cmpyName || this.objData.cmpyName.trim() === "") {
          this.$mE("公司名称不能为空");
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
          cmpy: "",
          cmpyName: "",
          cmpyShort: "",
          accSet: "",
          accSetName: ""
        };
      }
    }
  };
</script>