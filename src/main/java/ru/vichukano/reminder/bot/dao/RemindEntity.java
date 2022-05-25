package ru.vichukano.reminder.bot.dao;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class RemindEntity {
    String reminderId;
    String messageText;
    LocalDateTime remindDateTime;
}
