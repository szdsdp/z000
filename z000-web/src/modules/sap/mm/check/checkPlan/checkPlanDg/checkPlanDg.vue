<template>
  <div class="app-container" v-if="dg.open">
    <div style="padding-bottom: 5px; display: flex">
      <szd-button
        v-if="['C', 'D'].includes(objData.header.bussStatus) && objData.header.planStatus === '01'"
        label="下发盘点计划"
        type="primary"
        @click="distributePlan"
        icon="Upload" />
      <szd-button v-if="objData.header.bussStatus === 'D'" label="同步" type="primary" @click="syncSap" icon="refresh" />
      <szd-button
        v-if="objData.header.bussStatus === 'F' && objData.header.mblnr && !objData.header.revMblnr"
        label="冲销"
        type="warning"
        @click="openReverseDialog"
        icon="refresh-left" />
      <!-- 工作流按钮 -->
      <szd-wf-button :routerCode="SR.routerCode" btnHide="FGJQRS" :wfOper="wfOper" :bussBase="objData.header" :data="objData" @submit="submitForm" />
    </div>
    <el-dialog v-model="reverseDialog.visible" title="冲销日期" width="400px" :close-on-click-modal="false">
      <el-form>
        <el-form-item label="冲销日期">
          <el-date-picker v-model="reverseDialog.budat" type="date" value-format="YYYY-MM-DD" placeholder="请选择冲销日期" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reverseDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="reverseSap" :loading="reverseDialog.loading">确定</el-button>
      </template>
    </el-dialog>
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <checkPlanDgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <checkPlanDgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { getApply, checkApply, saveApply, syncSapApply, reverseApply, distributeApply } from "../api/checkPlan.js";
  import { submitFormCheck } from "./js/checkPlanCheck";
  import checkPlanDgHeader from "./checkPlanDgHeader.vue";
  import checkPlanDgItem from "./checkPlanDgItem.vue";

  export default {
    name: "Z000_MM_KCPD_01A",
    components: {
      checkPlanDgHeader,
      checkPlanDgItem
    },
    data() {
      return {
        loading: false,
        dg: {
          open: false
        },
        SR: {
          routerCode: "Z000_MM_KCPD_01A",
          SRH_height: 300,
          SRH_default: "Tab1",
          SRH: "Tab1",
          SRI: "Tab1",
          szdopt: "U"
        },
        wfOper: {},
        reverseDialog: {
          visible: false,
          budat: "",
          loading: false
        },
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
      // 同步
      syncSap() {
        syncSapApply(this.objData.header.planId).then(res => {
          if (res.code === 200) {
            this.szdMsgBox(res.data.retMsgBox);
            this.getData();
          }
        });
      },
      // 冲销弹框
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
        reverseApply(this.objData.header.planId, this.reverseDialog.budat)
          .then(res => {
            if (res.code === 200) {
              this.reverseDialog.visible = false;
              this.szdMsgBox(res.data.retMsgBox);
              this.getData();
            }
          })
          .finally(() => (this.reverseDialog.loading = false));
      },

      // 下发盘点计划
      distributePlan() {
        this.szdPop("确认下发盘点计划吗？将根据工厂、库位生成盘点任务。", true)
          .then(() => {
          distributeApply({ data: this.objData }).then(res => {
            if (res.code === 200) {
              this.$mS("下发成功");
              this.getData();
            }
          });
        }).catch(() => {});
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
          cstcName: userData.cstcName,
          createBy: userData.userId,
          createByName: userData.userName,
          planStatus: "01"
        };
        this.objData.itemList = [];
        this.dg.open = true;
      }
    }
  };
</script>
