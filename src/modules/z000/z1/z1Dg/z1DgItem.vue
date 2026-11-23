<template>
  <!-- 页标签信息 -->
  <el-tabs v-model="SR.SRI" type="border-card">
    <el-tab-pane name="fold">
      <template #label>
        <el-icon style="font-size: 18px; color: #1890ff">
          <FolderRemove />
        </el-icon>
      </template>
    </el-tab-pane>
    <el-tab-pane label="项目信息" name="Tab1">
      <z1ItemTab10 :objData="objData" :SR="SR" :wfOper="wfOper" @updChgRow="updChgRow" />
    </el-tab-pane>
  </el-tabs>
</template>
<script>
  import z1ItemTab10 from "./z1ItemTab10.vue";

  export default {
    props: ["objData", "SR", "wfOper"],
    components: {
      z1ItemTab10
    },

    data() {
      return {
      };
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
      },
    }
  };
</script>
