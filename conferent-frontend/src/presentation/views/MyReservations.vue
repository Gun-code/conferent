<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-4">내 예약</h1>
      <p class="text-gray-600">예약 현황을 확인하고 관리하세요</p>
    </div>

    <!-- 탭 네비게이션 -->
    <div class="border-b border-gray-200 mb-8">
      <nav class="-mb-px flex space-x-8">
        <button class="border-blue-500 text-blue-600 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm">
          전체 예약
        </button>
        <button class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm">
          예정된 예약
        </button>
        <button class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm">
          완료된 예약
        </button>
      </nav>
    </div>

    <!-- 예약 목록 -->
    <div class="space-y-6">
      <!-- 예약 카드 1 -->
      <div class="bg-white rounded-lg shadow hover:shadow-md transition-shadow">
        <div class="p-6">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-4">
              <div>
                <h3 class="text-lg font-semibold text-gray-900">대회의실 A</h3>
                <p class="text-sm text-gray-600">2층 동쪽</p>
              </div>
              <span class="bg-green-100 text-green-800 text-xs px-2 py-1 rounded-full">확정됨</span>
            </div>
            <div class="text-right">
              <p class="text-sm font-medium text-gray-900">2024년 1월 15일</p>
              <p class="text-sm text-gray-600">09:00 - 11:00</p>
            </div>
          </div>
          
          <div class="mb-4">
            <p class="text-sm text-gray-700 mb-2"><strong>목적:</strong> 팀 미팅 및 프로젝트 논의</p>
            <p class="text-sm text-gray-700"><strong>참석자:</strong> 8명</p>
          </div>

          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-2 text-sm text-gray-600">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
              <span>1시간 30분 후 시작</span>
            </div>
            <div class="flex space-x-2">
              <button class="text-blue-600 hover:text-blue-800 text-sm font-medium">수정</button>
              <button class="text-red-600 hover:text-red-800 text-sm font-medium">취소</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 예약 카드 2 -->
      <div class="bg-white rounded-lg shadow hover:shadow-md transition-shadow">
        <div class="p-6">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-4">
              <div>
                <h3 class="text-lg font-semibold text-gray-900">중회의실 B</h3>
                <p class="text-sm text-gray-600">1층 서쪽</p>
              </div>
              <span class="bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded-full">대기중</span>
            </div>
            <div class="text-right">
              <p class="text-sm font-medium text-gray-900">2024년 1월 16일</p>
              <p class="text-sm text-gray-600">14:00 - 16:00</p>
            </div>
          </div>
          
          <div class="mb-4">
            <p class="text-sm text-gray-700 mb-2"><strong>목적:</strong> 고객 미팅</p>
            <p class="text-sm text-gray-700"><strong>참석자:</strong> 4명</p>
          </div>

          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-2 text-sm text-gray-600">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
              <span>승인 대기 중</span>
            </div>
            <div class="flex space-x-2">
              <button class="text-blue-600 hover:text-blue-800 text-sm font-medium">수정</button>
              <button class="text-red-600 hover:text-red-800 text-sm font-medium">취소</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 예약 카드 3 -->
      <div class="bg-white rounded-lg shadow hover:shadow-md transition-shadow opacity-60">
        <div class="p-6">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center space-x-4">
              <div>
                <h3 class="text-lg font-semibold text-gray-900">소회의실 C</h3>
                <p class="text-sm text-gray-600">3층 북쪽</p>
              </div>
              <span class="bg-gray-100 text-gray-800 text-xs px-2 py-1 rounded-full">완료됨</span>
            </div>
            <div class="text-right">
              <p class="text-sm font-medium text-gray-900">2024년 1월 10일</p>
              <p class="text-sm text-gray-600">10:00 - 12:00</p>
            </div>
          </div>
          
          <div class="mb-4">
            <p class="text-sm text-gray-700 mb-2"><strong>목적:</strong> 1:1 면담</p>
            <p class="text-sm text-gray-700"><strong>참석자:</strong> 2명</p>
          </div>

          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-2 text-sm text-gray-600">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"></path>
              </svg>
              <span>사용 완료</span>
            </div>
            <div class="flex space-x-2">
              <button class="text-blue-600 hover:text-blue-800 text-sm font-medium">다시 예약</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 빈 상태 메시지 (조건부 렌더링) -->
    <div class="text-center py-12 hidden">
      <svg class="w-12 h-12 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
      </svg>
      <h3 class="text-lg font-medium text-gray-900 mb-2">예약이 없습니다</h3>
      <p class="text-gray-600 mb-4">첫 번째 회의실을 예약해보세요</p>
      <router-link to="/reservations/create" class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
        지금 예약하기
      </router-link>
    </div>
  </div>
</template>

<script setup>
// 내 예약 로직
</script> 