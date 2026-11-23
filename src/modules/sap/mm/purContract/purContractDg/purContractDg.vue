<template>
  <div class="app-container" v-if="dg.open">
    <div style="padding-bottom: 5px; display: flex">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" icon="Refresh" />
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <purContractDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <purContractDgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getContract, checkContract, saveContract, syncSapContract } from "../api/purContract.js";
  import { submitFormCheck } from "./js/purContractCheck";
  import purContractDgHeader from "./purContractDgHeader.vue";
  import purContractDgItem from "./purContractDgItem.vue";

  export default {
    name: "Z000_MM_CGHT_01A",
    components: { purContractDgHeader, purContractDgItem },
    data() {
      return {
        loading: false,
        dg: { open: false },
        SR: {
          routerCode: "Z000_MM_CGHT_01A",
          SRH_height: 380,
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
        getContract(json).then(response => {
          if (response.code === 200) {
            this.objData = response.data.data;
            this.wfOper = response.data.wfOper;
            this.SR.szdopt = this.wfOper.wfOper.szdopt;
          }
          this.dg.open = true;
        });
      },
      submitForm(wfEvt) {
        if (this.loading === false && (this.szdWf.btnCheck(wfEvt) || submitFormCheck(this.objData))) {
          this.loading = true;
          if (wfEvt.operate === "btn_check") {
            checkContract(this.objData, wfEvt).then(response => {
              if (response.code === 200) {
                this.szdMsgBox(response.data.retMsgBox);
                if (response.data.msg) {
                  if (response.data.status === "S") this.$mS(response.data.msg);
                  else if (response.data.status === "E") this.$mE(response.data.msg);
                  else if (response.data.status === "W") this.$mW(response.data.msg);
                }
              }
            }).finally(() => this.loading = false);
          } else {
            saveContract(this.objData, wfEvt).then(response => {
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
            }).finally(() => this.loading = false);
          }
        }
      },
      syncSap() {
        syncSapContract(this.objData.header.contractId).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
          }
        });
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
          createBy: userData.userId,
          createByName: userData.userName,
          cmpy: userData.cmpy,
          cmpyName: userData.cmpyName,
          cstc: userData.cstc,
          cstcName: userData.cstcName,
        };
        this.objData.itemList = [];
        this.dg.open = true;
      }
    }
  };
</script>
