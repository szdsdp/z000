<template>
  <el-tabs v-model="SR.SRI" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff">
          <FolderRemove />
        </el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane :label="$t('项目信息')" name="Tab1">
      <expCostItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" @updChgRow="updChgRow" />
    </el-tab-pane>
    <el-tab-pane :label="$t('项目信息2')" name="Tab2">
      <expCostItemTab20 :objData="objData" :SR="SR" :wfOper="wfOper" @updChgRow="updChgRow" />
    </el-tab-pane>
  </el-tabs>
</template>
<script>
  import expCostItemTab10 from "./expCostItemTab10.vue";
  import expCostItemTab20 from "./expCostItemTab20.vue";
  import {FolderRemove} from "@element-plus/icons-vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      FolderRemove,
      expCostItemTab10,
      expCostItemTab20
    },

    data() {
      return {};
    },
    methods: {
      // 更新
      async updChgRow(fname) {
        switch (fname) {
          case "FCalcAmt":
            // 计算金额
            await this.calcAmt();
            break;
        }
      },

      // 金额汇总
      calcAmt() {
        this.objData.header.amtApply = 0;
        // 项目信息汇总
        this.calcAmtItem();
        this.objData.header = JSON.parse(JSON.stringify(this.objData.header));
      },
      // 项目信息汇总
      calcAmtItem() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.header.amtApply = this.toNum(this.objData.header.amtApply) + this.toNum(this.objData.itemList[i].amtApply);
        }
      },

      toNum(num) {
        if (!num || isNaN(num)) {
          return 0;
        }
        return Number(num);
      }
    }
  };
</script>