<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <el-table ref="refTable" border stripe :data="objData.itemList" class="flex-1">
      <el-table-column label="行号" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">{{ scope.row.itemNo }}</template>
      </el-table-column>
      <el-table-column label="物料编码" prop="mtlCode" min-width="160">
        <template v-slot:header>
          <szd-table-col-header label="物料编码" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.mtlCode" disabled />
        </template>
      </el-table-column>
      <el-table-column v-if="!isBlind" label="台账数量" prop="sysQnt" min-width="130">
        <template v-slot:header>
          <szd-table-col-header label="台账数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.sysQnt" disabled type="number" numDot :numDec="3" />
        </template>
      </el-table-column>
      <el-table-column label="盘点数量" prop="countQnt" min-width="130">
        <template v-slot:header>
          <szd-table-col-header required label="盘点数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.countQnt" :szdopt="SR.szdopt" type="number" numDot :numDec="3" @change="calcDiff(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="差异数量" prop="diffQnt" min-width="130">
        <template v-slot:header>
          <szd-table-col-header label="差异数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.diffQnt" disabled type="number" numDot :numDec="3" />
        </template>
      </el-table-column>
      <el-table-column v-if="!isBlind" label="盘点状态" prop="countStatus" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="盘点状态" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.countStatus" baseField="Z000_KCPD_COUNT_STATUS" :szdopt="'V'" />
        </template>
      </el-table-column>
      <el-table-column v-if="!isBlind" label="差异说明" prop="diffDesc" min-width="180">
        <template v-slot:header>
          <szd-table-col-header label="差异说明" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.diffDesc" :szdopt="SR.szdopt" :maxlength="200" />
        </template>
      </el-table-column>
      <el-table-column label="单位" prop="unit" min-width="100">
        <template v-slot:header>
          <szd-table-col-header label="单位" />
        </template>
        <template v-slot="scope">
          <szd-select v-model="scope.row.unit" baseField="CORE_UNIT" :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="盘点人" prop="counter" min-width="140">
        <template v-slot:header>
          <szd-table-col-header required label="盘点人" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.counter"
            showHelp
            :help="{ a: 'CORE_USER', b: scope.row, c: ['counter-userId', 'counterName-userName'] }"
            :szdopt="SR.szdopt"
            :descSpan="10"
            :desc="scope.row.counterName" />
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="itemText" min-width="180">
        <template v-slot:header>
          <szd-table-col-header label="备注" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.itemText" :szdopt="SR.szdopt" :maxlength="200" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    inject: ["isBlind"],
    methods: {
      calcDiff(row) {
        const sysQnt = parseFloat(row.sysQnt) || 0;
        const countQnt = parseFloat(row.countQnt) || 0;
        row.diffQnt = countQnt - sysQnt;
        if (row.diffQnt > 0) {
          row.countStatus = "02";
          row.moveType = "701";
        } else if (row.diffQnt < 0) {
          row.countStatus = "03";
          row.moveType = "702";
        } else {
          row.countStatus = "01";
          row.moveType = "";
        }
      }
    }
  };
</script>
