import { addMsg, checkNull } from "./tool/tool";

export const z000Tool = {
  checkNull,
  addMsg
};

const installZ000CommonFunction = app => {
  app.config.globalProperties.z000Tool = z000Tool;
};

export default installZ000CommonFunction;
