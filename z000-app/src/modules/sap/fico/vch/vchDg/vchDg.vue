<template>
  <div class="szd__tb__wrapper">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-container">
        <span class="title-bar"></span>
        <var-text size="large" type="primary">{{ pageTitle }}</var-text>
      </div>
    </div>
    <!-- 功能按钮 -->
    <div class="szd__tb__top" style="padding-top: 30px; padding-bottom: 20px">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" />
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQR" :wfOper="wfOper" :bussBase="objData.header" @submit="submitForm" />
    </div>
    <!-- 抬头（含标签页） -->
    <vchDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply, genVchView } from "../api/vch.js";
  import { submitFormCheck } from "./js/vchCheck";
  import vchDgHeader from "./vchDgHeader.vue";

  export default {
    name: "Z000_FI_VCH_01A",
    components: { vchDgHeader },
    data() {
      return {
        loading: false,
        SR: { routerCode: "Z000_FI_VCH_01A", actTab: "Tab1", szdopt: "U" },
        wfOper: {},
        objData: { header: {}, itemList: [], adjustList: [] }
      };
    },
    created() {
      this.szdWf.getUrlParams(this.wfOper);
      // 设置标题
      if (this.wfOper.option == "V") {
        this.pageTitle = "查看费用报销";
      } else if (this.wfOper.option == "U") {
        this.pageTitle = "修改费用报销";
      } else {
        this.pageTitle = "新增费用报销";
      }
      if (!this.wfOper.bussId) {
        // 新建模式：初始化默认数据
        this.szdWf.init(this.SR.routerCode).then(res => {
          this.wfOper = res;
          if (res.bstp) {
            this.initDataDef(res);
          }
        });
      } else {
        // 查看/编辑模式：加载数据
        this.getData();
      }
    },
    methods: {
      // 获取数据
      getData() {
        getApply({ bussId: this.wfOper.bussId, userId: this.wfOper.userId, option: this.wfOper.option }).then(response => {
          if (response.code === 200) {
            this.objData = response.data.data;
            this.wfOper = response.data.wfOper;
            this.SR.szdopt = this.wfOper.wfOper.szdopt;
          }
        });
      },
      // 预览凭证
      wfVchView() {
        genVchView(this.objData.header.bussId).then(res => {
          if (res.code === 200) {
            this.szdWf.vchView(res.data);
          }
        });
      },
      // 提交表单
      submitForm(wfEvt) {
        if (this.loading === false && (this.szdWf.btnCheck(wfEvt) || submitFormCheck(this.objData))) {
          this.loading = true;
          if (wfEvt.operate === "btn_check") {
            checkApply(this.objData, wfEvt)
              .then(response => {
                if (response.code === 200) {
                  this.szdMsgBox(response.data.retMsgBox);
                  if (response.data.msg) {
                    if (response.data.status === "S") {
                      this.$mS(response.data.msg);
                    } else if (response.data.status === "E") {
                      this.$mE(response.data.msg);
                    } else if (response.data.status === "W") {
                      this.$mW(response.data.msg);
                    }
                  }
                }
              })
              .finally(() => (this.loading = false));
          } else {
            saveApply(this.objData, wfEvt)
              .then(response => {
                if (response.code === 200) {
                  if (!this.wfOper.bussId && response.data.bussId) {
                    this.szdWf.replaceUrl(response.data.bussId);
                  }
                  this.wfOper.bussId = response.data.bussId;
                  this.objData.header.bussId = response.data.bussId;
                  this.szdWf.retInfo(response.data, wfEvt).then(resRet => {
                    if (resRet) {
                      if (resRet !== true && resRet.nextUserId) {
                        wfEvt.nextUserId = resRet.nextUserId;
                        this.submitForm(wfEvt);
                      } else {
                        this.getData();
                      }
                    }
                  });
                }
              })
              .finally(() => (this.loading = false));
          }
        }
      },
      // 同步SAP
      syncSap() {
        syncSapApply(this.objData.header.costId).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
          }
        });
      },
      // 初始化新建单据默认数据
      initDataDef(res) {
        const userData = this.$store.state.loginUser.user;
        this.objData.header = {
          routerCode: this.SR.routerCode,
          bstp: res.bstp,
          bstpName: res.bstpName,
          bussDate: this.szdDate.getDay(),
          bussStatus: "A",
          wfDyId: this.wfOper.wfDef.wfDyId,
          wfDyName: this.wfOper.wfDef.wfDyName,
          cmpy: userData.cmpy,
          cmpyName: userData.cmpyName,
          cstc: userData.cstc,
          cstcName: userData.cstcName,
          curr: "CNY"
        };
        this.objData.itemList = [];
        this.objData.adjustList = [];
      }
    }
  };
</script>
<style scoped>
  .page-header {
    text-align: left;
    font-weight: bold;
    padding: 10px;
    margin-bottom: -20px;
  }
  .title-container {
    display: flex;
    align-items: center;
  }
  .title-bar {
    width: 4px;
    height: 20px;
    background-color: #007bff;
    margin-right: 8px;
    border-radius: 2px;
  }
</style>
