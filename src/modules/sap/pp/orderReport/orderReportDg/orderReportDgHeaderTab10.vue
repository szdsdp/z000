<template>
  <div class="szd__tb__main" style="padding-top: 5px">
    <szd-card-a :footer="false">
      <template #header>业务单号: {{ objData.header.bussDocId || "未提交" }}</template>
      <template #main>
        <szd-input label="业务类型" v-model="objData.header.bstp" :desc="objData.header.bstpName" disabled />
        <szd-date label="业务日期" v-model="objData.header.bussDate" type="date" required :szdopt="SR.szdopt" />
        <szd-select label="单据状态" v-model="objData.header.bussStatus" baseField="Z000_DOC_STAU" disabled />
        <szd-input label="创建人" v-model="objData.header.createBy" :desc="objData.header.createByName" disabled />
<!--        <szd-input label="创建时间" v-model="objData.header.createTime" disabled />-->
        <szd-input
          label="公司代码"
          v-model="objData.header.cmpy"
          :szdopt="SR.szdopt"
          required
          :desc="objData.header.cmpyName"
          readonly
          showHelp
          @help="szdSh({ a: 'CORE_CMPY', b: objData.header, c: ['cmpy-cmpy', 'cmpyName-cmpyName'] })" />
<!--        <szd-input label="成本中心" v-model="objData.header.cstc" disabled required :desc="objData.header.cstcName" />-->
        <!-- 订单号：扫描录入 -->
        <szd-scan
          label="订单号"
          v-model="objData.header.aufnr"
          :scanCtrl="{ show: true }"
          readonly
          required
          :szdopt="SR.szdopt"
          @change="handleOrderScan" />
        <szd-date label="凭证过帐日期" v-model="objData.header.budat" :szdopt="SR.szdopt" required />
        <szd-input label="业务标题" v-model="objData.header.title" :szdopt="SR.szdopt" :maxlength="255" />
        <szd-textarea label="备注" v-model="objData.header.remark" :szdopt="SR.szdopt" :maxlength="150" />
      </template>
    </szd-card-a>
  </div>
</template>

<script>
  import { validateScan } from "../api/orderReport.js";

  export default {
    props: ["objData", "SR"],
    components: {},
    data() {
      return {
        // 已校验通过的订单号，用于跨次扫描对比
        savedOrderNo: ""
      };
    },
    methods: {
      // 扫描订单号：后4位=工序编号，前面=订单号
      async handleOrderScan() {
        const code = this.objData.header.aufnr;
        if (!code || code.length < 5) {
          this.$mW("扫描结果格式不正确，至少需要5位（订单号+4位工序编号）");
          this.objData.header.aufnr = this.savedOrderNo;
          return;
        }
        // 后4位为工序编号，前面为订单号
        const operNo = code.slice(-4);
        const orderNo = code.slice(0, -4);

        // 校验唯一订单号
        if (this.savedOrderNo && this.savedOrderNo !== orderNo) {
          this.$mW(`订单号不一致，当前单据订单号为${this.savedOrderNo}，扫描到${orderNo}`);
          this.objData.header.aufnr = this.savedOrderNo;
          return;
        }

        // 工序去重
        if (this.objData.itemList.some(r => r.operNo === operNo)) {
          this.$mW(`工序编号${operNo}已存在`);
          this.objData.header.aufnr = this.savedOrderNo;
          return;
        }

        // 调用后端校验
        try {
          const res = await validateScan(orderNo, operNo);
          if (res.code !== 200) {
            this.$mW(res.msg || "校验失败");
            this.objData.header.aufnr = this.savedOrderNo;
            return;
          }
          const operDesc = res.data.operDesc || "";

          // 校验通过，记住订单号
          this.savedOrderNo = orderNo;
          this.objData.header.aufnr = orderNo;

          // 新增行
          this.objData.itemList.push({
            itemNo: 0,
            operNo: operNo,
            confText: operDesc,
            yield: "",
            scrap: "",
            rework: ""
          });
          this.setItemNo();
          this.$mS(`已添加工序${operNo}`);
        } catch {
          this.$mW("校验接口调用失败");
          this.objData.header.aufnr = this.savedOrderNo;
        }
      },
      // 设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      }
    }
  };
</script>
