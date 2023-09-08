<template>
  <div class="out-container">
    <div class="container" style="display: flex;">
      <div class="inner-container">

      </div>
      <div style="display: flex;justify-content: center;align-items: center;width: 500px;">
        <el-form ref="loginForm" :model="loginForm" :rules="rules"
                 label-width="0px">
          <h3 class="login_title">Resident profile management system</h3>
          <el-form-item prop="pin">
            <el-input style="width: 432px;" type="text" v-model="loginForm.account" placeholder="Pin" clearable></el-input>
          </el-form-item>
          <el-form-item style="width: 100%">
            <el-button type="primary" @click="submitClick" class="btn">Login</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
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
  background-image: url('../../public/img/back.png');
  background-repeat: no-repeat;
  background-size: cover;
  overflow: hidden;
  min-height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.inner-container {
  background-image: url('../../public/img/ocean.jpg');
  background-repeat: no-repeat;
  background-size: cover;
  width: 500px;
  height: 420px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login_title {
  font-weight: 300;
  margin-bottom: 1.25rem;
}

.container {
  background-color: #e9e9e9;
  border-radius: 0.7rem;
  box-shadow: 0 0.9rem 1.7rem rgba(0, 0, 0, 0.25),
  0 0.7rem 0.7rem rgba(0, 0, 0, 0.22);
  overflow: hidden;
  position: relative;
  width: 1000px;
  height: 420px;
}

.btn {
  background-color: #0367a6;
  background-image: linear-gradient(90deg,#0367a6 0%, #008997 74%);
  border-radius: 20px;
  border: 1px solid #0367a6;
  color: #e9e9e9;
  cursor: pointer;
  font-size: 0.8rem;
  font-weight: bold;
  letter-spacing: 0.1rem;
  padding: 0.9rem 4rem;
  text-transform: uppercase;
  transition: transform 80ms ease-in;
}

</style>
