<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <div class="main-table-title" style="height: 35px">
      <div class="main-table-title-left" style="width: 50%"></div>
      <div class="main-table-title-right" style="width: 50%; align-items: center">
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
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">{{ scope.row.itemNo }}</template>
      </el-table-column>
      <el-table-column label="工序编号" prop="operNo" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="工序编号" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.operNo"
            showHelp
            :help="{
              a: 'Z000_PP_SCDDCQ_PROCESS',
              b: scope.row,
              c: ['operNo-operNo'],
              d: [['orderNo', objData.header.aufnr]]
            }"
            :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="确认文本" prop="confText" min-width="200">
        <template v-slot:header>
          <szd-table-col-header label="确认文本" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.confText" :szdopt="SR.szdopt" :maxlength="40" />
        </template>
      </el-table-column>
      <el-table-column label="产量" prop="yield" min-width="120">
        <template v-slot:header>
          <szd-table-col-header required label="产量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.yield" :szdopt="SR.szdopt" :maxlength="13" />
        </template>
      </el-table-column>
      <el-table-column label="废品" prop="scrap" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="废品" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.scrap" :szdopt="SR.szdopt" :maxlength="13" />
        </template>
      </el-table-column>
      <el-table-column label="返工数量" prop="rework" min-width="120">
        <template v-slot:header>
          <szd-table-col-header label="返工数量" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.rework" :szdopt="SR.szdopt" :maxlength="13" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return { listSel: [] };
    },
    methods: {
      handleSelRow(selection) {
        this.listSel = selection;
      },
      addRow() {
        this.szdShShow({
          a: "Z000_PP_SCDDCQ_PROCESS",
          d: [["orderNo", this.objData.header.aufnr]]
        }).then(res => {
          if (res.data && res.data.length > 0) {
            for (let item of res.data) {
              // operNo 去重，已存在的工序不再添加
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
      delRow() {
        this.objData.itemList = this.objData.itemList.filter(item => !this.listSel.some(ele => ele === item));
        this.setItemNo();
      },
      setItemNo() {
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      }
    }
  };
</script>
