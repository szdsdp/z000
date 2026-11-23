import { createStore } from "vuex";
import storeModules from "szd-client-app/store/index.js";

export default createStore({
  modules: {
    ...storeModules
  }
});
