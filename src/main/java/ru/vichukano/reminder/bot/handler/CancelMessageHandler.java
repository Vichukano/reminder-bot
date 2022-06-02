package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.BotUser;

@RequiredArgsConstructor
class CancelMessageHandler implements Handler<MessageContext, SendMessage> {
    static final String MESSAGE = "Remind aborted";
    private final Dao<BotUser> userDao;

    @Override
    public SendMessage handle(MessageContext context) {
        try {
            final var user = userDao.find(context.getUserId()).orElseThrow();
            userDao.remove(user);
            return SendMessage.builder()
                .chatId(context.getChatId())
                .text(MESSAGE)
                .build();
        } catch (Exception e) {
            throw new HandlerException("Failed to process context: " + context, e);
        }
    }
}
