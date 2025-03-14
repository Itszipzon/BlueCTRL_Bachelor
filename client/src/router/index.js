import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '../views/Main.vue'
import Details from '../views/DetailsPage.vue'

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  base: import.meta.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'main',
      component: Main
    },
    {
      path: '/details',
      name: 'details',
      component: Details

    },
  ]
})

export default router
