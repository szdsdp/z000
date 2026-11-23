<template>
  <div class="szd__tb__wrapper">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-container">
        <span class="title-bar"></span>
        <var-text size="large" type="primary">{{ this.pageTitle }}</var-text>
      </div>
    </div>
    <!-- 功能按钮 -->
    <div class="szd__tb__top" style="padding-top: 30px; padding-bottom: 20px">
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" />
      <!-- 工作流按钮 -->
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" @submit="submitForm" />
    </div>
    <!-- 抬头（含标签页） -->
    <purContractDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>

<script>
  import { getContract, checkContract, saveContract, syncSapContract } from "../api/purContract.js";
  import { submitFormCheck } from "./js/purContractCheck";
  import purContractDgHeader from "./purContractDgHeader.vue";

  export default {
    name: "Z000_MM_CGHT_01A",
    components: {
      purContractDgHeader
    },
    data() {
      return {
        loading: false,
        SR: {
          routerCode: "Z000_MM_CGHT_01A",
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
      // 设置标题
      if (this.wfOper.option == "V") {
        this.pageTitle = "查看采购合同";
      } else if (this.wfOper.option == "U") {
        this.pageTitle = "修改采购合同";
      } else {
        this.pageTitle = "新增采购合同";
      }
      if (!this.wfOper.bussId) {
        // 新建模式：初始化默认数据
        this.szdWf.init(this.SR.routerCode).then(res => {
          this.wfOper = res;
          if (res.bstp) {
            this.initDataDef(res);
          }
        });
      } else {
        // 查看/编辑模式：加载数据
        this.getData();
      }
    },
    methods: {
      // 获取数据
      getData() {
        let json = { bussId: this.wfOper.bussId, userId: this.wfOper.userId, option: this.wfOper.option };
        getContract(json).then(response => {
          if (response.code === 200) {
            this.objData = response.data.data;
            this.wfOper = response.data.wfOper;
            this.SR.szdopt = this.wfOper.wfOper.szdopt;
          }
        });
      },
      // 提交表单
      submitForm(wfEvt) {
        if (this.loading === false && (this.szdWf.btnCheck(wfEvt) || submitFormCheck(this.objData))) {
          this.loading = true;
          if (wfEvt.operate === "btn_check") {
            checkContract(this.objData, wfEvt)
              .then(response => {
                if (response.code === 200) {
                  this.szdWf.retMsg(response.data);
                }
              })
              .finally(() => (this.loading = false));
          } else {
            saveContract(this.objData, wfEvt)
              .then(response => {
                if (response.code === 200) {
                  // 首次保存后更新URL
                  if (!this.wfOper.bussId && response.data.bussId) {
                    this.szdWf.replaceUrl(response.data.bussId);
                  }
                  this.wfOper.bussId = response.data.bussId;
                  this.objData.header.bussId = response.data.bussId;
                  this.szdWf.retInfo(response.data, wfEvt).then(resRet => {
                    if (resRet) {
                      if (wfEvt.submitFlag) {
                        // 保存后自动提交
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
        syncSapContract(this.objData.header.contractId).then(res => {
          if (res.code === 200) {
            this.szdWf.retMsg(res.data);
            this.getData();
          }
        });
      },
      // 初始化新建单据默认数据
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
          cstcName: userData.cstcName
        };
        this.objData.itemList = [];
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
