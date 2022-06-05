package ru.vichukano.reminder.bot.web;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.util.List;

@Value
@Builder
@Jacksonized
public class NotificationInfo {
    String uuid;
    String sourceName;
    String requestDateTime;
    List<Notification> notifications;

    @Value
    @Builder
    @Jacksonized
    public static class Notification {
        String notifiedUserId;
        String notificationText;
    }
}
