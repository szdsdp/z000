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
        v-if="objData.header.bussStatus === 'F' && objData.header.aufnr && objData.header.released !== '1' && objData.header.closed !== '1'"
        label="下达"
        type="warning"
        @click="releaseSap" />
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.aufnr && objData.header.released === '1' && objData.header.closed !== '1'"
        label="关闭"
        type="danger"
        @click="closeSap" />
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.aufnr && objData.header.closed === '1'"
        label="取消关闭"
        type="warning"
        @click="cancelCloseSap" />
      <!-- 工作流按钮 -->
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" @submit="submitForm" />
    </div>
    <!-- 抬头（含标签页） -->
    <orderCreateDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply, releaseApply, closeApply } from "../api/orderCreate.js";
  import { submitFormCheck } from "./js/orderCreateCheck";
  import orderCreateDgHeader from "./orderCreateDgHeader.vue";

  export default {
    name: "Z000_PP_SCDDCJ_01A",
    components: {
      orderCreateDgHeader
    },
    data() {
      return {
        loading: false,
        SR: {
          routerCode: "Z000_PP_SCDDCJ_01A",
          actTab: "Tab1",
          szdopt: "U"
        },
        wfOper: {},
        objData: {
          header: {}
        }
      };
    },
    created() {
      this.szdWf.getUrlParams(this.wfOper);
      // 设置标题
      if (this.wfOper.option == "V") {
        this.pageTitle = "查看生产订单";
      } else if (this.wfOper.option == "U") {
        this.pageTitle = "修改生产订单";
      } else {
        this.pageTitle = "新增生产订单";
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
        // 已产生产订单，不允许撤回/作废
        if ((wfEvt.operate === "btn_revoke" || wfEvt.operate === "btn_delete") && this.objData.header.aufnr) {
          const action = wfEvt.operate === "btn_revoke" ? "撤回" : "作废";
          this.$mW(`已产生产订单，无法${action}！`);
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
                  // 首次保存后更新URL
                  if (!this.wfOper.bussId && response.data.bussId) {
                    this.szdWf.replaceUrl(response.data.bussId);
                  }
                  this.wfOper.bussId = response.data.bussId;
                  this.objData.header.bussId = response.data.bussId;
                  this.szdWf.retInfo(response.data, wfEvt).then(resRet => {
                    if (resRet) {
                      if (wfEvt.submitFlag) {
                        // 保存后自动提交
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
        syncSapApply(this.objData.header.applyId).then(res => {
          if (res.code === 200) {
            this.szdWf.retMsg(res.data);
            this.getData();
          }
        });
      },
      // 下达生产订单
      releaseSap() {
        this.szdPop("确定要下达该生产订单吗？", true)
          .then(() => {
            releaseApply(this.objData.header.applyId).then(res => {
              if (res.code === 200) {
                this.szdWf.retMsg(res.data);
                this.getData();
              }
            });
          })
          .catch(() => {});
      },
      // 关闭生产订单
      closeSap() {
        this.szdPop("确定要关闭该生产订单吗？", true)
          .then(() => {
            closeApply(this.objData.header.applyId, "C").then(res => {
              if (res.code === 200) {
                this.szdWf.retMsg(res.data);
                this.getData();
              }
            });
          })
          .catch(() => {});
      },
      // 取消关闭
      cancelCloseSap() {
        this.szdPop("确定要取消关闭该生产订单吗？", true)
          .then(() => {
            closeApply(this.objData.header.applyId, "R").then(res => {
              if (res.code === 200) {
                this.szdWf.retMsg(res.data);
                this.getData();
              }
            });
          })
          .catch(() => {});
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
          createBy: userData.userId,
          createByName: userData.userName
        };
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
