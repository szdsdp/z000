<template>
  <div class="szd__tb__wrapper" style="padding-top: 5px">
    <!-- 功能按钮 -->
    <div class="szd__tb__top" v-if="['A', 'B', 'C'].includes(objData.header.bussStatus) && SR.szdopt === 'U'" style="justify-content: space-between">
      <div>
        <szd-checkbox-all v-model:data="objData.adjustList" :szdopt="SR.szdopt" @selection-change="handleSelRow" />
      </div>
      <div>
        <szd-button label="新增" :szdopt="SR.szdopt" @click="addRow" />
        <szd-button label="删除" :szdopt="SR.szdopt" @click="delRow" />
      </div>
    </div>
    <!-- 调整分录卡片列表 -->
    <div class="szd__tb__main">
      <szd-card-a v-for="row in objData.adjustList" :key="row.itemNo" :footer="false">
        <template #header>
          <szd-card-row-title v-model="row.$SZDCHECKBOX" :index="row.itemNo" checkbox :btnDelete="SR.szdopt == 'U'" @delete="handleRowDelete(row)" />
        </template>
        <template #main>
          <szd-input
            label="会计科目"
            v-model="row.accs"
            showHelp
            :help="{ a: 'CORE_ACCS', b: row, c: ['accs-accs', 'accsName-accsName'] }"
            :szdopt="SR.szdopt"
            :desc="row.accsName" />
          <szd-select label="借贷" v-model="row.drCr" baseField="CORE_VCH_ACCSH" :szdopt="SR.szdopt" />
          <szd-input label="金额" v-model="row.amt" :szdopt="SR.szdopt" type="number" />
          <szd-input
            label="供应商"
            v-model="row.supp"
            showHelp
            :help="{ a: 'CORE_SUPP', b: row, c: ['supp-supp', 'suppName-suppName'] }"
            :szdopt="SR.szdopt"
            :desc="row.suppName" />
          <szd-input
            label="客户"
            v-model="row.cust"
            showHelp
            :help="{ a: 'CORE_CUST', b: row, c: ['cust-cust', 'custName-custName'] }"
            :szdopt="SR.szdopt"
            :desc="row.custName" />
          <szd-input label="特别总账标识" v-model="row.sgl" :szdopt="SR.szdopt" :maxlength="10" />
          <szd-input label="摘要" v-model="row.summary" :szdopt="SR.szdopt" :maxlength="200" />
          <szd-input
            label="成本中心"
            v-model="row.cstc"
            showHelp
            :help="{ a: 'CORE_CSTC', b: row, c: ['cstc-cstc', 'cstcName-cstcName'] }"
            :szdopt="SR.szdopt"
            :desc="row.cstcName" />
          <szd-input
            label="利润中心"
            v-model="row.prfc"
            showHelp
            :help="{ a: 'CORE_PRFC', b: row, c: ['prfc-prfc', 'prfcName-prfcName'] }"
            :szdopt="SR.szdopt"
            :desc="row.prfcName" />
          <szd-input
            label="业务范围"
            v-model="row.busc"
            showHelp
            :help="{ a: 'CORE_BUSC', b: row, c: ['busc-busc', 'buscName-buscName'] }"
            :szdopt="SR.szdopt"
            :desc="row.buscName" />
          <szd-input label="分配编号" v-model="row.assignNo" :szdopt="SR.szdopt" :maxlength="50" />
          <szd-input
            label="原因代码"
            v-model="row.reas"
            showHelp
            :help="{ a: 'CORE_REAS', b: row, c: ['reas-reas', 'reasName-reasName'] }"
            :szdopt="SR.szdopt"
            :desc="row.reasName" />
          <szd-input label="采购凭证" v-model="row.poDoc" :szdopt="SR.szdopt" :maxlength="50" />
          <szd-input label="采购凭证项目" v-model="row.poDocItem" :szdopt="SR.szdopt" type="number" />
          <szd-input
            label="内部订单"
            v-model="row.orderCode"
            showHelp
            :help="{ a: 'SEXP_ORDE', b: row, c: ['orderCode-orderCode', 'orderName-orderName'] }"
            :szdopt="SR.szdopt"
            :desc="row.orderName" />
          <szd-input
            label="段"
            v-model="row.segm"
            showHelp
            :help="{ a: 'CORE_SEGM', b: row, c: ['segm-segm', 'segmName-segmName'] }"
            :szdopt="SR.szdopt"
            :desc="row.segmName" />
          <szd-input
            label="功能范围"
            v-model="row.fusc"
            showHelp
            :help="{ a: 'CORE_FUSC', b: row, c: ['fusc-fusc', 'fuscName-fuscName'] }"
            :szdopt="SR.szdopt"
            :desc="row.fuscName" />
          <szd-input label="固定资产" v-model="row.assetNo" :szdopt="SR.szdopt" :maxlength="50" />
          <szd-input label="资产次级编号" v-model="row.assetNoSub" :szdopt="SR.szdopt" :maxlength="10" />
          <szd-input label="WBS项目号" v-model="row.wbsNo" :szdopt="SR.szdopt" :maxlength="50" />
        </template>
      </szd-card-a>
    </div>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return { listSel: [] };
    },
    methods: {
      // 选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 新增行
      addRow() {
        this.objData.adjustList.push({
          itemNo: "",
          accs: "",
          accsName: "",
          drCr: "",
          amt: "",
          supp: "",
          suppName: "",
          cust: "",
          custName: "",
          sgl: "",
          summary: "",
          cstc: "",
          cstcName: "",
          prfc: "",
          prfcName: "",
          busc: "",
          buscName: "",
          assignNo: "",
          reas: "",
          reasName: "",
          poDoc: "",
          poDocItem: "",
          orderCode: "",
          orderName: "",
          segm: "",
          segmName: "",
          fusc: "",
          fuscName: "",
          assetNo: "",
          assetNoSub: "",
          wbsNo: ""
        });
        this.setItemNo();
      },
      // 批量删除行
      delRow() {
        if (this.listSel.length === 0) {
          this.$mW("请至少选择一行需要删除的数据");
          return;
        }
        this.objData.adjustList = this.objData.adjustList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      // 单行删除
      handleRowDelete(row) {
        this.objData.adjustList = this.objData.adjustList.filter(item => item !== row);
        this.setItemNo();
      },
      // 设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.adjustList.length; i++) {
          this.objData.adjustList[i].itemNo = i + 1;
        }
      }
    }
  };
</script>
