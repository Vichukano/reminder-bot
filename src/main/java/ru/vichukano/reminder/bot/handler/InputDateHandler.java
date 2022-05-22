package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.vichukano.reminder.bot.dao.BotUser;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.Factory;
import java.time.LocalDate;

@Slf4j
@Component
public class InputDateHandler extends AbstractUpdateHandler {
    private final Dao<BotUser> userDao;
    private final Factory<InlineKeyboardMarkup> factory;

    public InputDateHandler(Dao<BotUser> userDao, Factory<InlineKeyboardMarkup> factory) {
        this.userDao = userDao;
        this.factory = factory;
    }

    @Override
    public SendMessage handle(Update update) {
        log.trace("Receive update: {}", update);
        final String chatId = chatId(update);
        final String text = text(update);
        final String userId = userId(update);
        final BotUser current = userDao.find(userId).orElseThrow();
        final BotUser updated = BotUser.builder()
            .id(current.getId())
            .state(UserState.INPUT_TIME)
            .context(
                BotUser.RemindContext.builder()
                    .text(current.getContext().getText())
                    .date(LocalDate.parse(text))
                    .build()
            )
            .build();
        userDao.add(updated);
        final SendMessage out = SendMessage.builder()
            .chatId(chatId)
            .text("Message received. Now choose remind time from current values:\n")
            .replyMarkup(factory.construct(Factory.Item.TIME))
            .build();
        log.trace("Out message: {}", out);
        return out;
    }
}
