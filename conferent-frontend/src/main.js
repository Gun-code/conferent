import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './presentation/router'
import './style.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)

// 임시 사용자 ID 설정 (실제로는 인증 시스템에서 처리)
localStorage.setItem('userId', '1')

app.mount('#app') 