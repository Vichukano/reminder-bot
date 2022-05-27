package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.dao.RemindEntity;
import ru.vichukano.reminder.bot.domain.BotUser;

@Slf4j
@RequiredArgsConstructor
class ConfirmMessageHandler implements Handler<MessageContext, SendMessage> {
    private final Dao<BotUser> userDao;
    private final Dao<RemindEntity> remindDao;

    @Override
    public SendMessage handle(MessageContext context) {
        final BotUser current = userDao.find(context.getUserId()).orElseThrow();
        final var remindMessage = RemindEntity.builder()
            .reminderId(current.getId())
            .messageText(current.getContext().getText())
            .remindDateTime(current.getContext().getTime().atDate(current.getContext().getDate()))
            .build();
        remindDao.add(remindMessage);
        userDao.remove(current);
        return SendMessage.builder()
            .chatId(context.getChatId())
            .text("Successfully remind message!!!")
            .build();
    }
}
