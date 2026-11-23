<template>
  <div class="app-sub-container szd__tb__wrapper" style="padding: 0">
    <!-- 功能按钮 -->
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
    <!-- 表单输出 -->
    <el-table ref="refTable" border stripe :data="objData.bkacList" @selection-change="handleSelRow" class="flex-1">
      <el-table-column align="center" type="selection" width="40" fixed="left" />
      <el-table-column :label="$t('行项目')" prop="itemNo" width="80" align="center" fixed="left">
        <template v-slot="scope">
          {{ scope.row.itemNo }}
        </template>
      </el-table-column>
      <el-table-column label="银行账号" prop="acctNo" width="250">
        <template v-slot:header>
          <szd-table-col-header label="银行账户" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.acctNo" :szdopt="SR.szdopt" :maxlength="32" />
        </template>
      </el-table-column>
      <el-table-column label="账户名称" prop="acctName" min-width="200">
        <template v-slot:header>
          <szd-table-col-header label="账户名称" />
        </template>
        <template v-slot="scope">
          <szd-input v-model="scope.row.acctName" :szdopt="SR.szdopt" :maxlength="128" />
        </template>
      </el-table-column>
      <el-table-column label="开户行" prop="cnap" min-width="250">
        <template v-slot:header>
          <szd-table-col-header label="联行号" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-model="scope.row.cnap"
            showHelp
            readonly
            :help="{
              a: 'CORE_CNAP',
              b: scope.row,
              c: ['cnap-cnap', 'cnapName-cnapName']
            }"
            :szdopt="SR.szdopt"
            :descSpan="16"
            :desc="scope.row.cnapName" />
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
      //选择行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      addRow() {
        this.objData.bkacList.push({
          itemNo: 1,
          acctNo: "",
          acctName: "",
          cnap: "",
          cnapName: ""
        });
        this.setItemNo();
      },
      //删除行
      delRow() {
        this.objData.bkacList = this.objData.bkacList.filter(item => !this.listSel.some(ele => ele === item));
      },
      //设置行号
      setItemNo() {
        for (let i = 0; i < this.objData.bkacList.length; i++) {
          this.objData.bkacList[i].itemNo = i + 1;
        }
      }
    }
  };
</script>
