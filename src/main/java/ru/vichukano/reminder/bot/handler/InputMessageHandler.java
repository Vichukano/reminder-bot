package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.Factory;

@Slf4j
@RequiredArgsConstructor
class InputMessageHandler implements Handler<MessageContext, SendMessage> {
    private final Dao<BotUser> userDao;
    private final Factory<InlineKeyboardMarkup> factory;

    @Override
    public SendMessage handle(MessageContext context) {
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
            .text("Message received. Now choose remind date from current values:\n")
            .replyMarkup(factory.construct(Factory.Item.DATE))
            .build();
    }
}
