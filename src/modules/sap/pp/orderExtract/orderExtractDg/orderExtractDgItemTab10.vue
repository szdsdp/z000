<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <!-- 功能按钮 -->
    <div class="main-table-title" style="height: 35px">
      <div class="main-table-title-left" style="width: 50%"></div>
      <div class="main-table-title-right" style="width: 50%; align-items: center">
        <szd-button label="生产报工" type="primary" @click="handleCreateOrderReport" link icon="Upload" />
      </div>
    </div>
    <el-table ref="refTable" border stripe :data="objData.processList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('序号')" type="index" width="60" align="center" fixed="left" />
      <el-table-column label="工艺路线编号" prop="routingNo" min-width="120" />
      <el-table-column label="工序内部编号" prop="operInnerNo" min-width="100" />
      <el-table-column label="工序编号" prop="operNo" min-width="130" />
      <el-table-column label="标准文本码" prop="stdTextCode" min-width="110" />
      <el-table-column label="工序描述" prop="operDesc" min-width="250" />
    </el-table>
  </div>
</template>

<script>
  import { createFromOrderExtract } from "../../orderReport/api/orderReport.js";

  export default {
    props: ["objData", "SR"],
    data() {
      return { listSel: [] };
    },
    methods: {
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 创建生产报工单
      handleCreateOrderReport() {
        if (!this.listSel || this.listSel.length === 0) {
          this.$mW("请先勾选工序行");
          return;
        }
        this.szdPop("确定要基于所选工序行创建生产报工单据吗？", true)
          .then(() => {
            let itemList = [];
            this.listSel.forEach(item => {
              itemList.push({
                operation: item.operNo || "",
                confText: item.operDesc || "",
                yield: "",
                scrap: "",
                rework: ""
              });
            });

            let data = {
              header: {
                routerCode: "Z000_PP_SCDDBG_01A",
                bstp: "Z000_PP_SCDDBG01",
                aufnr: this.objData.header.orderNo || ""
              },
              itemList: itemList
            };

            createFromOrderExtract(data)
              .then(response => {
                if (response.code === 200 && response.data.status === "S") {
                  this.$mS("创建生产报工成功");
                  this.szdWf.goDoc("Z000_PP_SCDDBG_01A", response.data.docId, "U");
                } else {
                  this.$mE("创建生产报工失败");
                }
              })
              .catch(() => {
                this.$mE("创建生产报工失败");
              });
          })
          .catch(() => {});
      }
    }
  };
</script>
