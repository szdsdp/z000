const { defineConfig } = require("@vue/cli-service");
const NodePolyfillPlugin = require("node-polyfill-webpack-plugin");
const path = require("path");

// 环境变量-系统代码
const SYS_CODE = process.env.VUE_APP_SYS_CODE;
// 环境变量-前端端口号
const WEB_PORT = process.env.VUE_APP_WEB_PORT || 80;
// 环境变量-接口代理
const BASE_API = process.env.VUE_APP_BASE_API;
const BASE_API_MICRO = process.env.VUE_APP_BASE_API + "/" + SYS_CODE.toLowerCase();
// 环境变量-后端地址
const JAVA_URL_CORE = process.env.VUE_APP_JAVA_URL_CORE;
const JAVA_URL_MICRO = process.env.VUE_APP_JAVA_URL_MICRO;

// Webpack 打包配置
module.exports = defineConfig({
  publicPath: "/app-" + SYS_CODE.toLowerCase(), //项目访问路径变成 /app-系统名/
  transpileDependencies: true, // 兼容旧浏览器
  productionSourceMap: false,  // 生产环境不生成调试文件

  // 开发模式配置
  devServer: {
    host: "0.0.0.0",    //监听所有IP地址，方便外部访问
    port: WEB_PORT,    // 指定端口号
    //接口代理配置
    proxy: {
      [BASE_API_MICRO]: {
        target: JAVA_URL_MICRO,
        changeOrigin: true,
        pathRewrite: { ["^" + process.env.VUE_APP_BASE_API]: "/" }
      },
      [BASE_API]: {
        target: JAVA_URL_CORE,
        changeOrigin: true,
        pathRewrite: { ["^" + process.env.VUE_APP_BASE_API]: "/" }
      }
    }
  },

  // 配置打包-直接修改配置对象
  configureWebpack: config => {
    config.plugins.push(new NodePolyfillPlugin());
    config.output.library = `${SYS_CODE}-[SYS_CODE]`;
    config.output.libraryTarget = "umd";
    config.output.chunkLoadingGlobal = `webpackJsonp_${SYS_CODE}`;
  },

  // 配置打包-链式API精细控制
  chainWebpack(config) {
    // 排除特定目录的 svg 规则
    config.module.rule("svg").exclude.add(resolve("node_modules/szd-client-web/client/assets/icons"));
    // 创建新的 icons 规则，使用 svg-sprite-loader
    config.module
      .rule("icons")
      .test(/\.svg$/)
      .include.add(resolve("node_modules/szd-client-web/client/assets/icons")).end()
      .use("svg-sprite-loader")
      .loader("svg-sprite-loader")
      .options({ symbolId: "icon-[name]" }).end();
    // 设置路径别名
    config.resolve.alias.set("~store", resolve("./src/store"));
    config.resolve.alias.set("~main", resolve("./src/main.js"));
    // Vue 编译开关
    config.plugin("define").tap(definitions => {
      Object.assign(definitions[0], {
        __VUE_OPTIONS_API__: "true",
        __VUE_PROD_DEVTOOLS__: "false",
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: "false"
      });
      return definitions;
    });
  }
});

/**
 * 解析路径为绝对路径
 * @param {string} dir - 需要解析的相对路径
 * @returns {string} 返回基于当前文件目录的绝对路径
 */
function resolve(dir) {
  return path.join(__dirname, dir);
}
