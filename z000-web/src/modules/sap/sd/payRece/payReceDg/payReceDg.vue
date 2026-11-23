<template>
  <div class="app-container">
    <!-- 工具栏 -->
    <div style="padding-bottom: 5px; display: flex">
      <szd-button v-if="(objData.header.bussStatus === 'D' || objData.header.bussStatus === 'F') && !objData.header.vchNum" label="同步" type="primary" @click="genSapDoc" icon="refresh" />
      <szd-button v-if="(objData.header.bussStatus === 'D' || objData.header.bussStatus === 'F') && objData.header.vchNum && !objData.header.vchNumReverse" label="冲销" type="warning" @click="reverseSapDoc" icon="refresh-left" />
      <szd-wf-button :routerCode="SR.routerCode" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <!-- 内容 -->
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <payReceDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <payReceDgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, genSapDocApply, reverseSapDocApply, getDataInit } from "../api/payRece.js";
  import { submitFormCheck } from "./js/payReceCheck";
  import payReceDgHeader from "./payReceDgHeader.vue";
  import payReceDgItem from "./payReceDgItem.vue";

  export default {
    name: "Z000_SD_PAY_RECE_01A",
    components: { payReceDgHeader, payReceDgItem },
    data() {
      return {
        SR: {
          routerCode: "Z000_SD_PAY_RECE_01A",
          SRH_height: 280,
          SRH: "Tab1",
          SRI: "Tab1",
          szdopt: "U"
        },
        wfOper: {},
        objData: {
          header: {},
          cash: {},
          bill: {},
          itemList: [],
          itemListSale: []
        }
      };
    },
    created() {
      this.szdWf.getUrlParams(this.wfOper);
      let bankDocType = this.$route.query.bankDocType;
      let bankDoc = this.$route.query.bankDoc;
      if (bankDoc) {
        this.getDataInitData(bankDocType, bankDoc);
      } else if (!this.wfOper.bussId) {
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
      // 获取业务数据
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

      // 获取认领初始数据
      getDataInitData(bankDocType, bankDoc) {
        getDataInit(bankDocType, bankDoc).then(response => {
          if (response.code === 200) {
            this.objData = response.data.data;
            this.wfOper = response.data.wfOper;
            this.SR.szdopt = this.wfOper.wfOper ? this.wfOper.wfOper.szdopt : "U";
          }
        });
      },

      // 保存数据
      submitForm(wfEvt) {
        if (this.szdWf.btnCheck(wfEvt) || submitFormCheck(this.objData)) {
          if (wfEvt.operate === "btn_check") {
            checkApply(this.objData, wfEvt).then(response => {
              if (response.code === 200) {
                this.szdWf.retMsg(response.data);
              }
            });
          } else {
            saveApply(this.objData, wfEvt).then(response => {
              if (response.code === 200) {
                this.wfOper.bussId = response.data.bussId;
                this.objData.header.receiptId = response.data.bussId;
                this.objData.header.bussId = response.data.bussId;
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
            });
          }
        }
      },

      // 同步SAP
      genSapDoc() {
        genSapDocApply(this.objData.header.bussId).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
          }
        });
      },

      // 冲销SAP
      reverseSapDoc() {
        this.$confirm("确认冲销该回款认领单吗？", "提示", { type: "warning" }).then(() => {
          reverseSapDocApply(this.objData.header.bussId).then(res => {
            if (res.code === 200) {
              this.szdMsgBox(res.data.retMsgBox);
              this.getData();
            }
          });
        }).catch(() => {});
      },

      // 初始化新单默认数据
      initDataDef(res) {
        const userData = this.$store.state.loginUser.user;
        this.objData.header = {
          routerCode: this.SR.routerCode,
          bstp: res.bstp,
          bstpName: res.bstpName,
          bussDate: this.szdDate.getDay(),
          bussStatus: "A",
          wfDyId: this.wfOper.wfDef ? this.wfOper.wfDef.wfDyId : "",
          wfDyName: this.wfOper.wfDef ? this.wfOper.wfDef.wfDyName : "",
          cmpy: userData.cmpy,
          cmpyName: userData.cmpyName,
          cstc: userData.cstc,
          cstcName: userData.cstcName,
          curr: "CNY",
          payNature: "10"
        };
        this.objData.cash = {};
        this.objData.bill = {};
        this.objData.itemList = [];
      }
    }
  };
</script>
