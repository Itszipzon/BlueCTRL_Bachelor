import Vue from "vue";
import VueRouter from "vue-router";
import Main from "../views/Main.vue";
import Compare from "../views/Compare.vue";
import Boats from "../views/Boats.vue";

Vue.use(VueRouter);

const router = new VueRouter({
  mode: "history",
  base: import.meta.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "main",
      component: Main,
    },
    {
      path: "/compare",
      name: "compare",
      component: Compare,
    },
    {
      path: "/vessels",
      name: "boats",
      component: Boats,
    },
  ],
});

export default router;
