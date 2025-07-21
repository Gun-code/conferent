<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-4">회의실 목록</h1>
      <p class="text-gray-600">사용 가능한 회의실을 확인하고 예약하세요</p>
    </div>

    <!-- Filters -->
    <div class="bg-white rounded-lg shadow p-6 mb-8">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">최소 수용인원</label>
          <select class="w-full border border-gray-300 rounded-md px-3 py-2">
            <option value="">전체</option>
            <option value="2">2명 이상</option>
            <option value="5">5명 이상</option>
            <option value="10">10명 이상</option>
            <option value="20">20명 이상</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">위치</label>
          <select class="w-full border border-gray-300 rounded-md px-3 py-2">
            <option value="">전체</option>
            <option value="floor1">1층</option>
            <option value="floor2">2층</option>
            <option value="floor3">3층</option>
          </select>
        </div>
        <div class="flex items-end">
          <button class="w-full bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
            검색
          </button>
        </div>
      </div>
    </div>

    <!-- Room List -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <!-- 예시 회의실 카드 -->
      <div class="bg-white rounded-lg shadow hover:shadow-md transition-shadow">
        <div class="p-6">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-semibold text-gray-900">대회의실 A</h3>
            <span class="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full">사용 가능</span>
          </div>
          <div class="space-y-2 text-sm text-gray-600 mb-4">
            <div class="flex items-center">
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
              </svg>
              2층 동쪽
            </div>
            <div class="flex items-center">
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
              </svg>
              최대 12명
            </div>
          </div>
          <p class="text-gray-600 text-sm mb-4">프로젝터, 화이트보드, 화상회의 장비 구비</p>
          <router-link to="/reservations/create" class="block w-full bg-blue-600 text-white text-center py-2 rounded-md hover:bg-blue-700">
            예약하기
          </router-link>
        </div>
      </div>

      <!-- 더 많은 회의실 카드들... -->
      <div class="bg-white rounded-lg shadow hover:shadow-md transition-shadow">
        <div class="p-6">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-semibold text-gray-900">중회의실 B</h3>
            <span class="bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded-full">사용 중</span>
          </div>
          <div class="space-y-2 text-sm text-gray-600 mb-4">
            <div class="flex items-center">
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
              </svg>
              1층 서쪽
            </div>
            <div class="flex items-center">
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
              </svg>
              최대 6명
            </div>
          </div>
          <p class="text-gray-600 text-sm mb-4">화이트보드, 모니터 구비</p>
          <button disabled class="block w-full bg-gray-300 text-gray-500 text-center py-2 rounded-md cursor-not-allowed">
            사용 중
          </button>
        </div>
      </div>
    </div>

    <!-- 빈 상태 메시지 (조건부 렌더링) -->
    <div class="text-center py-12 hidden">
      <svg class="w-12 h-12 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
      </svg>
      <h3 class="text-lg font-medium text-gray-900 mb-2">회의실이 없습니다</h3>
      <p class="text-gray-600">검색 조건을 변경해보세요</p>
    </div>
  </div>
</template>

<script setup>
// 회의실 목록 로직
</script> 