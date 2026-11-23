import { createApp } from "vue";
import App from "./App.vue";
import ElementPlus, { ElDialog } from "element-plus";
import router from "@/router";
import store from "./store";
import "element-plus/dist/index.css";
import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
//自定义样式
import "szd-client-web/client/assets/styles/element-ui-appContainer.scss";
import "szd-client-web/client/assets/styles/index.scss";

//前置引用
import installDependency from "szd-client-web/index.js";
//乾坤
import qiankunMicro from "szd-client-web/client/common/qiankun/qiankunMicro";
import "./public-path";

export let app = null;

// 挂载应用
function render(props = {}) {
  const { container } = props;

  if (props["szdMircoProps"]) {
    app = createApp(App, { szdMircoProps: props["szdMircoProps"] });
  } else {
    app = createApp(App);
  }

  installDependency(app);
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
  }

  app.use(store);
  app.use(router);
  app.use(ElementPlus, {
    locale: zhCn,
    size: "small"
  });
  app.mount(container ? container.querySelector("#app") : "#app");
}

// 设置全局属性 弹窗默认点击遮罩层不关闭
ElDialog.props.closeOnClickModal = { default: false };
// 关闭form无效检查警告
window.ASYNC_VALIDATOR_NO_WARNING = true;

// 静默 ResizeObserver loop 报错（浏览器无害警告，Element Plus 弹框内部表格触发）
window.addEventListener("error", (e) => {
  if (e.message === "ResizeObserver loop completed with undelivered notifications.") {
    e.stopImmediatePropagation();
  }
});

// 独立运行时
if (!window.__POWERED_BY_QIANKUN__) {
  render();
}

// qiankun的生命周期
export async function bootstrap() {
}

export async function mount(props) {
  qiankunMicro.setActions(props);
  render(props);
}

export async function unmount() {
  app.unmount();
  app = null;
}
