<template>
  <el-container class="home_container">
    <el-header>
      <div class="home_title">profile mangement system</div>
      <div class="home_userinfoContainer">
        <el-dropdown @command="logout">
          <span class="el-dropdown-link home_userinfo">
            <i class="el-icon-arrow-down el-icon--right home_userinfo"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout">Log out</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <el-menu
          default-active="0"
          class="el-menu-vertical-demo" style="background-color: rgb(250, 250, 250)" router>
          <template v-for="(item,index) in routers" v-if="!item.hidden">
            <el-submenu :index="index+''" v-if="item.children.length>0" :key="index">
              <template slot="title">
                <i :class="item.iconCls" style="margin-right: 5px;"></i>
                <span>{{ item.name }}</span>
              </template>
              <el-menu-item v-for="child in item.children" v-if="!child.hidden" :index="child.path" :key="child.path">
                {{ child.name }}
              </el-menu-item>
            </el-submenu>
            <template v-else>
              <el-menu-item :index="item.children[0].path">
                <i :class="item.children[0].iconCls"></i>
                <span slot="title">{{ item.children[0].name }}</span>
              </el-menu-item>
            </template>
          </template>
        </el-menu>
      </el-aside>
      <el-container>
        <el-main style="overflow-y: hidden;">
          <el-breadcrumb v-if="this.$router.currentRoute.name == 'home'">
            <el-breadcrumb-item disabled>Home</el-breadcrumb-item>
          </el-breadcrumb>
          <el-breadcrumb separator-class="el-icon-arrow-right" v-else>
            <el-breadcrumb-item :to="{ path: '/home' }">Home</el-breadcrumb-item>
            <el-breadcrumb-item v-text="this.$router.currentRoute.name"></el-breadcrumb-item>
          </el-breadcrumb>
          <keep-alive>
            <router-view v-if="this.$route.meta.keepAlive"></router-view>
          </keep-alive>
          <router-view v-if="!this.$route.meta.keepAlive"></router-view>
          <div class="index-container" v-if="this.$route.name == 'Home'">
            <div class="index-background"/>
          </div>
          <div class="index-text" v-if="this.$route.name == 'Home'">
            profile mangement system
          </div>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>
<script>


import {getPermissionMenus} from "@/api/menu";

export default {
  data() {
    return {
      nickname: '',
      routers: [],
      userInfo: {}
    }
  },
  mounted: function () {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!this.userInfo) {
      this.$router.push('/')
    } else {
      this.nickname = this.userInfo.nickname
      this.initMenu()
    }
  },
  methods: {
    // 初始化菜单
    initMenu() {
      let routers = JSON.parse(localStorage.getItem('routers'))
      const params = {
        roleIds: this.userInfo.roleIds
      }
      getPermissionMenus(params).then(response => {
        if (response.code == 200) {
          // 查询成功
          const menus = response.data
          if (menus && menus.length > 0) {
            const startRoutes = routers.slice(0, 2);
            let endRoutes = routers.slice(2, routers.length + 1)
            const codes = menus.map(item => item.code)
            endRoutes = endRoutes.filter(item => codes.indexOf(item.code) != -1)
            // 修改名称和排序值
            for (const endRoute of endRoutes) {
              const menu = menus.filter(item => item.code == endRoute.code)[0]
              endRoute.name = menu.name
              endRoute.sort = menu.sort
            }
            endRoutes.sort(function (a, b) {
              return a.sort - b.sort
            });
            for (let endRoute of endRoutes) {
              endRoute.children = endRoute.children.filter(item => codes.indexOf(item.code) != -1)
              for (const child of endRoute.children) {
                const menu = menus.filter(item => item.code == child.code)[0]
                child.name = menu.name
                child.sort = menu.sort
              }
              endRoute.children.sort(function (a, b) {
                return a.sort - b.sort
              });
            }
            this.routers = startRoutes.concat(endRoutes)
          } else {
            this.routers = []
          }
        } else {
          this.routers = []
        }
      })
    },
    // 退出登录
    logout() {
      this.$confirm('Exit login, do you want to continue?', 'prompt', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        this.$message.success('Successfully logged out and logged in!')
        localStorage.removeItem('userInfo')
        localStorage.removeItem('routers')
        this.$router.push('/')
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Canceled exit!'
        })
      })
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss">
.home_container {
  height: 100%;
  position: absolute;
  top: 0px;
  left: 0px;
  width: 100%;
}

.el-header {
  background-color: #004ce5;
  color: #333;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-aside {
  background-color: rgb(250, 250, 250);
}

.el-menu {
  height: 100%;
}

.el-main {
  background-color: #fff;
  color: #000;
  text-align: center;
}

.home_title {
  color: #fff;
  font-size: 22px;
  display: inline;
}

.home_userinfo {
  color: #fff;
  cursor: pointer;
}

.home_userinfoContainer {
  display: inline;
  margin-right: 20px;
}

.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
}

.index-background {
  background-image: url('../../public/img/index.png');
  background-repeat: no-repeat;
  background-size: cover;
  overflow: hidden;
  height: 400px;
  width: 300px;
}

.index-text {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bolder;
}

.el-submenu__title {
  padding-left: 0 !important;
}

.index-container {
  width: 100%;
  min-height: 70%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
