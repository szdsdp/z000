<template>
  <div class="app-container" v-if="dg.open">
    <div style="padding-bottom: 5px; display: flex">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" icon="refresh" />
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.reBelnr && !objData.header.revBelnr"
        label="冲销"
        type="warning"
        @click="reverseSap"
        icon="refresh-left" />
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <invCheckDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <invCheckDgItem :objData="objData" :SR="SR" :wfOper="wfOper" @update-gross="updateInvAmt" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply, reverseApply } from "../api/invCheck.js";
  import { submitFormCheck } from "./js/invCheckCheck";
  import invCheckDgHeader from "./invCheckDgHeader.vue";
  import invCheckDgItem from "./invCheckDgItem.vue";

  export default {
    name: "Z000_MM_FPJY_01A",
    components: { invCheckDgHeader, invCheckDgItem },
    data() {
      return {
        loading: false,
        dg: { open: false },
        SR: {
          routerCode: "Z000_MM_FPJY_01A",
          SRH_height: 350,
          SRH: "Tab1",
          SRI: "Tab1",
          szdopt: "U"
        },
        wfOper: {},
        objData: { header: {}, itemList: [] }
      };
    },
    created() {
      this.szdWf.getUrlParams(this.wfOper);
      if (!this.wfOper.bussId) {
        this.szdWf.init(this.SR.routerCode).then(res => {
          this.wfOper = res;
          if (res.bstp) this.initDataDef(res);
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
        if ((wfEvt.operate === "btn_revoke" || wfEvt.operate === "btn_delete") && this.objData.header.reBelnr && !this.objData.header.revReBelnr) {
          const action = wfEvt.operate === "btn_revoke" ? "撤回" : "作废";
          this.$mW(`已产生发票凭证，无法${action}，请执行冲销操作`);
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
                    if (response.data.status === "S") this.$mS(response.data.msg);
                    else if (response.data.status === "E") this.$mE(response.data.msg);
                    else if (response.data.status === "W") this.$mW(response.data.msg);
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
        syncSapApply(this.objData.header.invId).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
          }
        });
      },
      reverseSap() {
        this.szdPop("确定要对该发票校验单据执行冲销操作吗？", true)
          .then(() => {
            reverseApply(this.objData.header.invId).then(res => {
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
          createByName: userData.userName,
          curr: "CNY"
        };
        this.objData.itemList = [];
        this.dg.open = true;
      },
      // 更新总金额（Σ金额 + Σ税收金额）
      updateInvAmt() {
        let total = 0;
        if (this.objData.itemList) {
          this.objData.itemList.forEach(item => {
            total += parseFloat(item.amt) || 0;
            total += parseFloat(item.taxAmount) || 0;
          });
        }
        this.objData.header.invAmt = total.toFixed(2);
      }
    }
  };
</script>
