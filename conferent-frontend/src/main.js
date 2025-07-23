import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

// CSS 스타일 import
import './styles/index.css'

// 디렉티브 import
import clickOutside from './directives/clickOutside'

const app = createApp(App)

// Pinia 스토어 설정
const pinia = createPinia()
app.use(pinia)

// 라우터 설정
app.use(router)

// 전역 디렉티브 등록
app.directive('click-outside', clickOutside)

// 전역 속성 설정
app.config.globalProperties.$formatDate = (date, format = 'datetime') => {
  // 날짜 포맷팅 유틸리티
  const d = new Date(date)
  if (format === 'date') {
    return d.toLocaleDateString('ko-KR')
  } else if (format === 'time') {
    return d.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })
  } else {
    return d.toLocaleString('ko-KR')
  }
}

app.mount('#app') 