<template>
  <div class="app-container">
    <div class="tb" style="height: 100%">
      <div class="main tb">
        <div class="lr between">
          <div style="display: flex;justify-content: space-between;">
            <el-input style="width: 217px;" v-model="table.query.keyWord" placeholder="Please enter keywords for query" clearable
                      suffix-icon="el-icon-search" @change="search(true)"/>
          </div>
          <div style="display: flex;justify-content: flex-end;">
            <el-button type="primary" size="small" @click="openDialog(null, false, false)"
                       icon="el-icon-plus">Add
            </el-button>
          </div>
        </div>
        <div class="main" style="margin-top: 20px;">
          <el-table :data="table.data" height="650px" border
                    :header-cell-style="{background:'#fafafa',color:'#000000'}" v-loading="tableLoading">
            <template slot="empty">
              <p>no data</p>
            </template>
            <el-table-column show-overflow-tooltip fixed="left" label="Serial Number" type="index" width="150"
                             align="center"/>
            <el-table-column show-overflow-tooltip label="name" align="center"
                             prop="name"></el-table-column>
            <el-table-column show-overflow-tooltip label="age" align="center"
                             prop="backInfo"></el-table-column>
            <el-table-column show-overflow-tooltip label="gender" align="center"
                             prop="backInfo"></el-table-column>
            <el-table-column show-overflow-tooltip label="roomNumber" align="center"
                             prop="backInfo"></el-table-column>
            <el-table-column show-overflow-tooltip label="operate" width="260" align="center">
              <template slot-scope="scope">
                <el-button type="text" @click="openDialog(scope.row, true, false)" style="font-size: 18px;">Edit</el-button>
                <el-button type="text" @click="openDialog(scope.row, false, true)" style="font-size: 18px;">Info</el-button>
                <el-button type="text" style="color: #ff4949;font-size: 18px;" @click="deleteData(scope.row.id)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="lr end" style="margin-top: 20px;">
          <el-pagination :total="table.total" :page-size="table.query.size" :current-page="table.query.page"
                         layout="prev, pager, next" @current-change="currentPageChange"/>
        </div>
      </div>
    </div>
    <el-dialog :visible.sync="documentDialog.visible" :close-on-click-modal="false" width="40%"
               :title="!isEdit && !isInfo ? 'add resident file' :  !isInfo ? 'Editing personnel profiles' : 'Personnel profiles info'">
      <el-form ref="documentForm" label-position="right" label-width="200px" :model="documentDialog.formData"
               :rules="rules" style="height: 480px;overflow-y: auto;padding: 0 20px;" :disabled="isInfo">
        <el-form-item label="ResidentName：" prop="name">
          <el-input placeholder="Please enter ResidentName" v-model="documentDialog.formData.name" clearable/>
        </el-form-item>
        <el-form-item label="age：" prop="age">
          <el-input-number :min="0" v-model="documentDialog.formData.age" clearable style="width: 100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="gender：" prop="gender">
          <el-select filterable  v-model="documentDialog.formData.gender" placeholder="Please select Gender" clearable style="width: 100%;">
            <el-option v-for="item in gender" :key="item.id" :value="item.id"
                       :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="roomNumber：" prop="roomNumber">
          <el-input placeholder="Please enter roomNumber" v-model="documentDialog.formData.roomNumber" clearable/>
        </el-form-item>
        <el-form-item label="Resident photos：" prop="pictureUrl">
          <el-upload
            action="http://124.221.211.24:8080/group1/upload?output=json"
            list-type="picture-card"
            :limit="3"
            :file-list="imageList"
            :on-success="handleImageSuccess"
            :on-remove="handleImageRemove"
            accept="image/gif,image/jpeg,image/png,.gif,.jpeg,.jpg,.png">
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="BackgroundInformation：" prop="backInfo">
          <el-input type="textarea" :rows="3" placeholder="Please enter BackgroundInformation" v-model="documentDialog.formData.backInfo"></el-input>
        </el-form-item>
        <el-form-item label="Language：" prop="languageIds">
          <el-select filterable  v-model="documentDialog.formData.languageIds" placeholder="Please select Language" clearable style="width: 100%;" multiple>
            <el-option v-for="item in languages" :key="item.id" :value="item.id"
                       :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Favorite：" prop="favoriteIds">
          <el-select filterable  v-model="documentDialog.formData.favoriteIds" placeholder="Please select Favorite" clearable style="width: 100%;" multiple>
            <el-option v-for="item in favorites" :key="item.id" :value="item.id"
                       :label="item.name"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="dialog-btn">
        <el-button @click="documentDialog.visible = false">Cancel</el-button>
        <el-button type="primary" @click="saveOrUpdateData" :loading="loading">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>

import {getPageList, deleteData, saveOrUpdateData} from "@/api/document";

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
      documentDialog: {
        formData: {
          name: '',
          age: '',
          gender: '',
          roomNumber: '',
          pictureUrl: '',
          backInfo: '',
          languageIds: '',
          languageNames: '',
          favoriteIds: '',
          favoriteNames: ''
        },
        visible: false
      },
      rules: {
        name: [
          {trigger: 'change', required: true, message: 'Please enter ResidentName'}
        ],
        age: [
          {trigger: 'change', required: true, message: 'Please enter Age'}
        ],
        gender: [
          {trigger: 'change', required: true, message: 'Please enter Gender'}
        ],
        roomNumber: [
          {trigger: 'change', required: true, message: 'Please enter RoomNumber'}
        ],
        pictureUrl: [
          {trigger: 'change', required: true, message: 'Please upload resident photos'}
        ],
        backInfo: [
          {trigger: 'blur', required: true, message: 'Please enter BackgroundInformation'}
        ],
        languageIds: [
          {trigger: 'blur', required: true, message: 'Please select Language'}
        ],
        favoriteIds: [
          {trigger: 'blur', required: true, message: 'Please select Favorite'}
        ]
      },
      loading: false,
      tableLoading: false,
      isEdit: false,
      isInfo: false,
      imageList: [],
      languages: [
        {
          id: 1,
          name: 'English'
        },
        {
          id: 2,
          name: 'Chinese'
        },
        {
          id: 3,
          name: 'Japanese'
        },
        {
          id: 4,
          name: 'Korean'
        }
      ],
      favorites: [
        {
          id: 1,
          name: 'Music'
        },
        {
          id: 2,
          name: 'Movie'
        },
        {
          id: 3,
          name: 'Game'
        },
        {
          id: 4,
          name: 'Food'
        }
      ],
      gender: [
        {
          id: 1,
          name: 'male'
        },
        {
          id: 2,
          name: 'female'
        }
      ]
    }
  },
  mounted() {
    this.search(true)
  },
  methods: {
    // 搜索框查询
    search(flag) {
      this.tableLoading = true
      if (flag) {
        this.table.query.page = 1
        this.table.query.keyWord = ''
      }
      const params = {
        current: this.table.query.page,
        size: this.table.query.size,
        keyWord: this.table.query.keyWord
      }
      getPageList(params).then(response => {
        if (response.code == 200) {
          // 查询成功
          this.table.data = response.data
          this.table.total = response.count
        } else {
          // 查询失败
          this.$message.error('Query failed!')
        }
        this.tableLoading = false
      })
    },
    // 修改人员档案
    saveOrUpdateData() {
      this.loading = true
      this.$refs['documentForm'].validate(async (valid) => {
        if (valid) {
          this.documentDialog.formData.languageNames = this.languages.filter(item => this.documentDialog.formData.languageIds.indexOf(item.id)).map(item => item.name).join(',')
          this.documentDialog.formData.languageIds = this.documentDialog.formData.languageIds.join(',')
          this.documentDialog.formData.favoriteNames = this.favorites.filter(item => this.documentDialog.formData.favoriteIds.indexOf(item.id)).map(item => item.name).join(',')
          this.documentDialog.formData.favoriteIds = this.documentDialog.formData.favoriteIds.join(',')
          saveOrUpdateData(this.documentDialog.formData).then(response => {
            if (response.code == 200) {
              // 操作成功
              this.$message.success('Operation successful!')
              this.documentDialog.visible = false
              this.search(false)
            } else {
              // 操作失败
              this.$message.error('Operation failed!')
            }
            this.loading = false
          })
        } else {
          this.loading = false
        }
      })
    },
    // 打开弹窗
    openDialog(data, isEdit, isInfo) {
      const rowData = JSON.parse(JSON.stringify(data))
      this.isEdit = isEdit
      this.isInfo = isInfo
      this.imageList = []
      if (!isEdit && !isInfo) {
        // 重置数据
        this.documentDialog.formData = {
          name: '',
          age: '',
          gender: '',
          roomNumber: '',
          pictureUrl: '',
          backInfo: '',
          languageIds: '',
          languageNames: '',
          favoriteIds: '',
          favoriteNames: ''
        }
      } else {
        if (rowData.pictureUrl) {
          const imageUrls = rowData.pictureUrl.split(',')
          for (let i = 0; i < imageUrls.length; i++) {
            this.imageList.push({
              uid: i,
              url: imageUrls[i]
            })
          }
        }
        const languageIds = rowData.languageIds.split(',')
        for (const languageId of languageIds) {
          rowData.languageIds = [];
          rowData.languageIds.push(parseInt(languageId))
        }
        const favoriteIds = rowData.favoriteIds.split(',')
        for (const favoriteId of favoriteIds) {
          rowData.favoriteIds = [];
          rowData.favoriteIds.push(parseInt(favoriteId))
        }
        this.documentDialog.formData = JSON.parse(JSON.stringify(rowData))
      }
      this.documentDialog.visible = true
      // 清空验证信息
      this.$nextTick(() => {
        this.$refs.documentForm.clearValidate()
      })
    },
    // 页码变更
    currentPageChange(page) {
      this.table.query.page = page
      this.search(false)
    },
    // 删除人员档案
    deleteData(id) {
      this.$confirm('This operation will permanently delete personnel profile information. Do you want to continue?', 'prompt', {
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
            this.$message.error(response.data)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Cancelled deletion'
        })
      })
    },
    handleImageSuccess(response, file) {
      this.imageList.push({
        uid: file.uid,
        url: response.url
      })
      this.documentDialog.formData.pictureUrl = this.imageList.map(item => item.url).join(',')
    },
    handleImageRemove(file) {
      this.imageList = this.imageList.filter(item => item.uid != file.uid)
      if (this.imageList && this.imageList.length > 0) {
        this.documentDialog.formData.pictureUrl = this.imageList.map(item => item.url).join(',')
      } else {
        this.documentDialog.formData.pictureUrl = ''
      }
    }
  }
}
</script>

<style scoped>
</style>
