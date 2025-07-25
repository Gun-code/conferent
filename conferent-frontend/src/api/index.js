/**
 * API 클라이언트 통합 인덱스
 * 모든 엔티티별 API 클라이언트를 한 곳에서 관리
 */

// 기본 API 클라이언트
export { default as apiClient } from './ApiClient';

// 엔티티별 API 클라이언트
export { authApiClient } from './auth/ApiClient';
export { default as authApi } from './auth/ApiClient';
export { roomApiClient } from './room/ApiClient';
export { userApiClient } from './user/ApiClient';
export { rentApiClient } from './rent/ApiClient';
export { roomRentApiClient } from './roomrent/ApiClient';
export { userInviteApiClient } from './userinvite/ApiClient';

// API 클라이언트 통합 객체는 별도 파일에서 import 후 생성
import { authApiClient } from './auth/ApiClient';
import { roomApiClient } from './room/ApiClient';
import { userApiClient } from './user/ApiClient';
import { rentApiClient } from './rent/ApiClient';
import { roomRentApiClient } from './roomrent/ApiClient';
import { userInviteApiClient } from './userinvite/ApiClient';

export const api = {
  auth: authApiClient,
  room: roomApiClient,
  user: userApiClient,
  rent: rentApiClient,
  roomRent: roomRentApiClient,
  userInvite: userInviteApiClient
}; 