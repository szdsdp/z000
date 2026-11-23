<template>
  <div class="app-container" v-if="dg.open">
    <!-- 工具栏  -->
    <div style="padding-bottom: 5px; display: flex">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" icon="refresh" />
      <szd-button label="验重" type="primary" @click="checkSap" icon="refresh" />
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <!-- 内容 -->
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <custDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <custDgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply, checkSapApply } from "../api/cust.js";
  import { submitFormCheck } from "./js/custCheck";
  import custDgHeader from "./custDgHeader.vue";
  import custDgItem from "./custDgItem.vue";

  export default {
    name: "Z000_MDM_CUST_01A",
    components: {
      custDgHeader,
      custDgItem
    },
    data() {
      return {
        loading: false,
        dg: {
          open: false,
        },
        SR: {
          routerCode: "Z000_MDM_CUST_01A",
          SRH_height: 350, //抬头高度
          SRH: "Tab1",
          SRI: "Tab1",
          szdopt: "U" //更改权限
        },
        wfOper: {},
        objData: {
          header: {},
          cmpyKList: [],
          cmpyDList: [],
          bkacList: [],
          saleList: [],
          purchaseList: [],
          creditList: []
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
      // 获取数据
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
            checkApply(this.objData, wfEvt).then(response => {
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

            }).finally(() => this.loading = false);
          } else {
            saveApply(this.objData, wfEvt).then(response => {
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

      // 同步Sap
      syncSap() {
        syncSapApply(this.objData.header.bussId).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
          }
        });
      },
      // SAP客商验重
      checkSap() {
        if (!this.objData.header.custName && !this.objData.header.idNum) {
          this.$mE("请输入客商名称或税号！");
          return;
        }
        let json = {
          custId: this.objData.header.custId,
          custName: this.objData.header.custName,
          idNum: this.objData.header.idNum
        };
        checkSapApply(json).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
          }
        });
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
          wfDyId: this.wfOper.wfDef.wfDyId,
          wfDyName: this.wfOper.wfDef.wfDyName,
          cmpy: userData.cmpy,
          cmpyName: userData.cmpyName,
          cstc: userData.cstc,
          cstcName: userData.cstcName,
          bussId: "",
          bussDocId: "",
          title: "",
          custId: "",
          custName: "",
          custGrp: "",
          partnerType: "",
          u8Code: "",
          ctry: "CN",
          province: "",
          city: "",
          address: "",
          phone: "",
          contact: "",
          idNum: "",
          riskClass: "",
          taxClass: "1",
          custFlag: [],
        };
        this.objData.cmpyKList = [];
        this.objData.cmpyDList = [];
        this.objData.bkacList = [];
        this.objData.saleList = [];
        this.objData.purchaseList = [];
        this.objData.creditList = [];
        this.dg.open = true;
      }
    }
  };
</script>
