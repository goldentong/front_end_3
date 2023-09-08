import axios from 'axios'
import { Message, MessageBox } from 'element-ui'

// create axios instance
const service = axios.create({
  baseURL: process.env.BASE_API, // apibase_url
  timeout: 35000
})

// responeblock
service.interceptors.response.use(
  response => {
  /**
  * code为非0
  */
    const res = response.data
    if (res.code != 200) {
      console.log(res);
      Message({
        message: res.msg||res.message,
        type: 'error',
        duration: 3 * 1000
      })
      return Promise.reject('error')
    } else {
      return response.data
    }
  },
  error => {
    console.log(error);
    Message({
      message: error.message,
      type: 'error',
      duration: 3 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
