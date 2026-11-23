<template>
  <div class="szd__tb__wrapper">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-container">
        <span class="title-bar"></span>
        <var-text size="large" type="primary">{{ this.pageTitle }}</var-text>
      </div>
    </div>
    <!-- 功能按钮 -->
    <div class="szd__tb__top" style="padding-top: 30px; padding-bottom: 20px">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" />
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.confno && !objData.header.revConfcnt"
        label="冲销"
        type="warning"
        @click="reverseSap" />
      <!-- 工作流按钮 -->
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" @submit="submitForm" />
    </div>
    <!-- 抬头（含标签页） -->
    <orderReportDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply, reverseApply } from "../api/orderReport.js";
  import { submitFormCheck } from "./js/orderReportCheck";
  import orderReportDgHeader from "./orderReportDgHeader.vue";

  export default {
    name: "Z000_PP_SCDDBG_01A",
    components: {
      orderReportDgHeader
    },
    data() {
      return {
        loading: false,
        SR: {
          routerCode: "Z000_PP_SCDDBG_01A",
          actTab: "Tab1",
          szdopt: "U"
        },
        wfOper: {},
        objData: {
          header: {},
          itemList: []
        }
      };
    },
    created() {
      this.szdWf.getUrlParams(this.wfOper);
      // 设置标题
      if (this.wfOper.option == "V") {
        this.pageTitle = "查看生产报工";
      } else if (this.wfOper.option == "U") {
        this.pageTitle = "修改生产报工";
      } else {
        this.pageTitle = "新增生产报工";
      }
      if (!this.wfOper.bussId) {
        this.szdWf.init(this.SR.routerCode).then(res => {
          this.wfOper = res;
          if (res.bstp) {
            this.initDataDef(res);
          }
        });
      } else {
        this.getData();
      }
    },
    methods: {
      // 获取数据
      getData() {
        let json = { bussId: this.wfOper.bussId, userId: this.wfOper.userId, option: this.wfOper.option };
        getApply(json).then(response => {
          if (response.code === 200) {
            this.objData = response.data.data;
            this.wfOper = response.data.wfOper;
            this.SR.szdopt = this.wfOper.wfOper.szdopt;
          }
        });
      },
      // 提交表单
      submitForm(wfEvt) {
        if ((wfEvt.operate === "btn_revoke" || wfEvt.operate === "btn_delete") && this.objData.header.confno && !this.objData.header.revConfcnt) {
          const action = wfEvt.operate === "btn_revoke" ? "撤回" : "作废";
          this.$mW(`已产生确认编号，无法${action}，请执行冲销操作`);
          return;
        }
        if (this.loading === false && (this.szdWf.btnCheck(wfEvt) || submitFormCheck(this.objData))) {
          this.loading = true;
          if (wfEvt.operate === "btn_check") {
            checkApply(this.objData, wfEvt)
              .then(response => {
                if (response.code === 200) {
                  this.szdWf.retMsg(response.data);
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
                      if (wfEvt.submitFlag) {
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
        syncSapApply(this.objData.header.reportId).then(res => {
          if (res.code === 200) {
            this.szdWf.retMsg(res.data);
            this.getData();
          }
        });
      },
      // 冲销报工
      reverseSap() {
        this.szdPop("确定要冲销该报工单吗？", true)
          .then(() => {
            reverseApply(this.objData.header.reportId).then(res => {
              if (res.code === 200) {
                this.szdWf.retMsg(res.data);
                this.getData();
              }
            });
          })
          .catch(() => {});
      },
      // 初始化数据
      initDataDef(res) {
        const userData = this.$store.state.loginUser.user;
        this.objData.header = {
          routerCode: this.SR.routerCode,
          bstp: res.bstp,
          bstpName: res.bstpName,
          bussDate: this.szdDate.getDay(),
          bussStatus: "A",
          // wfDef 为缺省值
          // 创建单据时初始化参数，后端返回前端页面使用
          wfDyId: this.wfOper.wfDef.wfDyId,
          wfDyName: this.wfOper.wfDef.wfDyName,

          cmpy: userData.cmpy,
          cmpyName: userData.cmpyName,
          cstc: userData.cstc,
          cstcName: userData.cstcName,
          createBy: userData.userId,
          createByName: userData.userName
        };
        this.objData.itemList = [];
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
