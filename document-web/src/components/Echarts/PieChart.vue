<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    },
    chartData: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.initChart()
    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'right',
          orient: 'vertical',
          bottom: '80',
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        calculable: true,
        series: [
          {
            type: 'pie',
            roseType: 'radius',
            radius: ['40%', '60%'],
            center: ['50%', '50%'],
            bottom: 0,
            data: [
              { value: this.chartData[0], name: '周一' },
              { value: this.chartData[1], name: '周二' },
              { value: this.chartData[2], name: '周三' },
              { value: this.chartData[3], name: '周四' },
              { value: this.chartData[4], name: '周五' },
              { value: this.chartData[5], name: '周六' },
              { value: this.chartData[6], name: '周日' }
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      }, true)
    }
  }
}
</script>
