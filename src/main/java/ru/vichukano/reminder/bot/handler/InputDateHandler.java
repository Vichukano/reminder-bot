package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.Factory;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
class InputDateHandler implements Handler<MessageContext, SendMessage> {
    private final Dao<BotUser> userDao;
    private final Factory<InlineKeyboardMarkup> factory;

    @Override
    public SendMessage handle(MessageContext context) {
        final BotUser current = userDao.find(context.getUserId()).orElseThrow();
        final BotUser updated = BotUser.builder()
            .id(current.getId())
            .state(UserState.INPUT_TIME)
            .context(
                BotUser.RemindContext.builder()
                    .text(current.getContext().getText())
                    .date(LocalDate.parse(context.getMessage()))
                    .build()
            )
            .build();
        userDao.add(updated);
        return SendMessage.builder()
            .chatId(context.getChatId())
            .text("Date received. Now choose remind time from current values:\n")
            .replyMarkup(factory.construct(Factory.Item.TIME))
            .build();
    }
}
