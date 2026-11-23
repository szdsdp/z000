<template>
  <div class="szd__tb__wrapper">
    <div class="page-header">
      <div class="title-container">
        <span class="title-bar"></span>
        <var-text size="large" type="primary">{{ pageTitle }}</var-text>
      </div>
    </div>
    <div class="szd__tb__top" style="padding-top: 30px; padding-bottom: 20px">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" />
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" @submit="submitForm" />
    </div>
    <saleOrderDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>
<script>
  import { getApply, checkApply, saveApply, syncSapApply } from "../api/saleOrder.js";
  import { submitFormCheck } from "./js/saleOrderCheck";
  import saleOrderDgHeader from "./saleOrderDgHeader.vue";

  export default {
    name: "Z000_SD_XSDD_01A",
    components: { saleOrderDgHeader },
    data() {
      return {
        loading: false,
        SR: { routerCode: "Z000_SD_XSDD_01A", actTab: "Tab1", szdopt: "U" },
        wfOper: {},
        objData: { header: {}, itemList: [], schedList: [] }
      };
    },
    created() {
      this.szdWf.getUrlParams(this.wfOper);
      if (this.wfOper.option == "V") {
        this.pageTitle = "查看销售订单";
      } else if (this.wfOper.option == "U") {
        this.pageTitle = "修改销售订单";
      } else {
        this.pageTitle = "新增销售订单";
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
        getApply({ bussId: this.wfOper.bussId, userId: this.wfOper.userId, option: this.wfOper.option }).then(r => {
          if (r.code === 200) {
            this.objData = r.data.data;
            this.wfOper = r.data.wfOper;
            this.SR.szdopt = this.wfOper.wfOper.szdopt;
          }
        });
      },
      // 提交表单
      submitForm(wfEvt) {
        if (this.loading === false && (this.szdWf.btnCheck(wfEvt) || submitFormCheck(this.objData))) {
          this.loading = true;
          if (wfEvt.operate === "btn_check") {
            checkApply(this.objData, wfEvt)
              .then(r => {
                if (r.code === 200) {
                  this.szdWf.retMsg(r.data);
                }
              })
              .finally(() => (this.loading = false));
          } else {
            saveApply(this.objData, wfEvt)
              .then(r => {
                if (r.code === 200) {
                  if (!this.wfOper.bussId && r.data.bussId) {
                    this.szdWf.replaceUrl(r.data.bussId);
                  }
                  this.wfOper.bussId = r.data.bussId;
                  this.objData.header.bussId = r.data.bussId;
                  this.szdWf.retInfo(r.data, wfEvt).then(resRet => {
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
        syncSapApply(this.objData.header.orderId).then(r => {
          if (r.code === 200) {
            this.szdWf.retMsg(r.data);
            this.getData();
          }
        });
      },
      // 初始化新建单据默认数据
      initDataDef(res) {
        const u = this.$store.state.loginUser.user;
        this.objData.header = {
          routerCode: this.SR.routerCode,
          bstp: res.bstp,
          bstpName: res.bstpName,
          bussDate: this.szdDate.getDay(),
          bussStatus: "A",
          wfDyId: this.wfOper.wfDef.wfDyId,
          wfDyName: this.wfOper.wfDef.wfDyName,
          cmpy: u.cmpy,
          cmpyName: u.cmpyName,
          cstc: u.cstc,
          cstcName: u.cstcName,
          createBy: u.userId,
          createByName: u.userName
        };
        this.objData.itemList = [];
        this.objData.schedList = [];
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
