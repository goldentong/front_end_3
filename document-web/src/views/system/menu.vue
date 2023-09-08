<template>
  <div class="app-container">
    <div class="tb" style="height: 100%">
      <div class="main tb">
        <div class="main" style="margin-top: 20px;">
          <el-table :data="table.data" border row-key="id" :tree-props="{children: 'children'}"
                    :header-cell-style="{background:'#fafafa',color:'#000000'}" v-loading="tableLoading">
            <el-table-column show-overflow-tooltip fixed="left" label="Serial Number" type="index" width="150"
                             align="center"/>
            <el-table-column show-overflow-tooltip label="MenuName" width="180" align="center"
                             prop="name"></el-table-column>
            <el-table-column show-overflow-tooltip label="Code" align="center"
                             prop="code"></el-table-column>
            <el-table-column show-overflow-tooltip label="Sort" align="center"
                             prop="sort"></el-table-column>
            <el-table-column show-overflow-tooltip label="Remark" align="center"
                             prop="remark"></el-table-column>
            <el-table-column show-overflow-tooltip label="operate" width="160" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="openDialog(scope.row)">Edit</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <el-dialog :visible.sync="menuDialog.visible" :close-on-click-modal="false" width="30%" title="Edit menu">
      <el-form ref="roleForm" label-position="right" label-width="100px" :model="menuDialog.formData"
               :rules="rules">
        <el-form-item label="MenuName：" prop="name">
          <el-input placeholder="Please enter MenuName" v-model="menuDialog.formData.name"
                    clearable/>
        </el-form-item>
        <el-form-item label="Code：" prop="code">
          <el-input placeholder="Please enter Code" v-model="menuDialog.formData.code"
                    clearable disabled/>
        </el-form-item>
        <el-form-item label="Sort：" prop="sort">
          <el-input-number style="width: 100%;" :min="0" :step="1" v-model="menuDialog.formData.sort"/>
        </el-form-item>
        <el-form-item label="Remark:">
          <el-input type="textarea" :rows="3" placeholder="Please enter a comment" v-model="menuDialog.formData.remark" clearable/>
        </el-form-item>
      </el-form>
      <div class="dialog-btn">
        <el-button @click="menuDialog.visible = false">Cancel</el-button>
        <el-button type="primary" @click="updateMenu" :loading="loading">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>


import {getMenus, updateData} from "@/api/menu";

export default {
  data() {
    return {
      table: {
        data: []
      },
      menuDialog: {
        formData: {
          name: '',
          code: '',
          sort: '',
          remark: ''
        },
        visible: false
      },
      rules: {
        name: [
          {trigger: 'blur', required: true, message: 'Please enter MenuName'}
        ],
        code: [
          {trigger: 'blur', required: true, message: 'Please enter Code'}
        ],
        sort: [
          {trigger: 'blur', required: true, message: 'Please enter sort'}
        ]
      },
      loading: false,
      tableLoading: false
    }
  },
  mounted() {
    this.search()
  },
  methods: {
    // 搜索框查询
    search() {
      this.tableLoading = true
      getMenus().then(response => {
        if (response.code == 200) {
          // 查询成功
          this.table.data = response.data
        } else {
          // 查询失败
          this.$message.error('Query failed!')
        }
        this.tableLoading = false
      });
    },
    // 新增修改菜单
    updateMenu() {
      this.loading = true
      this.$refs['roleForm'].validate(async (valid) => {
        if (valid) {
          updateData(this.menuDialog.formData).then(response => {
            if (response.code == 200) {
              // 操作成功
              this.$message.success('Operation successful!')
              this.menuDialog.visible = false
              this.search()
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
    openDialog(data) {
      this.menuDialog.formData = JSON.parse(JSON.stringify(data))
      this.menuDialog.visible = true
      // 清空验证信息
      this.$nextTick(() => {
        this.$refs.roleForm.clearValidate()
      })
    }
  }
}
</script>

<style scoped>
</style>
