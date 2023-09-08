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
                       icon="el-icon-plus">New Role
            </el-button>
          </div>
        </div>
        <div class="main" style="margin-top: 20px;">
          <el-table :data="table.data" border
                    :header-cell-style="{background:'#fafafa',color:'#000000'}" v-loading="tableLoading">
            <el-table-column show-overflow-tooltip fixed="left" label="Serial Number" type="index" width="150"
                             align="center"/>
            <el-table-column show-overflow-tooltip label="RoleName" width="260" align="center"
                             prop="name"></el-table-column>
            <el-table-column show-overflow-tooltip label="Menu" width="400" align="center" prop="menuNames"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column show-overflow-tooltip label="Type" width="400" align="center" prop="menuNames"
                             :show-overflow-tooltip="true">
              <template slot-scope="scope">
                <span v-if="scope.row.id == 1 || scope.row.id == 2">System Presets</span>
                <span v-else>User defined</span>
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip label="Remark" align="center"
                             prop="remark"></el-table-column>
            <el-table-column show-overflow-tooltip label="operate" width="300" align="center">
              <template slot-scope="scope">
                <!-- 超级管理员及管理员角色不可操作 -->
                <el-button type="text" @click="openDialog(scope.row, true)" :disabled="scope.row.id == 1 || scope.row.id == 2">Edit</el-button>
                <el-button type="text" @click="distributeMenu(scope.row, true)" :disabled="scope.row.id == 1 || scope.row.id == 2">Assignments Menu</el-button>
                <el-button type="text" style="color: #ff4949;" @click="deleteRole(scope.row.id)" v-if="scope.row.id != 1 && scope.row.id != 2">Delete</el-button>
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
    <el-dialog :visible.sync="roleDialog.visible" :close-on-click-modal="false" width="40%"
               :title="!isEdit ? 'New Role' : 'Edit Role'">
      <el-form ref="roleForm" label-position="right" label-width="100px" :model="roleDialog.formData"
               :rules="rules">
        <el-form-item label="RoleName：" prop="name">
          <el-input placeholder="Please enter a role" v-model="roleDialog.formData.name"
                    clearable/>
        </el-form-item>
        <el-form-item label="Remark:">
          <el-input type="textarea" :rows="3" placeholder="Please enter a comment" v-model="roleDialog.formData.remark" clearable/>
        </el-form-item>
      </el-form>
      <div class="dialog-btn">
        <el-button @click="roleDialog.visible = false">Cancel</el-button>
        <el-button type="primary" @click="saveOrUpdateRole" :loading="loading">Save</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="menuDialog.visible" :close-on-click-modal="false" width="15%" title="Assignments Menu">
      <div style="display: flex;justify-content: flex-start">
        <el-tree
          ref="menuTree"
          :data="menuDialog.data"
          show-checkbox
          node-key="id"
          default-expand-all
          :props="menuDialog.defaultProps">
        </el-tree>
      </div>
      <div class="dialog-btn">
        <el-button @click="menuDialog.visible = false">Cancel</el-button>
        <el-button type="primary" @click="updateRoleMenu" :loading="menuDialog.loading">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>


import {getPageList, saveOrUpdateData, deleteData} from "@/api/role";
import {getAllMenus, getMenus} from "@/api/menu";

export default {
  data() {
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
      roleDialog: {
        formData: {
          name: '',
          menuIds: '',
          remark: ''
        },
        visible: false
      },
      rules: {
        name: [
          {trigger: 'blur', required: true, message: 'Please enter a role'}
        ]
      },
      loading: false,
      tableLoading: false,
      isEdit: false,
      roles: [],
      menuDialog: {
        data: [],
        visible: false,
        loading: false,
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      menus: []
    }
  },
  mounted() {
    this.getMenus()
    this.getMenuTree()
  },
  methods: {
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
          // 格式化Menu显示
          if (this.table.data && this.table.data.length > 0) {
            for (let item of this.table.data) {
              item.menuNames = this.formatMenu(item.menuIds)
            }
          }
        } else {
          // 查询失败
          this.$message.error('Query failed!')
        }
        this.tableLoading = false
      });
    },
    // 新增修改角色
    saveOrUpdateRole() {
      this.loading = true
      this.$refs['roleForm'].validate(async (valid) => {
        if (valid) {
          saveOrUpdateData(this.roleDialog.formData).then(response => {
            if (response.code == 200) {
              // 操作成功
              this.$message.success('Operation successful!')
              this.roleDialog.visible = false
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
        this.roleDialog.formData = JSON.parse(JSON.stringify(data))
      } else {
        // 重置数据
        this.roleDialog.formData = {
          name: '',
          remark: ''
        }
      }
      this.roleDialog.visible = true
      // 清空验证信息
      this.$nextTick(() => {
        this.$refs.roleForm.clearValidate()
      })
    },
    // 页码变更
    currentPageChange(page) {
      this.table.query.page = page
      this.search(false)
    },
    // 删除角色
    deleteRole(id) {
      this.$confirm('This operation will permanently delete the role information. Do you want to continue?', 'prompt', {
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
    },
    // 获取Menu树
    getMenuTree() {
      getMenus().then(response => {
        if (response.code == 200) {
          // 查询成功
          const menuData = response.data
          for (let i = 1; i < menuData.length; i++) {
            this.menuDialog.data.push(menuData[i])
          }
        } else {
          // 查询失败
          this.$message.error('Failed to obtain Menu tree!')
        }
      })
    },
    // 分配Menu
    distributeMenu(data) {
      this.roleDialog.formData = JSON.parse(JSON.stringify(data))
      // 回显数据
      let checkedKeys = []
      const menuIds = this.roleDialog.formData.menuIds
      if (menuIds) {
        const menus = menuIds.split(",");
        for (const menu of menus) {
          if (menu != 1 && menu != 5) {
            checkedKeys.push(parseInt(menu))
          }
        }
      }
      this.menuDialog.visible = true
      // 回显数据
      this.$nextTick(() => {
        this.$refs.menuTree.setCheckedKeys(checkedKeys)
      })
    },
    // 更新Menu权限
    updateRoleMenu() {
      let checkedKeys = this.$refs.menuTree.getCheckedKeys().concat(this.$refs.menuTree.getHalfCheckedKeys())
      if (!checkedKeys || checkedKeys.length == 0) {
        this.$message.error('Please allocate Menu')
        return
      }
      this.menuDialog.loading = true
      this.roleDialog.formData.menuIds = checkedKeys.join(',')
      saveOrUpdateData(this.roleDialog.formData).then(response => {
        if (response.code == 200) {
          // 操作成功
          this.$message.success('Operation successful!')
          this.menuDialog.visible = false
          this.search(false)
        } else {
          // 操作失败
          this.$message.error('Operation failed!')
        }
        this.menuDialog.loading = false
      })
    },
    // 格式化Menu显示
    formatMenu(menuIds) {
      if (menuIds) {
        const ids = menuIds.split(',')
        const menus = this.menus.filter(role => ids.indexOf(role.id + '') != -1)
        if (menus && menus.length > 0) {
          return menus.map(menu => menu.name).join(',')
        }
      }
      return 'Unassigned Menu'
    },
    // 获取所有Menu
    getMenus() {
      getAllMenus().then(response => {
        if (response.code == 200) {
          // 查询成功
          this.menus = response.data
          this.search(true)
        } else {
          // 查询失败
          this.$message.error('Failed to query Menu!')
        }
      });
    },
  }
}
</script>

<style scoped>
</style>
