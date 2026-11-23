<template>
  <div class="app-container" v-if="dg.open">
    <div style="padding-bottom: 5px; display: flex">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" icon="refresh" />
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.confno && !objData.header.revConfcnt"
        label="冲销"
        type="warning"
        @click="reverseSap"
        icon="refresh-left" />
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <orderReportDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <orderReportDgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply, reverseApply } from "../api/orderReport.js";
  import { submitFormCheck } from "./js/orderReportCheck";
  import orderReportDgHeader from "./orderReportDgHeader.vue";
  import orderReportDgItem from "./orderReportDgItem.vue";

  export default {
    name: "Z000_PP_SCDDBG_01A",
    components: { orderReportDgHeader, orderReportDgItem },
    data() {
      return {
        loading: false,
        dg: { open: false },
        SR: {
          routerCode: "Z000_PP_SCDDBG_01A",
          SRH_height: 250,
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
        syncSapApply(this.objData.header.reportId).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
          }
        });
      },

      reverseSap() {
        this.szdPop("确定要冲销该报工单吗？", true)
          .then(() => {
            reverseApply(this.objData.header.reportId).then(res => {
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
          bussId: "",
          bussDocId: "",
          title: "",
          reportId: "",
          ztype: "",
          aufnr: "",
          confno: "",
          confcnt: "",
          budat: "",
          sapMsg: "",
          sapMessId: ""
        };
        this.objData.itemList = [];
        this.dg.open = true;
      }
    }
  };
</script>
