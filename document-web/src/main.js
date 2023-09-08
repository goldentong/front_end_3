import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss'
import 'font-awesome/css/font-awesome.min.css'

Vue.use(ElementUI)
Vue.config.productionTip = false
window.bus = new Vue();
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {App}
})

//login block
router.beforeEach((to,from,next)=>{
  if (to.path != '/') {
    const userInfo = localStorage.getItem('userInfo')
    if (userInfo) {
      next();
    } else {
      next({//return to login
        path: '/',
        query: {redirect: to.fullPath}
      })
    }
  } else {
    next();
  }
})
