package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.Factory;
import java.time.LocalDate;

@RequiredArgsConstructor
class InputDateHandler implements Handler<MessageContext, SendMessage> {
    static final String MESSAGE = "Date [%s] accepted. Now choose remind time from current values:\n";
    private final Dao<BotUser> userDao;
    private final Factory<InlineKeyboardMarkup> factory;

    @Override
    public SendMessage handle(MessageContext context) {
        try {
            final BotUser current = userDao.find(context.getUserId()).orElseThrow();
            final var date = LocalDate.parse(context.getMessage());
            final BotUser updated = BotUser.builder()
                .id(current.getId())
                .state(UserState.INPUT_TIME)
                .context(
                    BotUser.RemindContext.builder()
                        .text(current.getContext().getText())
                        .date(date)
                        .build()
                )
                .build();
            userDao.add(updated);
            return SendMessage.builder()
                .chatId(context.getChatId())
                .text(String.format(MESSAGE, date))
                .replyMarkup(factory.construct(Factory.Item.TIME))
                .build();
        } catch (Exception e) {
            throw new HandlerException("Can't process context: " + context, e);
        }
    }
}
