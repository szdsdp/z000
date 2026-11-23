<template>
  <!-- 交货计划对话框 -->
  <el-dialog
    :title="$t('交货计划')"
    v-model="dg.open"
    append-to-body
    :close-on-click-modal="false"
    class="app-dialog-container"
    align-center
    destroy-on-close
    draggable>
    <div v-szd-dialog="[700, 400]" />

    <!-- 功能按钮 -->
    <div class="main-table-title" style="margin-top: 5px">
      <div class="main-table-title-left" style="width: 60%">
        <span>行项目: {{ currentItemNo }}</span>
      </div>
      <div class="main-table-title-right" style="width: 40%">
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="新增行"
          type="primary"
          link
          icon="Plus"
          @click="addSchedRow"
          :szdopt="SR.szdopt" />
        <szd-button
          v-if="['A', 'B', 'C'].includes(objData.header.bussStatus)"
          label="删除行"
          type="primary"
          link
          icon="Delete"
          @click="delSchedRow"
          :szdopt="SR.szdopt" />
      </div>
    </div>
    <!-- 表格输出 -->
    <el-table ref="refSchedTable" border stripe :data="currentSchedList" @selection-change="handleSchedSelRow">
      <el-table-column align="center" type="selection" width="40" />
      <el-table-column label="交货计划行号" prop="schedLine" width="120" align="center">
        <template v-slot="scope">
          {{ scope.row.schedLine }}
        </template>
      </el-table-column>
      <el-table-column label="交货日期" prop="deliveryDate" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="交货日期" />
        </template>
        <template v-slot="scope">
          <szd-date v-model="scope.row.deliveryDate" :szdopt="SR.szdopt" type="date" />
        </template>
      </el-table-column>
      <el-table-column label="已计划数量" prop="quantity" min-width="130">
        <template v-slot:header>
          <szd-table-col-header required label="已计划数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.quantity" :szdopt="SR.szdopt" :maxlength="13" />
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <szd-button label="关闭" type="primary" @click="closeForm" />
    </template>
  </el-dialog>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {
        dg: {
          open: false,
          option: "U"
        },
        currentItemNo: "",
        schedSel: []
      };
    },

    computed: {
      currentSchedList() {
        if (!this.currentItemNo) return [];
        if (!this.objData.schedList) this.objData.schedList = [];
        return this.objData.schedList.filter(s => s.itemNo === this.currentItemNo);
      }
    },

    methods: {
      // 接收行项目数据并打开弹窗，默认填充一行交货计划
      acceptData(row) {
        this.dg.open = true;
        this.currentItemNo = row.itemNo;
        // 如果该行项目还没有交货计划，默认新增一行：交货日期=行项目交货日期，数量=采购数量
        if (!this.objData.schedList) this.objData.schedList = [];
        const existing = this.objData.schedList.filter(s => s.itemNo === row.itemNo);
        if (existing.length === 0) {
          this.objData.schedList.push({
            orderId: this.objData.header.orderId || "",
            itemNo: row.itemNo,
            schedLine: 1,
            deliveryDate: row.deliveryDate || "",
            quantity: row.purNum || ""
          });
        }
      },
      // 关闭弹窗
      closeForm() {
        this.dg.open = false;
      },
      // 多选行
      handleSchedSelRow(selection) {
        this.schedSel = selection;
      },
      // 新增交货计划行
      addSchedRow() {
        if (!this.objData.schedList) this.objData.schedList = [];
        const existing = this.currentSchedList;
        const maxLine = existing.reduce((max, s) => {
          const n = parseInt(s.schedLine) || 0;
          return n > max ? n : max;
        }, 0);
        this.objData.schedList.push({
          orderId: this.objData.header.orderId || "",
          itemNo: this.currentItemNo,
          schedLine: maxLine + 1,
          deliveryDate: "",
          quantity: ""
        });
      },
      // 删除选中的交货计划行
      delSchedRow() {
        if (!this.objData.schedList) return;
        this.objData.schedList = this.objData.schedList.filter(s => !this.schedSel.some(ele => ele === s));
      }
    }
  };
</script>
