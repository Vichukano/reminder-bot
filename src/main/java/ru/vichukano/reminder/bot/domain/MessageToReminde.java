package ru.vichukano.reminder.bot.domain;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;

@Value
@Builder
public class MessageToReminde {
    String reminderId;
    String messageText;
    LocalDate remindDate;
}
