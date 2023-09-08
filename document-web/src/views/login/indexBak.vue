<template>
  <div class="out-container">
    <el-form ref="loginForm" :model="loginForm" :rules="rules" class="login-container" label-position="left"
             label-width="0px" v-loading="loading">
      <h3 class="login_title">System Login</h3>
      <el-form-item prop="pin">
        <el-input type="text" v-model="loginForm.account" placeholder="Please enter a pin" clearable></el-input>
      </el-form-item>
      <!--      <el-form-item prop="password">-->
      <!--        <el-input type="password" v-model="loginForm.password" placeholder="Please enter the password" clearable></el-input>-->
      <!--      </el-form-item>-->
      <el-form-item style="width: 100%">
        <el-button type="primary" @click="submitClick" style="width: 100%">Login</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import {login} from "@/api/user";

export default {
  data() {
    return {
      rules: {
        account: [{required: true, message: 'Please enter a pin', trigger: 'blur'}],
        password: [{required: true, message: 'Please enter the password', trigger: 'blur'}]
      },
      loginForm: {
        account: '',
        password: ''
      },
      loading: false
    }
  },
  methods: {
    submitClick: function () {
      this.loading = true;
      this.loginForm.password = 123456;
      this.$refs['loginForm'].validate(async (valid) => {
        if (valid) {
          login(this.loginForm).then(response => {
            if (response.code == 200) {
              // 登录成功
              this.$message.success('Login succeeded!')
              localStorage.setItem('userInfo', JSON.stringify(response.data))
              localStorage.setItem('routers', JSON.stringify(this.$router.options.routes))
              this.$router.replace({path: '/home'})
            } else {
              // 登录失败
              this.$message.error('Login failed!')
            }
            this.loading = false
          });
        } else {
          this.loading = false
        }
      })
    }
  }
}
</script>
<style scoped>
.out-container {
  background-image: url('../../public/img/background.png');
  background-repeat: no-repeat;
  background-size: cover;
  overflow: hidden;
  min-height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background-color: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}

.login_remember {
  margin: 0px 0px 35px 0px;
  text-align: left;
}
</style>
