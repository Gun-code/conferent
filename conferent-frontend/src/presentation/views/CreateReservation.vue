<template>
  <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-4">회의실 예약</h1>
      <p class="text-gray-600">원하는 시간에 회의실을 예약하세요</p>
    </div>

    <form class="bg-white rounded-lg shadow p-6 space-y-6">
      <!-- 회의실 선택 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">회의실</label>
        <select class="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500">
          <option value="">회의실을 선택하세요</option>
          <option value="1">대회의실 A (최대 12명)</option>
          <option value="2">중회의실 B (최대 6명)</option>
          <option value="3">소회의실 C (최대 4명)</option>
        </select>
      </div>

      <!-- 예약 날짜 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">예약 날짜</label>
        <input 
          type="date" 
          class="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <!-- 시간 선택 -->
      <div class="grid grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">시작 시간</label>
          <select class="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500">
            <option value="">시간 선택</option>
            <option value="09:00">09:00</option>
            <option value="09:30">09:30</option>
            <option value="10:00">10:00</option>
            <option value="10:30">10:30</option>
            <option value="11:00">11:00</option>
            <option value="11:30">11:30</option>
            <option value="14:00">14:00</option>
            <option value="14:30">14:30</option>
            <option value="15:00">15:00</option>
            <option value="15:30">15:30</option>
            <option value="16:00">16:00</option>
            <option value="16:30">16:30</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">종료 시간</label>
          <select class="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500">
            <option value="">시간 선택</option>
            <option value="10:00">10:00</option>
            <option value="10:30">10:30</option>
            <option value="11:00">11:00</option>
            <option value="11:30">11:30</option>
            <option value="12:00">12:00</option>
            <option value="15:00">15:00</option>
            <option value="15:30">15:30</option>
            <option value="16:00">16:00</option>
            <option value="16:30">16:30</option>
            <option value="17:00">17:00</option>
            <option value="17:30">17:30</option>
            <option value="18:00">18:00</option>
          </select>
        </div>
      </div>

      <!-- 예약 목적 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">예약 목적</label>
        <textarea 
          rows="3"
          placeholder="회의 목적을 입력하세요"
          class="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
        ></textarea>
      </div>

      <!-- 참석자 수 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">예상 참석자 수</label>
        <input 
          type="number" 
          min="1"
          max="20"
          placeholder="참석자 수를 입력하세요"
          class="w-full border border-gray-300 rounded-md px-3 py-2 focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <!-- 추가 요청사항 -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">추가 요청사항</label>
        <div class="space-y-2">
          <label class="flex items-center">
            <input type="checkbox" class="mr-2" />
            <span class="text-sm text-gray-700">프로젝터 사용</span>
          </label>
          <label class="flex items-center">
            <input type="checkbox" class="mr-2" />
            <span class="text-sm text-gray-700">화상회의 장비 사용</span>
          </label>
          <label class="flex items-center">
            <input type="checkbox" class="mr-2" />
            <span class="text-sm text-gray-700">다과 준비</span>
          </label>
        </div>
      </div>

      <!-- 제출 버튼 -->
      <div class="flex space-x-4">
        <button 
          type="submit"
          class="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        >
          예약 신청
        </button>
        <router-link 
          to="/rooms"
          class="flex-1 bg-gray-200 text-gray-800 py-2 px-4 rounded-md text-center hover:bg-gray-300"
        >
          취소
        </router-link>
      </div>
    </form>

    <!-- 예약 안내 -->
    <div class="mt-8 bg-blue-50 border border-blue-200 rounded-lg p-4">
      <h3 class="text-sm font-medium text-blue-800 mb-2">예약 시 주의사항</h3>
      <ul class="text-sm text-blue-700 space-y-1">
        <li>• 예약은 최소 30분 단위로 가능합니다</li>
        <li>• 당일 예약은 2시간 전까지 가능합니다</li>
        <li>• 예약 취소는 1시간 전까지 가능합니다</li>
        <li>• 연속 사용 시간은 최대 4시간까지입니다</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
// 예약 생성 로직
</script> 