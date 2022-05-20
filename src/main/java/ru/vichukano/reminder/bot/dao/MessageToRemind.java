package ru.vichukano.reminder.bot.dao;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class MessageToRemind {
    String reminderId;
    String messageText;
    LocalDateTime remindDateTime;
}
