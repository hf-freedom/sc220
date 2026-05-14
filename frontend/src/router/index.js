import Vue from 'vue'
import Router from 'vue-router'
import ActivityList from '@/components/ActivityList'
import CreateActivity from '@/components/CreateActivity'
import ActivityDetail from '@/components/ActivityDetail'
import OrderList from '@/components/OrderList'
import Blacklist from '@/components/Blacklist'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'ActivityList',
      component: ActivityList
    },
    {
      path: '/create',
      name: 'CreateActivity',
      component: CreateActivity
    },
    {
      path: '/activity/:id',
      name: 'ActivityDetail',
      component: ActivityDetail
    },
    {
      path: '/orders',
      name: 'OrderList',
      component: OrderList
    },
    {
      path: '/blacklist',
      name: 'Blacklist',
      component: Blacklist
    }
  ]
})
