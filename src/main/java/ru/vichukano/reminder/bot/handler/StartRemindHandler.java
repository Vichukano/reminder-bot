package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vichukano.reminder.bot.dao.BotUser;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.UserState;

@Slf4j
@Component
public class StartRemindHandler extends AbstractUpdateHandler {
    private final Dao<BotUser> userDao;

    public StartRemindHandler(Dao<BotUser> userDao) {
        this.userDao = userDao;
    }

    @Override
    public SendMessage handle(Update update) {
        log.debug("Start to hande update: {}", update);
        final var botUser = BotUser.builder()
            .id(userId(update))
            .state(UserState.INPUT_MESSAGE)
            .build();
        userDao.add(botUser);
        final SendMessage out = SendMessage.builder()
            .chatId(chatId(update))
            .text("Please, input message what you want to remind and send it to me")
            .build();
        log.debug("Out message: {}", out);
        return out;
    }
}
