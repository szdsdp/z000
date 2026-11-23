<template>
  <div class="app-container">
    <!-- 工具栏 -->
    <div style="padding-bottom: 5px; display: flex">
      <szd-wf-button :routerCode="SR.routerCode" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <!-- 内容 -->
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <expCostDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <expCostDgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply } from "../api/expCost.js";
  import { submitFormCheck } from "./js/expCostDgCheck";
  import expCostDgHeader from "./expCostDgHeader.vue";
  import expCostDgItem from "./expCostDgItem.vue";
  import szdTool from "szd-client-web/client/common/tools/tool";

  export default {
    components: {
      expCostDgHeader,
      expCostDgItem
    },
    data() {
      return {
        SR: {
          routerCode: "Z000_DEMO_EXP_01A",
          SRH_height: 230, //抬头高度
          SRH: "Tab1",
          SRI: "Tab1",
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
        if (this.szdWf.btnCheck(wfEvt) || submitFormCheck(this.objData)) {
          if (wfEvt.operate === "btn_check") {
            checkApply(this.objData, wfEvt).then(response => {
              if (response.code === 200) {
                this.szdWf.retMsg(response.data);
              }
            });
          } else {
            this.setItemNo();
            saveApply(this.objData, wfEvt).then(response => {
              if (response.code === 200) {
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
            });
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
      },
      //设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          let item = this.objData.itemList[i];
          if (i === 0) {
            item.itemNo = 10;
          } else {
            item.itemNo = szdTool.getNextItemNo(this.objData.itemList, "itemNo");
          }
        }
      }
    }
  };
</script>
