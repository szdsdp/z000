<template>
  <div class="szd__tb__wrapper">
    <!-- 功能按钮 -->
    <div class="szd__tb__top" style="padding: 0">
      <szd-wf-button :routerCode="SR.routerCode" :wfOper="wfOper" :bussBase="objData.header" @submit="submitForm" />
    </div>
    <!-- 页标签 -->
    <var-tabs
      elevation
      color="var(--color-primary)"
      active-color="var(--color-on-primary)"
      inactive-color="var(--color-on-info)"
      v-model:active="SR.actTab">
      <var-tab name="Tab1">{{ $t("基本信息1") }}</var-tab>
      <var-tab name="Tab2">{{ $t("基本信息2") }}</var-tab>
      <var-tab name="Tab3">{{ $t("项目信息") }}</var-tab>
    </var-tabs>
    <!-- 页标签内容 -->
    <var-tabs-items v-model:active="SR.actTab" class="flex-1">
      <var-tab-item name="Tab1">
        <expCostDgHeaderTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
      </var-tab-item>
      <var-tab-item name="Tab2">
        <expCostDgHeaderTab20 :objData="objData" :SR="SR" :wfOper="wfOper" />
      </var-tab-item>
      <var-tab-item name="Tab3">
        <expCostDgItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" />
      </var-tab-item>
    </var-tabs-items>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply } from "../api/expCost.js";
  import { submitFormCheck } from "./js/expCostDgCheck";
  import expCostDgHeaderTab10 from "./expCostDgHeaderTab10.vue";
  import expCostDgHeaderTab20 from "./expCostDgHeaderTab20.vue";
  import expCostDgItemTab10 from "./expCostDgItemTab10.vue";

  export default {
    components: {
      expCostDgHeaderTab10,
      expCostDgHeaderTab20,
      expCostDgItemTab10
    },
    data() {
      return {
        loading: false,
        SR: {
          routerCode: "Z000_DEMO_EXP_01A",
          actTab: "Tab1",
          szdopt: "U"
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
        });
      },

      //保存数据
      submitForm(wfEvt) {
        if (this.loading === false && (this.szdWf.btnCheck(wfEvt) || submitFormCheck(this.objData))) {
          this.loading = true;
          if (wfEvt.operate === "btn_check") {
            checkApply(this.objData, wfEvt)
              .then(response => {
                if (response.code === 200) {
                  this.szdWf.retMsg(response.data);
                }
              })
              .finally(() => (this.loading = false));
          } else {
            saveApply(this.objData, wfEvt)
              .then(response => {
                if (response.code === 200) {
                  if (!this.wfOper.bussId && response.data.bussId) {
                    this.szdWf.replaceUrl(response.data.bussId);
                  }
                  this.wfOper.bussId = response.data.bussId;
                  this.objData.header.bussId = response.data.bussId;
                  this.szdWf.retInfo(response.data, wfEvt).then(resRet => {
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

      initDataDef(res) {
        const userData = this.$store.state.loginUser.user;
        this.objData.header = {
          routerCode: this.SR.routerCode,
          bstp: res.bstp,
          bstpName: res.bstpName,
          userId: userData.userId,
          userName: userData.userName,
          bussDate: this.szdDate.getDay(),
          curr: "CNY",
          status: "0",
          bussStatus: "A",
          wfDyId: this.wfOper.wfDef.wfDyId,
          wfDyName: this.wfOper.wfDef.wfDyName,

          cmpy: userData.cmpy,
          cmpyName: userData.cmpyName,
          cstc: userData.cstc,
          cstcName: userData.cstcName,
          cmpyCreate: userData.cmpy,
          cmpyCreateName: userData.cmpyName,
          cstcCreate: userData.cstc,
          cstcCreateName: userData.cstcName,
          amtApply: 0
        };
        this.objData.itemList = [];
      }
    }
  };
</script>