<template>
  <div class="szd__tb__wrapper" style="padding-top: 5px">
    <!-- 功能按钮 -->
    <div class="szd__tb__top" v-if="['A', 'B', 'C'].includes(objData.header.bussStatus) && SR.szdopt === 'U'" style="justify-content: space-between">
      <div>
        <szd-checkbox-all v-model:data="objData.itemList" :szdopt="SR.szdopt" @selection-change="handleSelRow" />
      </div>
      <div>
        <szd-button label="新增" :szdopt="SR.szdopt" @click="addRow" />
        <szd-button label="删除" :szdopt="SR.szdopt" @click="delRow" />
      </div>
    </div>
    <!-- 表格输出 -->
    <div class="szd__tb__main">
      <szd-card-a v-for="row in objData.itemList" :key="row.itemNo" :footer="false">
        <template #header>
          <szd-card-row-title v-model="row.$SZDCHECKBOX" :index="row.itemNo" checkbox :btnDelete="SR.szdopt == 'U'" @delete="handleRowDelete(row)" />
        </template>
        <template #main>
          <szd-input
            label="工序编号"
            v-model="row.operNo"
            required
            showHelp
            :help="{ a: 'Z000_PP_SCDDCQ_PROCESS', b: row, c: ['operNo-operNo'], d: [['orderNo', objData.header.aufnr]] }"
            :szdopt="SR.szdopt" />
          <szd-input label="确认文本" v-model="row.confText" :szdopt="SR.szdopt" :maxlength="40" />
          <szd-input label="产量" v-model="row.yield" :szdopt="SR.szdopt" required :maxlength="13" />
          <szd-input label="废品" v-model="row.scrap" :szdopt="SR.szdopt" :maxlength="13" />
          <szd-input label="返工数量" v-model="row.rework" :szdopt="SR.szdopt" :maxlength="13" />
        </template>
      </szd-card-a>
    </div>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    components: {},
    data() {
      return {
        listSel: []
      };
    },
    methods: {
      // 选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 新增行-搜索帮助选择工序
      addRow() {
        this.szdShShow({
          a: "Z000_PP_SCDDCQ_PROCESS",
          d: [["orderNo", this.objData.header.aufnr]]
        }).then(res => {
          if (res.data && res.data.length > 0) {
            for (let item of res.data) {
              // operNo 去重
              if (this.objData.itemList.some(r => r.operNo === item.operNo)) {
                continue;
              }
              this.objData.itemList.push({
                itemNo: 0,
                operNo: item.operNo,
                confText: item.operDesc || "",
                yield: "",
                scrap: "",
                rework: ""
              });
            }
            this.setItemNo();
          }
        });
      },
      // 批量删除行
      delRow() {
        if (this.listSel.length === 0) {
          this.$mW("请至少选择一行需要删除的数据");
          return;
        }
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      // 单行删除
      handleRowDelete(row) {
        this.objData.itemList = this.objData.itemList.filter(item => item !== row);
        this.setItemNo();
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
