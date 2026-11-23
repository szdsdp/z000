import szdCalc from "szd-client-web/client/common/tools/calculate";
import { szdWf } from "szd-client-web/client/wf/indexFunction";

//生成新增行
export function calcAddRow(objData, SR, objList, moduleName) {
  return new Promise(resolve => {
    szdWf.shBsubDef(objData.header.bstp, moduleName).then(resBsub => {
      //准备行数据
      let itemTemp = calcAddRowPrepareBase(objData, SR, resBsub);
      //获取最大行号
      itemTemp.itemNo = getMaxItemNo(objList);
      resolve(itemTemp);
    });
  });
}

//准备行数据
export function calcAddRowPrepareBase(objData, SR, resBsub) {
  //生成数据
  return {
    itemNo: 0,
    bsub: resBsub ? resBsub.bsub : "",
    bsubName: resBsub ? resBsub.bsubName : "",
    deduFlag: "A",
    taxr: "",
    amt: 0,
    amtTax: 0,
    amtNoTax: 0,
    amtDedu: 0,
    amtDeduTax: 0,
    amtDeduNoTax: 0,
    amtSum: 0,
    amtSumTax: 0,
    amtSumNoTax: 0,
    invNo: "",
    invCode: "",
    remark: ""
  };
}

//获取最大行号
export function getMaxItemNo(objList) {
  let itemNo_max = 0;
  for (let i = 0; i < objList.length; i++) {
    if (itemNo_max < objList[i].itemNo) itemNo_max = objList[i].itemNo;
  }
  return itemNo_max + 1;
}

//税率
export function calcTax(objData, SR, row) {
  row.amtTax = szdCalc.taxInc(row.amt, row.taxr);
  calcAmtItem(objData, SR, row);
}

//金额
export function calcAmt(objData, SR, row) {
  row.amtTax = szdCalc.taxInc(row.amt, row.taxr);
  calcAmtItem(objData, SR, row);
}

//税额
export function calcAmtTax(objData, SR, row) {
  if (row.amtTax > row.amt) {
    row.amtTax = szdCalc.taxInc(row.amt, row.taxr);
  }
  calcAmtItem(objData, SR, row);
}

// 合计项目信息
export function calcAmtItem(objData, SR, row) {
  row.amtNoTax = row.amt - row.amtTax;
  calcAmtHeader(objData);
}

// 合计信息
export function calcAmtHeader(objData) {
  objData.header.amtApply = 0;

  //其他
  for (let j = 0; j < objData.itemList.length; j++) {
    let item = objData.itemList[j];
    objData.header.amtApply += item.amt;
  }
}



