<template>
  <Transition name="alert">
    <div v-if="show" :class="alertClasses" role="alert">
      <div class="flex items-center">
        <!-- 아이콘 -->
        <div class="flex-shrink-0">
          <svg v-if="type === 'error'" class="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
          </svg>
          <svg v-else-if="type === 'warning'" class="h-5 w-5 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
          </svg>
          <svg v-else-if="type === 'success'" class="h-5 w-5 text-green-400" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
          </svg>
          <svg v-else class="h-5 w-5 text-blue-400" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd" />
          </svg>
        </div>
        
        <!-- 메시지 -->
        <div class="ml-3">
          <p class="text-sm font-medium" :class="textClasses">
            {{ message }}
          </p>
        </div>
        
        <!-- 닫기 버튼 -->
        <div v-if="dismissible" class="ml-auto pl-3">
          <div class="-mx-1.5 -my-1.5">
            <button
              type="button"
              :class="buttonClasses"
              @click="handleClose"
              aria-label="닫기"
            >
              <span class="sr-only">닫기</span>
              <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script>
export default {
  name: 'BaseAlert',
  props: {
    show: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: 'info',
      validator: (value) => ['error', 'warning', 'success', 'info'].includes(value)
    },
    message: {
      type: String,
      required: true
    },
    dismissible: {
      type: Boolean,
      default: true
    },
    autoHide: {
      type: Boolean,
      default: false
    },
    autoHideDelay: {
      type: Number,
      default: 5000
    }
  },
  emits: ['close'],
  watch: {
    show(newVal) {
      if (newVal && this.autoHide) {
        this.startAutoHide()
      }
    }
  },
  methods: {
    handleClose() {
      this.$emit('close')
    },
    startAutoHide() {
      if (this.autoHideTimer) {
        clearTimeout(this.autoHideTimer)
      }
      this.autoHideTimer = setTimeout(() => {
        this.handleClose()
      }, this.autoHideDelay)
    }
  },
  beforeUnmount() {
    if (this.autoHideTimer) {
      clearTimeout(this.autoHideTimer)
    }
  },
  computed: {
    alertClasses() {
      const baseClasses = 'rounded-md p-4 border'
      
      switch (this.type) {
        case 'error':
          return `${baseClasses} bg-red-50 border-red-200`
        case 'warning':
          return `${baseClasses} bg-yellow-50 border-yellow-200`
        case 'success':
          return `${baseClasses} bg-green-50 border-green-200`
        default:
          return `${baseClasses} bg-blue-50 border-blue-200`
      }
    },
    textClasses() {
      switch (this.type) {
        case 'error':
          return 'text-red-800'
        case 'warning':
          return 'text-yellow-800'
        case 'success':
          return 'text-green-800'
        default:
          return 'text-blue-800'
      }
    },
    buttonClasses() {
      const baseClasses = 'inline-flex rounded-md p-1.5 focus:outline-none focus:ring-2 focus:ring-offset-2'
      
      switch (this.type) {
        case 'error':
          return `${baseClasses} text-red-400 hover:bg-red-100 focus:ring-red-500`
        case 'warning':
          return `${baseClasses} text-yellow-400 hover:bg-yellow-100 focus:ring-yellow-500`
        case 'success':
          return `${baseClasses} text-green-400 hover:bg-green-100 focus:ring-green-500`
        default:
          return `${baseClasses} text-blue-400 hover:bg-blue-100 focus:ring-blue-500`
      }
    }
  }
}
</script>

<style scoped>
.alert-enter-active,
.alert-leave-active {
  transition: all 0.3s ease;
}

.alert-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.alert-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style> 