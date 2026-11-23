import { createRouter, createWebHistory } from "vue-router";
//框架引入
import Layout from "szd-client-web/layout/layout";
//路由
export const routes = [
  {
    path: "/login",
    component: () => import("szd-client-web/layout/login/login"),
    hidden: true
  },
  {
    path: "",
    component: Layout,
    redirect: "index",
    name: "Layout",
    hidden: true,
    children: [
      {
        path: "index",
        component: () => import("szd-client-web/layout/home/home.vue"),
        name: "home",
        meta: {
          title: "首页",
          icon: "dashboard",
          keepAlive: false,
          affix: true
        }
      },
      {
        path: "z000/:page(index)?",
        redirect: "index",
      }
    ]
  },
  {
    path: "/:catchAll(.*)",
    hidden: true,
    component: () => import("szd-client-web/layout/layout/error/404.vue")
  }
];

//路由基路径：乾坤下为 /micro/，独立运行时为 /系统代码小写/（与 publicPath、nginx 前缀保持一致）
const baseUrl = window.__POWERED_BY_QIANKUN__ ? "/" + process.env.VUE_APP_SYS_CODE.toLowerCase() + "/" : "/";
const router = createRouter({
  history: createWebHistory(baseUrl),
  routes
});

export default router;
