package ru.vichukano.reminder.bot.dao;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class RemindEntity {
    String reminderId;
    String messageText;
    LocalDateTime remindDateTime;
}
