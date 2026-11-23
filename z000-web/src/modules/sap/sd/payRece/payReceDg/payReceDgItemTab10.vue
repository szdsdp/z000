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
    <el-table ref="refTable" border stripe :data="objData.itemListSale" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="60" align="center" fixed="left">
        <template v-slot="scope">{{ scope.row.itemNo }}</template>
      </el-table-column>
      <el-table-column label="销售合同" prop="contractId" min-width="120" />
      <el-table-column label="双签版合同号" prop="dualSignContract" min-width="150" />
      <el-table-column label="开始日期" prop="effStartDate" min-width="100" />
      <el-table-column label="截止日期" prop="effEndDate" min-width="100" />
      <el-table-column label="订单金额" prop="contractAmt" min-width="120">
        <template v-slot="scope">
          <szd-dot :text="scope.row.contractAmt" :numDec="2" numDot />
        </template>
      </el-table-column>
      <el-table-column label="认领金额" prop="clearAmt" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="认领金额" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.clearAmt" :szdopt="SR.szdopt" type="number" numDot :numDec="2" :numMax="1000000000" :numMin="0" @change="handleClearAmtChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="已认领金额" prop="amtReceipt" min-width="120">
        <template v-slot="scope">
          <szd-dot :text="scope.row.amtReceipt" :numDec="2" numDot />
        </template>
      </el-table-column>
      <el-table-column label="剩余金额" prop="amtRemain" min-width="120">
        <template v-slot="scope">
          <szd-dot :text="scope.row.amtRemain" :numDec="2" numDot />
        </template>
      </el-table-column>
      <el-table-column label="销售组织" prop="salesOrg" min-width="100" />
      <el-table-column label="分销渠道" prop="distChannel" min-width="100" />
      <el-table-column label="终端用户" prop="endUser" min-width="100" />
      <el-table-column label="国际贸易条款" prop="incoterm" min-width="100" />
      <el-table-column label="付款条件" prop="payTermsDesc" min-width="150" />
    </el-table>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() { return { listSel: [] }; },
    methods: {
      addRow() {
        if (!this.objData.header.cust) {
          this.$mW("请先选择客户信息！");
          return;
        }
        this.szdShShow({
          a: "Z000_SALE_CONTRACT",
          d: [
            ["soldTo", this.objData.header.cust],
            ["currency", this.objData.header.curr]
          ],
          f: 2
        }).then(res => {
          if (res.data && res.data.length > 0) {
            for (let i = 0; i < res.data.length; i++) {
              let item = res.data[i];
              if (!this.objData.itemListSale.find(r => r.contractId === item.contractId)) {
                this.objData.itemListSale.push({
                  itemNo: 0, contractId: item.contractId, dualSignContract: item.dualSignContract,
                  effStartDate: item.effStartDate, effEndDate: item.effEndDate,
                  contractAmt: item.contractAmt, clearAmt: 0, amtReceipt: 0, amtRemain: item.contractAmt,
                  salesOrg: item.salesOrg, distChannel: item.distChannel, endUser: item.endUser,
                  incoterm: item.incoterm, payTermsDesc: item.payTermsDesc
                });
              }
            }
            this.setItemNo();
          }
        });
      },
      setItemNo() {
        for (let i = 0; i < this.objData.itemListSale.length; i++) {
          this.objData.itemListSale[i].itemNo = i + 1;
        }
      },
      handleClearAmtChange(row) {
        this.$nextTick(() => {
          if (row.clearAmt > row.contractAmt) {
            row.clearAmt = 0;
            this.$mW("本次认领金额不能大于本销售订单金额");
          }
        });
      },
      handleSelRow(selection) { this.listSel = selection; },
      delRow() {
        this.objData.itemListSale = this.objData.itemListSale.filter(item => !this.listSel.some(ele => ele === item));
      }
    }
  };
</script>
