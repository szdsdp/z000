import { createRouter, createWebHistory } from "vue-router";
import Layout from "szd-client-app/layout/layout";

const SYS_CODE = process.env.VUE_APP_SYS_CODE;
const BASE_PATH = "/app-" + SYS_CODE.toLowerCase();

export const routes = [
  {
    path: "/",
    redirect: `/index`
  },
  {
    path: `/${SYS_CODE.toLowerCase()}_app_00a`, //请根据实际修正路由
    redirect: `/index`
  },
  {
    path: `/login`,
    name: "login",
    component: () => import("szd-client-app/layout/login/login.vue")
  },
  {
    path: "",
    component: Layout,
    redirect: "index",
    hidden: true,
    name: "Layout",
    children: [
      {
        path: "index",
        component: () => import("szd-client-app/layout/home/home.vue"),
        name: "home",
        meta: {
          title: "首页",
          keepAlive: false,
          affix: true
        }
      }
    ]
  },
  {
    path: "/:catchAll(.*)",
    hidden: true,
    component: () => import("szd-client-app/layout/error/404.vue")
  }
];

const router = createRouter({
  history: createWebHistory(BASE_PATH),
  routes
});

export default router;