package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.dao.RemindEntity;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;

@Slf4j
@Component("confirm")
@RequiredArgsConstructor
class ConfirmMessageHandler extends SkeletonHandler {
    static final String MESSAGE = "Successfully remind message!!!";
    private final Dao<RemindEntity> remindDao;

    @Override
    protected VisibleContext<SendMessage> handleContext(Context in) {
        final BotUser user = in.getUser();
        final var remindMessage = RemindEntity.builder()
            .uid(in.getUid().toString())
            .reminderId(user.getId())
            .messageText(user.getContext().getText())
            .remindDateTime(user.getContext().getTime().atDate(user.getContext().getDate()))
            .build();
        remindDao.add(remindMessage);
        final BotUser updated = BotUser.builder()
            .id(user.getId())
            .state(UserState.FINISH)
            .context(user.getContext())
            .build();
        return SimpleAnswerContext.builder()
            .uid(in.getUid())
            .message(MESSAGE)
            .user(updated)
            .build();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
