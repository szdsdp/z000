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
      <!-- 工作流按钮 -->
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" @submit="submitForm" />
    </div>
    <!-- 抬头（含标签页） -->
    <checkTaskDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply } from "../api/checkTask.js";
  import { submitFormCheck } from "./js/checkTaskCheck";
  import checkTaskDgHeader from "./checkTaskDgHeader.vue";

  export default {
    name: "Z000_MM_KCPD_02A",
    components: {
      checkTaskDgHeader
    },
    provide() {
      return { isBlind: this.isBlind };
    },
    computed: {
      // 是否盲盘
      isBlind() {
        return this.objData.header.blindFlag === "1";
      }
    },
    data() {
      return {
        loading: false,
        SR: {
          routerCode: "Z000_MM_KCPD_02A",
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
        this.pageTitle = "查看盘点任务";
      } else if (this.wfOper.option == "U") {
        this.pageTitle = "修改盘点任务";
      } else {
        this.pageTitle = "盘点任务";
      }
      if (this.wfOper.bussId) {
        this.getData();
      }
    },
    methods: {
      // 获取数据
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
        });
      },
      // 提交表单
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
