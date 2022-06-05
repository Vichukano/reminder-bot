package ru.vichukano.reminder.bot.handler;

import ru.vichukano.reminder.bot.domain.BotUser;
import java.util.UUID;

interface Context {

    UUID getUid();

    BotUser getUser();

    String getMessage();

}
