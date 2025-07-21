<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <!-- Navigation Header -->
    <nav class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex">
            <div class="flex-shrink-0 flex items-center">
              <router-link to="/" class="text-xl font-bold text-blue-600">
                Conferent
              </router-link>
            </div>
            <div class="hidden sm:ml-6 sm:flex sm:space-x-8">
              <router-link 
                to="/" 
                class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm"
                :class="{ 'border-blue-500 text-gray-900': $route.name === 'home' }"
              >
                홈
              </router-link>
              <router-link 
                to="/rooms" 
                class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm"
                :class="{ 'border-blue-500 text-gray-900': $route.name === 'rooms' }"
              >
                회의실 목록
              </router-link>
              <router-link 
                to="/reservations/create" 
                class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm"
                :class="{ 'border-blue-500 text-gray-900': $route.name === 'create-reservation' }"
              >
                예약하기
              </router-link>
              <router-link 
                to="/my-reservations" 
                class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm"
                :class="{ 'border-blue-500 text-gray-900': $route.name === 'my-reservations' }"
              >
                내 예약
              </router-link>
              <router-link 
                to="/admin" 
                class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm"
                :class="{ 'border-blue-500 text-gray-900': $route.name === 'admin' }"
              >
                관리자
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup>
// App component logic (no imports needed for now)
</script> 