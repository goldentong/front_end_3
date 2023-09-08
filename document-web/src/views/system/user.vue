<template>
  <div class="app-container">
    <div class="tb" style="height: 100%">
      <div class="main tb">
        <div class="lr between">
          <div class="add-input">
            <el-input v-model="table.query.keyWord" placeholder="Please enter keywords for query" clearable
                      suffix-icon="el-icon-search" @change="search(true)"/>
          </div>
          <div style="display: flex;justify-content: flex-end;">
            <el-button type="primary" size="small" @click="openDialog(null, false)"
                       icon="el-icon-plus">New Users
            </el-button>
          </div>
        </div>
        <div class="main" style="margin-top: 20px;">
          <el-table :data="table.data" border
                    :header-cell-style="{background:'#fafafa',color:'#000000'}" v-loading="tableLoading">
            <el-table-column show-overflow-tooltip fixed="left" label="Serial Number" type="index" width="150"
                             align="center"/>
            <el-table-column show-overflow-tooltip label="Nickname" align="center"
                             prop="nickname"></el-table-column>
            <el-table-column show-overflow-tooltip label="Pin" align="center"
                             prop="account"></el-table-column>
            <el-table-column show-overflow-tooltip label="Phone" align="center"
                             prop="phone"></el-table-column>
            <el-table-column show-overflow-tooltip label="Email" width="200" align="center"
                             prop="email"></el-table-column>
            <el-table-column show-overflow-tooltip label="Role" width="300" align="center" prop="roleNames"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column show-overflow-tooltip label="Remark" width="300" align="center"
                             prop="remark"></el-table-column>
            <el-table-column show-overflow-tooltip label="operate" width="160" align="center">
              <template slot-scope="scope">
                <!-- 超级管理员账号不可操作 -->
                <el-button type="text" @click="openDialog(scope.row, true)" :disabled="scope.row.id == 1">Edit</el-button>
                <el-button type="text" style="color: #ff4949;" @click="deleteUser(scope.row.id)" v-if="scope.row.id != 1">Delete</el-button>
                <el-button type="text" disabled v-else>Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="lr end" style="margin-top: 20px;">
          <el-pagination :total="table.total" :page-size="table.query.size" :current-page="table.query.page"
                         layout="total, prev, pager, next" @current-change="currentPageChange"/>
        </div>
      </div>
    </div>
    <el-dialog :visible.sync="userDialog.visible" :close-on-click-modal="false" width="40%"
               :title="!isEdit ? 'New User' : 'Edit user'">
      <el-form ref="userForm" label-position="right" label-width="100px" :model="userDialog.formData"
               :rules="rules">
        <el-form-item label="Nickname：" prop="nickname">
          <el-input placeholder="Please enter a nickname" v-model="userDialog.formData.nickname"
                    clearable/>
        </el-form-item>
        <el-form-item label="Pin：" prop="Pin">
          <el-input placeholder="Please enter a Pin" v-model="userDialog.formData.account"
                    clearable/>
        </el-form-item>
<!--        <el-form-item label="Password：" prop="password">-->
<!--          <el-input placeholder="Please enter the password" v-model="userDialog.formData.password"-->
<!--                    clearable type="password"/>-->
<!--        </el-form-item>-->
        <el-form-item label="Phone：" prop="phone">
          <el-input placeholder="Please enter your phone number" v-model="userDialog.formData.phone"
                    clearable/>
        </el-form-item>
        <el-form-item label="Email：">
          <el-input placeholder="Please enter your email address" v-model="userDialog.formData.email"
                    clearable/>
        </el-form-item>
        <el-form-item label="Role：" prop="roleIds">
          <el-select @change="selectRoles" v-model="roleIds" placeholder="Please select a role" multiple clearable style="width: 100%;">
            <el-option v-for="item in roles" :key="item.id" :value="item.id"
                       :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Remark：">
          <el-input type="textarea" :rows="3" placeholder="Please enter a comment" v-model="userDialog.formData.remark" clearable/>
        </el-form-item>
      </el-form>
      <div class="dialog-btn">
        <el-button @click="userDialog.visible = false">Cancel</el-button>
        <el-button type="primary" @click="saveOrUpdateUser" :loading="loading">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>


import {getPageList, deleteData, saveOrUpdateData} from "@/api/user";
import {getAllRoles} from "@/api/role";

export default {
  data() {
    const validatePhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('Please enter your phone number'));
      } else {
        const reg = new RegExp("^1[3-9]\\d{9}$");
        if (!reg.test(value)) {
          callback(new Error('Please enter the correct phone number'));
        } else {
          callback();
        }
      }
    }
    return {
      table: {
        query: {
          page: 1,
          size: 10,
          keyWord: ''
        },
        total: 0,
        data: []
      },
      userDialog: {
        formData: {
          nickname: '',
          account: '',
          password: '',
          oldPassword: '',
          phone: '',
          email: '',
          roleIds: '',
          remark: ''
        },
        visible: false
      },
      rules: {
        nickname: [
          {trigger: 'blur', required: true, message: 'Please enter a nickname'}
        ],
        account: [
          {trigger: 'blur', required: true, message: 'Please enter an account'}
        ],
        password: [
          {trigger: 'blur', required: true, message: 'Please enter the password'}
        ],
        phone: [
          {trigger: 'blur', required: true, validator: validatePhone}
        ],
        roleIds: [
          {trigger: 'change', required: true, message: 'Please select a role'}
        ]
      },
      loading: false,
      tableLoading: false,
      isEdit: false,
      roleIds: [],
      roles: []
    }
  },
  mounted() {
    this.getRoles()
  },
  methods: {
    // 格式化角色显示
    formatRole(roleIds) {
      if (roleIds) {
        const ids = roleIds.split(',')
        const roles = this.roles.filter(role => ids.indexOf(role.id + '') != -1)
        if (roles && roles.length > 0) {
          return roles.map(role => role.name).join(',')
        }
      }
      return 'No roles assigned'
    },
    // 获取所有角色
    getRoles() {
      getAllRoles().then(response => {
        if (response.code == 200) {
          // 查询成功
          this.roles = response.data
          this.search(true)
        } else {
          // 查询失败
          this.$message.error('Failed to query roles!')
        }
      });
    },
    // 选择角色
    selectRoles() {
      this.userDialog.formData.roleIds = this.roleIds.join(',')
    },
    // 搜索框查询
    search(flag) {
      this.tableLoading = true
      if (flag) {
        this.table.query.page = 1
      }
      const params = {
        page: this.table.query.page,
        size: this.table.query.size,
        keyWord: this.table.query.keyWord
      }
      getPageList(params).then(response => {
        if (response.code == 200) {
          // 查询成功
          this.table.data = response.data
          this.table.total = response.count
          // 格式化角色显示
          if (this.table.data && this.table.data.length > 0) {
            for (let item of this.table.data) {
              item.roleNames = this.formatRole(item.roleIds)
            }
          }
        } else {
          // 查询失败
          this.$message.error('Query failed!')
        }
        this.tableLoading = false
      });
    },
    // 修改用户
    saveOrUpdateUser() {
      this.loading = true
      this.$refs['userForm'].validate(async (valid) => {
        if (valid) {
          this.userDialog.formData.password = 123456;
          saveOrUpdateData(this.userDialog.formData).then(response => {
            if (response.code == 200) {
              // 操作成功
              this.$message.success('Operation successful!')
              this.userDialog.visible = false
              this.search(false)
            } else {
              // 操作失败
              this.$message.error('Operation failed!')
            }
            this.loading = false
          });
        } else {
          this.loading = false
        }
      })
    },
    // 打开弹窗
    openDialog(data, isEdit) {
      this.isEdit = isEdit
      if (isEdit) {
        this.userDialog.formData = JSON.parse(JSON.stringify(data))
        this.roleIds = []
        // 角色回显
        if (this.userDialog.formData.roleIds) {
          const roleIds = this.userDialog.formData.roleIds.split(',')
          for (let i = 0; i < roleIds.length; i++) {
            this.roleIds.push(parseInt(roleIds[i]))
          }
        }
      } else {
        // 重置数据
        this.userDialog.formData = {
          nickname: '',
          account: '',
          password: '',
          oldPassword: '',
          phone: '',
          email: '',
          roleIds: '',
          remark: ''
        }
        this.roleIds = []
      }
      this.userDialog.visible = true
      // 清空验证信息
      this.$nextTick(() => {
        this.$refs.userForm.clearValidate()
      })
    },
    // 页码变更
    currentPageChange(page) {
      this.table.query.page = page
      this.search(false)
    },
    // 删除人员
    deleteUser(id) {
      this.$confirm('This operation will permanently delete user information. Do you want to continue?', 'prompt', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        deleteData(id).then(response => {
          if (response.code == 200) {
            // 操作成功
            this.$message.success('Operation successful!')
            this.search(false)
          } else {
            // 操作失败
            this.$message.error('Operation failed!')
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Cancelled deletion'
        })
      })
    }
  }
}
</script>

<style scoped>
</style>
