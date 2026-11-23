<template>
  <div class="app-container">
    <div style="padding-bottom: 5px; display: flex">
      <!-- 工具栏 -->
      <szd-wf-button
        :routerCode="SR.routerCode"
        :wfOper="wfOper"
        :bussBase="objData.header"
        @submit="submitForm"
        :btnParam="btnParam" />
    </div>
    <szd-tabs-wrap :top-height="SR.SRH_height" v-model:top="SR.SRH" v-model:bottom="SR.SRI">
      <template v-slot:top>
        <z1DgHeader :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
      <template v-slot:bottom>
        <z1DgItem :objData="objData" :SR="SR" :wfOper="wfOper" />
      </template>
    </szd-tabs-wrap>
  </div>
</template>

<script>
  import { get, check, save } from "../api/z1.js";
  import { z000Tool } from "@/modules/common/indexCommon";
  import z1DgHeader from "./z1DgHeader.vue";
  import z1DgItem from "./z1DgItem.vue";
  import { szdMsgBox } from "szd-client-web/client/components/msgBox/msgBox";

  export default {
    components: {
      z1DgHeader,
      z1DgItem
    },
    data() {
      return {
        SR: {
          routerCode: "Z000_Z1_01A",
          SRH_height: 250, //抬头高度
          SRH: "Tab1",
          SRI: "Tab1",
          szdopt: "U"
        },
        wfOper: {},
        objData: {
          header: {},
          itemList: []
        },
        btnParam: {
          D: { label: "同意", closeDg: true, comments: "后台同意" },
          E: { label: "不同意", closeDg: true, comments: "后台意见" }
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
        // 获取数据
        get(json).then(response => {
          if (response.code === 200) {
            this.objData = response.data.data;
            this.wfOper = response.data.wfOper;
            this.SR.szdopt = this.wfOper.wfOper.szdopt;
          }
        });
      },

      //保存数据
      submitForm(wfEvt) {
        if (this.szdWf.btnCheck(wfEvt) || this.submitFormCheck(this.objData)) {
          if (wfEvt.operate == "btn_check") {
            check(this.objData, wfEvt).then(response => {
              if (response.code === 200) {
                this.szdWf.retMsg(response.data);
              }
            });
          } else {
            save(this.objData, wfEvt).then(response => {
              if (response.code === 200) {
                this.wfOper.bussId = response.data.bussId;
                this.objData.header.costId = response.data.bussId;
                this.objData.header.bussId = response.data.bussId;
                this.szdWf.retInfo(response.data).then(resRet => {
                  if (resRet) {
                    if (resRet !== true && resRet.nextUserId) {
                      wfEvt.nextUserId = resRet.nextUserId;
                      this.submitForm(wfEvt);
                    } else if (resRet === true && response.data.retCodeF === "W") {
                      wfEvt.execFlag = true;
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
      //检查提交数据
      submitFormCheck(objData) {
        let msgList = [];
        // 检查抬头数据
        this.submitFormCheckHead(objData, msgList);
        // 检查项目数据
        this.submitFormCheckItem(objData, msgList);
        if (msgList.length === 0) {
          return true;
        } else {
          szdMsgBox(msgList).then();
          return false;
        }
      },
      // 检查抬头数据
      submitFormCheckHead(objData, msgList) {
        z000Tool.checkNull(msgList, objData.header.cmpy, "公司不能为空");
        z000Tool.checkNull(msgList, objData.header.cstc, "部门不能为空");
        z000Tool.checkNull(msgList, objData.header.bstp, "业务类型不能为空");
        z000Tool.checkNull(msgList, objData.header.bussDate, "业务日期不能为空");
        z000Tool.checkNull(msgList, objData.header.userId, "报账人不能为空");
        z000Tool.checkNull(msgList, objData.header.title, "业务标题不能为空");
      },
      // 检查项目数据
      submitFormCheckItem(objData, msgList) {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          z000Tool.checkNull(msgList, objData.itemList[i].bsub, "项目信息-业务小类不能为空");
          z000Tool.checkNull(msgList, objData.itemList[i].taxr, "项目信息-税率不能为空");
          z000Tool.checkNull(msgList, objData.itemList[i].amt, "项目信息-金额不能为空");
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
          cmpyNameCreate: userData.cmpyName,
          cstcCreate: userData.cstc,
          cstcNameCreate: userData.cstcName,
          amtApply: 0,
        };
      }
    }
  };
</script>
