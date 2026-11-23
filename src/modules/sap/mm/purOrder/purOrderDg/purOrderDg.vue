<template>
  <div class="app-container" v-if="dg.open">
    <!-- 工具栏  -->
    <div style="padding-bottom: 5px; display: flex">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" icon="refresh" />
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <!-- 内容 -->
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <purOrderDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <purOrderDgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply } from "../api/purOrder.js";
  import { submitFormCheck } from "./js/purOrderCheck";
  import purOrderDgHeader from "./purOrderDgHeader.vue";
  import purOrderDgItem from "./purOrderDgItem.vue";

  export default {
    name: "Z000_MM_CGDD_01A",
    components: {
      purOrderDgHeader,
      purOrderDgItem
    },
    data() {
      return {
        loading: false,
        dg: {
          open: false,
        },
        SR: {
          routerCode: "Z000_MM_CGDD_01A",
          SRH_height: 320, //抬头高度
          SRH: "Tab1",
          SRI: "Tab1",
          szdopt: "U" //更改权限
        },
        wfOper: {},
        objData: {
          header: {},
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
        syncSapApply(this.objData.header.orderId).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
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
          createBy: userData.userId,
          createByName: userData.userName
        };
        this.objData.itemList = [];
        this.objData.schedList = [];
        this.dg.open = true;
      }
    }
  };
</script>
