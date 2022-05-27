package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;

@Slf4j
@RequiredArgsConstructor
class StartRemindHandler implements Handler<MessageContext, SendMessage> {
    private final Dao<BotUser> userDao;

    @Override
    public SendMessage handle(MessageContext context) {
        final var botUser = BotUser.builder()
            .id(context.getUserId())
            .state(UserState.INPUT_MESSAGE)
            .build();
        userDao.add(botUser);
        return SendMessage.builder()
            .chatId(context.getChatId())
            .text("Please, input message what you want to remind and send it to me")
            .build();
    }
}
