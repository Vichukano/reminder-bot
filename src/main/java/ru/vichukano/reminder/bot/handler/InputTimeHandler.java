package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
class InputTimeHandler implements Handler<MessageContext, SendMessage> {
    static final String MESSAGE = "Time accepted. To confirm choose: %s, or choose: %s for cancel message.\n"
        + "Message: %s, remind date-time: %sT%s";
    private final Dao<BotUser> userDao;

    @Override
    public SendMessage handle(MessageContext context) {
        try {
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
                    String.format(
                        MESSAGE,
                        BotCommand.CONFIRM.getVal(),
                        BotCommand.CANCEL.getVal(),
                        updated.getContext().getText(),
                        updated.getContext().getDate(),
                        updated.getContext().getTime()
                    )
                )
                .build();
        } catch (Exception e) {
            throw new HandlerException("Failed to process context: " + context, e);
        }
    }
}
