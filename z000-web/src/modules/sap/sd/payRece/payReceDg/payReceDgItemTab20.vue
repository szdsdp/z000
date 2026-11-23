<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <div class="main-table-title">
      <div class="main-table-title-left" style="width: 50%" />
      <div class="main-table-title-right" style="width: 50%">
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="新增"
          type="primary"
          link
          @click="addRow"
          icon="Plus"
          :szdopt="SR.szdopt" />
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="删除"
          type="primary"
          link
          @click="delRow"
          icon="Delete"
          :szdopt="SR.szdopt" />
      </div>
    </div>
    <el-table ref="refTable" border stripe :data="objData.itemList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="60" align="center" fixed="left">
        <template v-slot="scope">{{ scope.row.itemNo }}</template>
      </el-table-column>
      <el-table-column label="会计凭证" prop="vchNum" width="120" />
      <el-table-column label="财年" prop="vchYear" width="80" />
      <el-table-column label="凭证行号" prop="vchNumItem" min-width="100" />
      <el-table-column label="凭证类型" prop="blart" min-width="100" />
      <el-table-column label="凭证货币金额" prop="amt" min-width="120">
        <template v-slot="scope">
          <szd-dot :text="scope.row.amt" type="number" numDot :numDec="2" />
        </template>
      </el-table-column>
      <el-table-column label="清账金额" prop="clearAmt" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="清账金额" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.clearAmt" :szdopt="SR.szdopt" type="number" numDot :numDec="2" @change="handleClearAmtChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="科目" prop="accs" min-width="100" />
      <el-table-column label="科目描述" prop="accsName" min-width="180" />
      <el-table-column label="记帐代码" prop="bschl" min-width="80" />
      <el-table-column label="借贷方" prop="drCr" min-width="80" />
      <el-table-column label="客户" prop="cust" min-width="100" />
      <el-table-column label="客户名称" prop="custName" min-width="180" />
      <el-table-column label="供应商" prop="lifnr" min-width="100" />
      <el-table-column label="供应商名称" prop="lifnrT" min-width="180" />
      <el-table-column label="抬头文本" prop="bkTxt" min-width="150" />
      <el-table-column label="项目文本" prop="sgtxt" min-width="150" />
      <el-table-column label="分配编号" prop="zuonr" min-width="120" />
      <el-table-column label="货币码" prop="curr" min-width="80" />
    </el-table>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() { return { listSel: [] }; },
    methods: {
      addRow() {
        this.szdShShow({
          a: "Z000_PAY_CLEAR",
          d: [
            ["bukrs", this.objData.header.cmpy],
            ["kunnr", this.objData.header.cust]
          ],
          f: 2
        }).then(res => {
          if (res.data && res.data.length > 0) {
            for (let i = 0; i < res.data.length; i++) {
              let item = res.data[i];
              if (this.objData.itemList.find(r => r.vchNum === item.belnr && r.vchYear === item.gjahr && r.vchNumItem === item.buzei)) {
                continue;
              }
              this.objData.itemList.push({
                itemNo: 0,
                cmpy: item.bukrs,
                vchNum: item.belnr,
                vchYear: item.gjahr,
                vchNumItem: item.buzei,
                blart: item.blart,
                bkTxt: item.bktxt,
                curr: item.waers,
                bschl: item.bschl,
                drCr: item.shkzg,
                amt: item.wrbtr,
                lifnr: item.lifnr,
                lifnrT: item.lifnrT,
                cust: item.kunnr,
                custName: item.kunnrT,
                accs: item.hkont,
                accsName: item.hkontT,
                zuonr: item.zuonr,
                sgtxt: item.sgtxt,
                clearAmt: 0
              });
            }
            this.setItemNo();
          }
        });
      },
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      },
      handleSelRow(selection) { this.listSel = selection; },
      delRow() {
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
      },
      handleClearAmtChange(row) {
        this.$nextTick(() => {
          if (row.clearAmt > row.amt) {
            this.$mW("本次清账金额不能大于凭证货币金额");
            row.clearAmt = row.amt;
          }
        });
      }
    }
  };
</script>
