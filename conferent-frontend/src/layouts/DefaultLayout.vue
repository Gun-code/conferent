<template>
  <div class="layout">
    <Header />
    <main class="layout__main">
      <div class="layout__container">
        <slot />
      </div>
    </main>
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'

export default {
  name: 'DefaultLayout',
  components: {
    Header,
    Footer
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout__main {
  flex: 1;
  padding: 2rem 0;
}

.layout__container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

@media (max-width: 768px) {
  .layout__main {
    padding: 1rem 0;
  }
  
  .layout__container {
    padding: 0 0.5rem;
  }
}
</style> 