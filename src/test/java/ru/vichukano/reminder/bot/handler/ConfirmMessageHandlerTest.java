package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.dao.FileSystemMessageToRemindDao;
import ru.vichukano.reminder.bot.dao.RemindEntity;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

class ConfirmMessageHandlerTest {
    private final Dao<RemindEntity> mockDao = Mockito.mock(FileSystemMessageToRemindDao.class);
    private final ConfirmMessageHandler testTarget = new ConfirmMessageHandler(mockDao);

    @Test
    void shouldSaveReminderEntity() {
        final var user = BotUser.builder()
            .id("1")
            .state(UserState.CONFIRM)
            .context(
                BotUser.RemindContext.builder()
                    .text("text to remind")
                    .date(LocalDate.now())
                    .time(LocalTime.now())
                    .build()
            )
            .build();
        final var context = InMessageContext.builder()
            .uid(UUID.randomUUID())
            .message("confirm message")
            .user(user)
            .build();

        final VisibleContext<SendMessage> result = testTarget.handle(context);

        Assertions.assertThat(result.getMessage()).isEqualTo(ConfirmMessageHandler.MESSAGE);
        Assertions.assertThat(result.getUser().getState()).isEqualTo(UserState.FINISH);
        Mockito.verify(mockDao, Mockito.times(1)).add(ArgumentMatchers.any(RemindEntity.class));
    }
}