import Vue from "vue";
import VueRouter from "vue-router";
import Main from "../views/Main.vue";
import Details from "../views/DetailsPage.vue";
import Compare from "../views/Compare.vue";

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
      path: "/details",
      name: "details",
      component: Details,
    },
    {
      path: "/compare",
      name: "compare",
      component: Compare,
    }
  ],
});

export default router;
