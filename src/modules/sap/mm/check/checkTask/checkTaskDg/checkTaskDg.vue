<template>
  <div class="app-container" v-if="dg.open">
    <div style="padding-bottom: 5px; display: flex">
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <checkTaskDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <checkTaskDgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply } from "../api/checkTask.js";
  import { submitFormCheck } from "./js/checkTaskCheck";
  import checkTaskDgHeader from "./checkTaskDgHeader.vue";
  import checkTaskDgItem from "./checkTaskDgItem.vue";

  export default {
    name: "Z000_MM_KCPD_02A",
    components: { checkTaskDgHeader, checkTaskDgItem },
    data() {
      return {
        loading: false,
        dg: { open: false },
        SR: {
          routerCode: "Z000_MM_KCPD_02A",
          SRH_height: 250,
          SRH: "Tab1",
          SRI: "Tab1",
          szdopt: "U"
        },
        wfOper: {},
        objData: { header: {}, itemList: [] }
      };
    },
    provide() {
      return { isBlind: this.isBlind };
    },
    computed: {
      isBlind() {
        return this.objData.header.blindFlag === "1";
      }
    },
    created() {
      this.szdWf.getUrlParams(this.wfOper);
      if (this.wfOper.bussId) {
        this.getData();
      }
    },
    methods: {
      getData() {
        let json = { bussId: this.wfOper.bussId, userId: this.wfOper.userId, option: this.wfOper.option };
        getApply(json).then(response => {
          if (response.code === 200) {
            const result = response.data;
            this.objData = result.data || result;
            this.wfOper = result.wfOper;
            if (this.wfOper && this.wfOper.wfOper) {
              this.SR.szdopt = this.wfOper.wfOper.szdopt;
            }
          }
          this.dg.open = true;
        });
      },
      submitForm(wfEvt) {
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
      }
    }
  };
</script>
