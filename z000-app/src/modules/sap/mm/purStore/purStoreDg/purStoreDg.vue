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
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.mblnr && !objData.header.revMblnr"
        label="冲销"
        type="warning"
        @click="openReverseDialog" />
      <!-- 工作流按钮 -->
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" @submit="submitForm" />
    </div>
    <!-- 冲销日期弹窗 -->
    <var-popup v-model:show="reverseDialog.visible" position="center">
      <div class="reverse-popup">
        <h4>冲销日期</h4>
        <szd-date label="冲销日期" v-model="reverseDialog.budat" type="date" />
        <div style="display: flex; justify-content: flex-end; gap: 8px; margin-top: 16px">
          <var-button @click="reverseDialog.visible = false">取消</var-button>
          <var-button type="primary" @click="reverseSap" :loading="reverseDialog.loading">确定</var-button>
        </div>
      </div>
    </var-popup>
    <!-- 抬头（含标签页） -->
    <purStoreDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply, reverseApply } from "../api/purStore.js";
  import { submitFormCheck } from "./js/purStoreCheck";
  import purStoreDgHeader from "./purStoreDgHeader.vue";

  export default {
    name: "Z000_MM_CGRK_01A",
    components: {
      purStoreDgHeader
    },
    data() {
      return {
        loading: false,
        SR: {
          routerCode: "Z000_MM_CGRK_01A",
          actTab: "Tab1",
          szdopt: "U"
        },
        wfOper: {},
        // 冲销弹窗
        reverseDialog: {
          visible: false,
          budat: "",
          loading: false
        },
        objData: {
          header: {},
          itemList: [],
          relateList: []
        }
      };
    },
    created() {
      this.szdWf.getUrlParams(this.wfOper);
      // 设置标题
      if (this.wfOper.option == "V") {
        this.pageTitle = "查看物料凭证";
      } else if (this.wfOper.option == "U") {
        this.pageTitle = "修改物料凭证";
      } else {
        this.pageTitle = "新增物料凭证";
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
        getApply(json).then(response => {
          if (response.code === 200) {
            this.objData = response.data.data;
            this.wfOper = response.data.wfOper;
            this.SR.szdopt = this.wfOper.wfOper.szdopt;
          }
        });
      },
      // 提交表单
      submitForm(wfEvt) {
        // 已产生物料凭证且未冲销时，不允许撤回/作废
        if ((wfEvt.operate === "btn_revoke" || wfEvt.operate === "btn_delete") && this.objData.header.mblnr && !this.objData.header.revMblnr) {
          const action = wfEvt.operate === "btn_revoke" ? "撤回" : "作废";
          this.$mW(`已产生物料凭证，无法${action}，请执行冲销操作`);
          return;
        }
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
        syncSapApply(this.objData.header.storeId).then(res => {
          if (res.code === 200) {
            this.szdWf.retMsg(res.data);
            this.getData();
          }
        });
      },
      // 打开冲销弹窗
      openReverseDialog() {
        this.reverseDialog.budat = this.szdDate.getDay();
        this.reverseDialog.visible = true;
      },
      // 冲销
      reverseSap() {
        if (!this.reverseDialog.budat) {
          this.$mW("请选择冲销日期");
          return;
        }
        this.reverseDialog.loading = true;
        reverseApply(this.objData.header.storeId, this.reverseDialog.budat)
          .then(res => {
            if (res.code === 200) {
              this.reverseDialog.visible = false;
              this.szdWf.retMsg(res.data);
              this.getData();
            }
          })
          .finally(() => (this.reverseDialog.loading = false));
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
          cmpy: userData.cmpy,
          cmpyName: userData.cmpyName,
          cstc: userData.cstc,
          cstcName: userData.cstcName,
          createBy: userData.userId,
          createByName: userData.userName
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
  .reverse-popup {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
  }
  .reverse-popup h4 {
    margin: 0 0 12px 0;
    font-size: 16px;
  }
</style>
