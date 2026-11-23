<template>
  <div class="app-container" v-if="dg.open">
    <div style="padding-bottom: 5px; display: flex">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" icon="refresh" />
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.aufnr && objData.header.released !== '1' && objData.header.closed !== '1'"
        label="下达"
        type="warning"
        @click="releaseSap"
        icon="upload" />
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.aufnr && objData.header.released === '1' && objData.header.closed !== '1'"
        label="关闭"
        type="danger"
        @click="closeSap"
        icon="circle-close" />
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.aufnr && objData.header.closed === '1'"
        label="取消关闭"
        type="warning"
        @click="cancelCloseSap"
        icon="circle-check" />
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
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
        dg: {
          open: false
        },
        SR: {
          routerCode: "Z000_PP_SCDDCJ_01A",
          SRH: "Tab1",
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
      getData() {
        let json = { bussId: this.wfOper.bussId, userId: this.wfOper.userId, option: this.wfOper.option };
        getApply(json).then(response => {
          if (response.code === 200) {
            this.objData = response.data.data;
            this.wfOper = response.data.wfOper;
            this.SR.szdopt = this.wfOper.wfOper.szdopt;
          }
          this.dg.open = true;
        });
      },

      submitForm(wfEvt) {
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
                  this.wfOper.bussId = response.data.bussId;
                  this.szdWf.retInfo(response.data).then(resRet => {
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

      syncSap() {
        syncSapApply(this.objData.header.applyId).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
          }
        });
      },

      releaseSap() {
        this.szdPop("确定要下达该生产订单吗？", true)
          .then(() => {
            releaseApply(this.objData.header.applyId).then(res => {
              if (res.code === 200) {
                this.szdMsgBox(res.data.retMsgBox);
                this.getData();
              }
            });
          })
          .catch(() => {});
      },

      closeSap() {
        this.szdPop("确定要关闭该生产订单吗？", true)
          .then(() => {
            closeApply(this.objData.header.applyId, "C").then(res => {
              if (res.code === 200) {
                this.szdMsgBox(res.data.retMsgBox);
                this.getData();
              }
            });
          })
          .catch(() => {});
      },

      cancelCloseSap() {
        this.szdPop("确定要取消关闭该生产订单吗？", true)
          .then(() => {
            closeApply(this.objData.header.applyId, "R").then(res => {
              if (res.code === 200) {
                this.szdMsgBox(res.data.retMsgBox);
                this.getData();
              }
            });
          })
          .catch(() => {});
      },

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
        this.dg.open = true;
      }
    }
  };
</script>
