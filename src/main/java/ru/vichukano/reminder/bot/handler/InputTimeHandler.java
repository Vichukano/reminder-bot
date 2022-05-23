package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.BotUser;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.UserState;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
class InputTimeHandler implements Handler<MessageContext, SendMessage> {
    private final Dao<BotUser> userDao;

    @Override
    public SendMessage handle(MessageContext context) {
        final BotUser current = userDao.find(context.getUserId()).orElseThrow();
        final BotUser updated = BotUser.builder()
            .id(current.getId())
            .state(UserState.CONFIRM)
            .context(
                BotUser.RemindContext.builder()
                    .text(current.getContext().getText())
                    .date(current.getContext().getDate())
                    .time(LocalTime.parse(context.getMessage()))
                    .build()
            )
            .build();
        userDao.add(updated);
        return SendMessage.builder()
            .chatId(context.getChatId())
            .text(
                "Time received. To confirm choose: "
                    + BotCommand.CONFIRM.getVal()
                    + " or choose: "
                    + BotCommand.CONFIRM.getVal()
                    + " for cancel message.\n"
                    + " Message: "
                    + updated.getContext()
            )
            .build();
    }
}
