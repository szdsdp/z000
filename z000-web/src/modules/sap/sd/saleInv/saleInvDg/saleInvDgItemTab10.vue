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
      <el-table-column label="交货单单号" prop="deliveryBussDocId" min-width="150">
        <template v-slot="scope">
          <szd-input v-model="scope.row.deliveryBussDocId" disabled />
        </template>
      </el-table-column>
      <el-table-column label="交货单号" prop="vbelnVl" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="交货单号" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.vbelnVl" disabled />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    props: ["objData", "SR", "wfOper"],
    data() {
      return {
        listSel: []
      };
    },
    methods: {
      handleSelRow(selection) {
        this.listSel = selection;
      },
      // 新增行-搜索帮助选择交货单
      addRow() {
        this.szdShShow({
          a: "Z000_SD_SALE_DELIVERY_SAP"
        }).then(res => {
          if (res.data && res.data.length > 0) {
            for (let i = 0; i < res.data.length; i++) {
              let item = res.data[i];
              // 交货单号去重，已存在则跳过
              if (this.objData.itemList.some(r => r.vbelnVl === item.vbelnVl)) {
                continue;
              }
              const newRow = {
                itemNo: 0,
                vbelnVl: item.vbelnVl || "",
                deliveryBussDocId: ""
              };
              this.objData.itemList.push(newRow);
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
        // 重新生成行号
        for (let i = 0; i < this.objData.itemList.length; i++) {
          this.objData.itemList[i].itemNo = i + 1;
        }
      }
    }
  };
</script>
