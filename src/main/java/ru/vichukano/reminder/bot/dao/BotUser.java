package ru.vichukano.reminder.bot.dao;

import lombok.Builder;
import lombok.Value;
import ru.vichukano.reminder.bot.domain.UserState;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
public class BotUser {
    String id;
    UserState state;
    RemindContext context;

    @Value
    @Builder
    public static class RemindContext {
        String text;
        LocalDate date;
        LocalTime time;
    }
}
