/**
 * 클릭 외부 감지 디렉티브
 * 요소 외부를 클릭했을 때 이벤트를 발생시킵니다.
 */

export const clickOutside = {
  mounted(el, binding) {
    el._clickOutside = (event) => {
      // 요소 내부 클릭인지 확인
      if (!(el === event.target || el.contains(event.target))) {
        // 바인딩된 함수 호출
        if (typeof binding.value === 'function') {
          binding.value(event)
        }
      }
    }
    
    // 이벤트 리스너 등록
    document.addEventListener('click', el._clickOutside)
  },
  
  unmounted(el) {
    // 이벤트 리스너 제거
    document.removeEventListener('click', el._clickOutside)
    delete el._clickOutside
  }
}

export default clickOutside 