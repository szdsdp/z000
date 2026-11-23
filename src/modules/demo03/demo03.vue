<template>
  <div class="szd__tb__wrapper">
    <!-- 工作流按钮 -->
    <div class="szd__popup__top">
      <szd-wf-button
        :routerCode="wf.routerCode"
        :btnHide="wf.wfBtnHide"
        :wfOper="wf.wfOper"
        :bussBase="wf.wf_buss_base"
        @submit="handleSubmit"
        @consume="handleConsume" />
    </div>

    <div class="szd__popup__main">
      <szd-card-a :footer="false" :header="false" style="margin-top: 5px">
        <!--  内容 -->
        <template #main>
          <span>
            <szd-input placeholder="bussId" v-model="test.bussId" clearable @change="handleChangeBussId" />
          </span>
          <span>
            <szd-input
              placeholder="公司代码"
              v-model="test.cmpy"
              clearable
              showHelp
              :desc="test.cmpyName"
              :help="{ a: 'CORE_CSTC', b: test, c: ['cmpy-cmpy', 'cmpyName-cmpyName'] }"
              :maxlength="32"
              required />
          </span>

          <span>
            <szd-input
              placeholder="搜索帮助集合"
              v-model="test.cateCode"
              clearable
              showHelp
              :desc="test.cateName"
              :help="{ a: 'SEAS_SINM', b: test, c: ['cateCode-cateCode', 'cateName-cateName'] }" />
          </span>
          <span>
            <szd-input placeholder="input数值" v-model="test.inputNum" type="number" numDot :numDec="2" clearable />
          </span>
          <span>
            <szd-date placeholder="日期" v-model="test.date" clearable required />
          </span>
          <span>
            <szd-time placeholder="时间" v-model="test.time" clearable required />
          </span>
          <span>
            <szd-textarea placeholder="文本域" v-model="test.text" clearable required />
          </span>
          <span>
            <szd-input-number placeholder="数值" v-model="test.number" :step="10" :decimalLength="2" :min="1" :max="100" />
          </span>
          <span>
            <szd-checkbox placeholder="复选框组" baseField="CORE_SCP_PRT_BORDER_STYLE" v-model="test.chk" @change="handleChange" checkboxAll="left" />
          </span>
          <span>
            <szd-checkbox placeholder="复选框" boxLabel="复选框" v-model="test.checkbox" @change="handleChange" />
          </span>
          <span>
            <szd-radio first placeholder="单选框" baseField="CORE_SCP_ALIGN_VERTICAL" v-model="test.radio" @change="handleChange" />
          </span>
          <span>
            <szd-select
              first
              placeholder="下拉框"
              baseField="CORE_SCP_ALIGN_VERTICAL"
              v-model="test.select"
              @change="handleChange"
              clearable
              required />
          </span>
          <span>
            <szd-text placeholder="文本" :text="test.text" />
          </span>
          <span>
            <szd-dot placeholder="小数为" :text="test.text" numDot numDec="2" />
          </span>
          <span>
            <szd-button label="按钮-消息" text @click="handleClickBut" />
            <szd-button label="上传文件" @click="handleClickUpload" />
          </span>
          <span>
            <szd-area placeholder="地区" v-model="test.area" clearable @change="handleChange" required />
          </span>
          <span>
            <szd-scan placeholder="扫码" v-model="test.scan" :scanCtrl="{}" clearable @change="handleChange" required />
          </span>
        </template>
      </szd-card-a>

      <!-- 查询条件 -->
      <span>
        <szd-sch-area :schLay="schLay" v-model="queryParams.ranges" @handleEvent="handleEventSchImp" />
      </span>

      <span>
        <szd-sch placeholder="查询条件" v-model="test.ranges" required/>
      </span>
    </div>
  </div>
</template>

<script setup>
  import { ref } from "vue";
  import { szdMsgBox } from "szd-client-app/client/components/msgBox/msgBox";
  import { szdUpload } from "szd-client-app/client/components/upload/upload";
  import { szdWf } from "szd-client-app/client/wf/indexFunction";
  import store from "~store";

  const test = ref({
    bussId: "",
    cmpy: "1111",
    inputNum: 11111,
    date: "2025-02-07",
    time: "23:15",
    area: "210100",
    chk: ["none", "solid"],
    row: [{}],
    ranges: undefined
  });

  const queryParams = ref({
    ranges: {}
  });

  const schLay = {
    schCat: [
      { field: "bussDocId", placeholder: "业务单号", type: "string", required: true },
      { field: "title", placeholder: "业务标题", type: "string" },
      { field: "cmpy", placeholder: "公司代码", type: "string", help: { a: "CORE_CMPY" } },
      { field: "bstp", placeholder: "业务类型", type: "string", help: { a: "CORE_BSTP" } },
      { field: "createTime", placeholder: "制单日期", type: "date" },
      { field: "status", placeholder: "单据状态", type: "string", help: { a: "CORE_WF_DOST" } }
    ],
    schBtn: [
      { fCode: "move", label: "移动", more: true },
      { fCode: "move", label: "转让", more: true },
      { fCode: "delete", label: "删除", more: true },
      { fCode: "exp", label: "报账", more: true }
    ]
  };

  const wf = ref({
    routerCode: "",
    wfBtnHide: "",
    wfOper: {
      bussId: "1831604619726426112",
      userId: store.state.loginUser.user.userId,
      wfBtn: {
        btn_check: "U", //检查  空不能使用，V显示 U
        btn_save: "U", //保存
        btn_commit: "U", //提交
        btn_approve: "U", //同意
        btn_reject: "U", //驳回
        btn_print: "U", //打印
        btn_atta: "U", //附件
        btn_image: "U", //影像
        btn_record: "U", //审批信息
        btn_vch: "U", //查看凭证
        btn_revoke: "U", //撤回
        btn_transfer: "U", //转办
        btn_meeting: "U", //评审
        btn_preAddSign: "U", //前加签
        btn_postAddSign: "U", //后加签
        btn_delete: "U", //作废
        btn_dyWfId: "WF_TEST_N03", //动态工作流-显示的是流程ID
        btn_consume: "U" //消费记录
      }
    },
    wf_buss_base: {
      bussId: "1831604619726426112"
    }
  });

  const handleChange = v => {
    console.log("v", v);
    console.log("test", test.value);
  };
  const handleChangeBussId = v => {
    wf.value.wf_buss_base.bussId = test.value.bussId ? test.value.bussId : "1831604619726426112";
    wf.value.wfOper.bussId = test.value.bussId ? test.value.bussId : "1831604619726426112";
  };

  const handleEventSchImp = () => {
    console.log("queryParams", queryParams.value);
  };

  const handleClickBut = () => {
    let msg = [
      { light: 1, retCode: "E", retMsg: "JSON.stringify(schLay.value)" },
      { light: 2, retCode: "W", retMsg: JSON.stringify(schLay) },
      { light: 3, retCode: "S", retMsg: JSON.stringify(schLay) }
    ];
    szdMsgBox(msg).then(() => {
      return false;
    });
  };

  const handleClickUpload = () => {
    szdUpload({
      bussId: wf.value.wfOper.bussId,
      uploadUrl: "",
      typeCode: "TEST01",
      fileSize: 0,
      limit: 2
    }).then(res => {
      console.log("1-szdUpload", res);
    });
  };

  const handleSubmit = wfEvt => {
    console.log("1", wfEvt);
  };

  const handleConsume = () => {
    szdWf.consume(undefined, []).then(res => {});
  };
</script>