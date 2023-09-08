import Vue from 'vue'
import Router from 'vue-router'
import login from '@/views/login/index'
import home from '@/views/home/index'
import document from '@/views/document/document'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      hidden: true,
      component: login
    }, {
      path: '/home',
      name: 'Home',
      component: home,
      hidden: true
    },
    {
      path: '/back',
      component: home,
      name: 'manage your files',
      code: 'back',
      iconCls: 'fa fa-file',
      children: [
        {
          path: '/document',
          name: 'Personnel files',
          code: 'back_document',
          component: document
        }
      ]
    }
  ]
})
