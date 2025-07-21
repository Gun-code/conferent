package com.conferent.persistence.notification;

import com.conferent.business.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class WebPushNotificationServiceImpl implements NotificationService {
    
    @Override
    public void sendReservationConfirmation(Long userId, String roomName, String startTime, String endTime) {
        // 실제 웹푸시 구현 - 현재는 콘솔 출력
        System.out.println("예약 확정 알림 발송");
        System.out.printf("사용자: %d, 회의실: %s, 시간: %s ~ %s%n", 
                         userId, roomName, startTime, endTime);
    }
    
    @Override
    public void sendReservationCancellation(Long userId, String roomName, String startTime, String endTime) {
        // 실제 웹푸시 구현 - 현재는 콘솔 출력
        System.out.println("예약 취소 알림 발송");
        System.out.printf("사용자: %d, 회의실: %s, 시간: %s ~ %s%n", 
                         userId, roomName, startTime, endTime);
    }
    
    @Override
    public void sendReservationReminder(Long userId, String roomName, String startTime) {
        // 실제 웹푸시 구현 - 현재는 콘솔 출력
        System.out.println("예약 리마인더 알림 발송");
        System.out.printf("사용자: %d, 회의실: %s, 시작시간: %s%n", 
                         userId, roomName, startTime);
    }
} 