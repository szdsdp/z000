<template>
  <div class="app-container" v-if="dg.open">
    <div style="padding-bottom: 5px; display: flex">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" icon="refresh" />
      <szd-button v-if="objData.header.bussStatus === 'F' && objData.header.vbeln && !objData.post.mblnr" label="发货过账" type="success" @click="postGoods" icon="upload" />
      <szd-button v-if="objData.post && objData.post.mblnr && !objData.post.revMblnr" label="冲销发货过账" type="warning" @click="reversePostGoods" icon="refresh-left" />
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <saleDeliveryDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <saleDeliveryDgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply, postApply, reversePostApply } from "../api/saleDelivery.js";
  import { submitFormCheck } from "./js/saleDeliveryCheck";
  import saleDeliveryDgHeader from "./saleDeliveryDgHeader.vue";
  import saleDeliveryDgItem from "./saleDeliveryDgItem.vue";

  export default {
    name: "Z000_SD_XSJH_01A",
    components: { saleDeliveryDgHeader, saleDeliveryDgItem },
    data() {
      return {
        loading: false,
        dg: { open: false },
        SR: {
          routerCode: "Z000_SD_XSJH_01A",
          SRH_height: 250,
          SRH: "Tab1",
          SRI: "Tab1",
          szdopt: "U"
        },
        wfOper: {},
        objData: {
          header: {},
          post: {},
          itemList: []
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

      postGoods() {
        if (!this.objData.post || !this.objData.post.pickDate || !this.objData.post.postDate || !this.objData.post.shipDate) {
          this.$mW("请先在过账信息页签填写拣配日期、过账日期、发货日期");
          return;
        }
        const postData = {
          deliveryId: this.objData.header.deliveryId,
          pickDate: this.objData.post.pickDate,
          postDate: this.objData.post.postDate,
          shipDate: this.objData.post.shipDate
        };
        postApply(postData).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
          }
        });
      },
      reversePostGoods() {
        this.szdPop("确定要冲销发货过账吗？", true)
          .then(() => {
          reversePostApply(this.objData.header.deliveryId).then(res => {
            if (res.code === 200) {
              this.szdMsgBox(res.data.retMsgBox);
              this.getData();
            }
          });
        }).catch(() => {});
      },
      syncSap() {
        syncSapApply(this.objData.header.deliveryId).then(res => {
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
          cmpy: userData.cmpy,
          cmpyName: userData.cmpyName,
          cstc: userData.cstc,
          cstcName: userData.cstcName
        };
        this.objData.itemList = [];
        this.dg.open = true;
      }
    }
  };
</script>
