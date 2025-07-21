package com.conferent.business.service;

public interface NotificationService {
    void sendReservationConfirmation(Long userId, String roomName, String startTime, String endTime);
    void sendReservationCancellation(Long userId, String roomName, String startTime, String endTime);
    void sendReservationReminder(Long userId, String roomName, String startTime);
} 