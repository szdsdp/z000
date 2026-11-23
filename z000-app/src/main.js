import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import Varlet from "@varlet/ui";
import "@varlet/ui/es/style";
//自定义样式
import "szd-client-app/client/assets/styles/index.scss";

//前置引用
import installDependency from "szd-client-app/index.js";

export const app = createApp(App);

installDependency(app);
app.use(router);
app.use(store);
app.use(Varlet);

app.mount("#app");
