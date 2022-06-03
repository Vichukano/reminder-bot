package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.Factory;

@RequiredArgsConstructor
class InputMessageHandler implements Handler<MessageContext, SendMessage> {
    static final String MESSAGE = "Message accepted. Now choose remind date from current values:\n";
    private final Dao<BotUser> userDao;
    private final Factory<InlineKeyboardMarkup> factory;

    @Override
    public SendMessage handle(MessageContext context) {
        try {
            final String userId = context.getUserId();
            final BotUser current = userDao.find(userId)
                .orElseThrow(() -> new IllegalStateException("Can't find user with id: " + userId));
            final BotUser updated = BotUser.builder()
                .id(current.getId())
                .state(UserState.INPUT_DATE)
                .context(
                    BotUser.RemindContext.builder()
                        .text(context.getMessage())
                        .build()
                )
                .build();
            userDao.add(updated);
            return SendMessage.builder()
                .chatId(context.getChatId())
                .text(MESSAGE)
                .replyMarkup(factory.construct(Factory.Item.DATE))
                .build();
        } catch (Exception e) {
            throw new HandlerException("Failed to precess context: " + context, e);
        }
    }
}
