<template>
  <router-view v-slot="{ Component }">
    <component :is="Component"/>
  </router-view>
</template>

<script setup>
import {onMounted, watch} from "vue";
import {useRoute} from "vue-router";
import szdStoreSession from "szd-client-app/client/common/store/storeSession";
// 获取当前路由信息
const route = useRoute();
//监听路由变化
watch(
    () => route.path,
    () => {
      setFrame(route);
    }
);
//加载数据
onMounted(() => {
  setFrame(route);
});

const setFrame = to => {
  const loginUser = szdStoreSession.getCoreLogin();
  const routerObj = loginUser && loginUser.routesObj && to.name ? loginUser.routesObj[to.name] : "";
  //设置标题
  if (routerObj && routerObj.titleApp) {
    document.title = routerObj.titleApp;
  } else {
    document.title = process.env.VUE_APP_SYS_NAME;
  }
};
</script>