<template>
  <button
    :class="buttonClasses"
    :disabled="disabled"
    :type="type"
    @click="handleClick"
  >
    <slot />
  </button>
</template>

<script>
export default {
  name: 'BaseButton',
  props: {
    variant: {
      type: String,
      default: 'primary',
      validator: (value) => ['primary', 'secondary', 'success', 'danger', 'warning', 'info'].includes(value)
    },
    size: {
      type: String,
      default: 'medium',
      validator: (value) => ['small', 'medium', 'large'].includes(value)
    },
    disabled: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: 'button',
      validator: (value) => ['button', 'submit', 'reset'].includes(value)
    },
    fullWidth: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    buttonClasses() {
      return [
        'base-button',
        `base-button--${this.variant}`,
        `base-button--${this.size}`,
        {
          'base-button--disabled': this.disabled,
          'base-button--full-width': this.fullWidth
        }
      ]
    }
  },
  methods: {
    handleClick(event) {
      if (!this.disabled) {
        this.$emit('click', event)
      }
    }
  }
}
</script>

<style scoped>
.base-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: 0.375rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  text-decoration: none;
  outline: none;
}

.base-button:focus {
  outline: 2px solid #3b82f6;
  outline-offset: 2px;
}

.base-button--disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.base-button--full-width {
  width: 100%;
}

/* Size variants */
.base-button--small {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
}

.base-button--medium {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
}

.base-button--large {
  padding: 1rem 2rem;
  font-size: 1.125rem;
}

/* Color variants */
.base-button--primary {
  background-color: #3b82f6;
  color: white;
}

.base-button--primary:hover:not(.base-button--disabled) {
  background-color: #2563eb;
}

.base-button--secondary {
  background-color: #6b7280;
  color: white;
}

.base-button--secondary:hover:not(.base-button--disabled) {
  background-color: #4b5563;
}

.base-button--success {
  background-color: #10b981;
  color: white;
}

.base-button--success:hover:not(.base-button--disabled) {
  background-color: #059669;
}

.base-button--danger {
  background-color: #ef4444;
  color: white;
}

.base-button--danger:hover:not(.base-button--disabled) {
  background-color: #dc2626;
}

.base-button--warning {
  background-color: #f59e0b;
  color: white;
}

.base-button--warning:hover:not(.base-button--disabled) {
  background-color: #d97706;
}

.base-button--info {
  background-color: #06b6d4;
  color: white;
}

.base-button--info:hover:not(.base-button--disabled) {
  background-color: #0891b2;
}
</style> 