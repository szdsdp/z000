<template>
  <!-- 添加或修改参数配置对话框 -->
  <el-dialog
    :title="$t('业务伙伴')"
    v-model="dg.open"
    append-to-body
    :close-on-click-modal="false"
    class="app-dialog-container"
    align-center
    destroy-on-close
    draggable>
    <div v-szd-dialog="[800, 500]" />

    <!-- 功能按钮 -->
    <div class="main-table-title" style="margin-top: 5px">
      <div class="main-table-title-left" style="width: 60%">
        <span>{{"区域负责人与销售经理必须维护，其他不维护则默认为当前客商编码"}}</span>
      </div>
      <div class="main-table-title-right" style="width: 40%">
        <szd-button label="新增" type="primary" link icon="Plus" @click="addRow" :szdopt="SR.szdopt"/>
        <szd-button label="删除" type="primary" link icon="Delete" @click="delRow" :szdopt="SR.szdopt"/>
      </div>
    </div>
    <!-- 表格输出 -->
    <el-table border ref="table" stripe :data="objData.partnerList" @selection-change="handleSelRow">
      <el-table-column align="center" type="selection" width="40" />
      <el-table-column label="伙伴类型" prop="partnerType" min-width="150">
        <template v-slot:header>
          <szd-table-col-header required label="伙伴类型" />
        </template>
        <template v-slot="scope">
          <szd-select
            v-model="scope.row.partnerType"
            baseField="Z000_MDM_CUST_PARTNER"
            :szdopt="SR.szdopt" />
        </template>
      </el-table-column>
      <el-table-column label="伙伴编码" prop="partnerCode" min-width="350">
        <template v-slot:header>
          <szd-table-col-header required label="伙伴编码" />
        </template>
        <template v-slot="scope">
          <szd-input-desc
            v-if="scope.row.partnerType === 'ZY' || scope.row.partnerType === 'ZZ'"
            v-model="scope.row.partnerCode"
            showHelp
            readonly
            :help="{
              a: 'Z000_MDM_CUST',
              b: scope.row,
              c: ['partnerCode-custCode', 'partnerName-custName'],
              d: [['buType', '9000']]
            }"
            :szdopt="SR.szdopt"
            :descSpan="14"
            :desc="scope.row.partnerName" />
          <szd-input-desc
            v-else
            v-model="scope.row.partnerCode"
            showHelp
            readonly
            :help="{
              a: 'Z000_MDM_CUST',
              b: scope.row,
              c: ['partnerCode-custCode', 'partnerName-custName'],
            }"
            :szdopt="SR.szdopt"
            :descSpan="14"
            :desc="scope.row.partnerName" />
        </template>
      </el-table-column>
      <el-table-column :label="$t('操作')" align="center" width="80" fixed="right">
        <template v-slot="scope">
          <szd-button label="删除" type="primary" link @click="handleDelRow(scope.row)" icon="Delete" :szdopt="SR.szdopt" />
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
    props: ["SR", "wfOper"],
    data() {
      return {
        dg: {
          open: false,
          option: "C" //C创建 U更改 V显示
        },
        // partnerList: [],
        objData: {
          applyId: "",
          itemNo: "",
          saleOrg: "",
          partnerList: []
        }
      };
    },
    methods: {
      //初始化数据
      acceptData(param) {
        this.initData();
        this.dg.open = true;
        this.objData = param;
      },
      //关闭窗口
      closeForm() {
        this.dg.open = false;
      },
      //新增
      addRow() {
        this.objData.partnerList.push({});
      },
      //多选行
      handleSelRow(selection) {
        this.listSel = selection;
      },
      //多行删除
      delRow() {
        this.objData.partnerList = this.objData.partnerList.filter(item => !this.listSel.some(ele => ele === item));
      },
      //单行删除
      handleDelRow(row) {
        this.objData.partnerList = this.objData.partnerList.filter(item => item !== row);
      },
      //初始化数据
      initData() {
        this.objData = {
          applyId: "",
          itemNo: "",
          saleOrg: "",
          partnerList: []
        };
      }
    }
  };
</script>
