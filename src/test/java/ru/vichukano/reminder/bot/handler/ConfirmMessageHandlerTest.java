package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.dao.FileSystemMessageToRemindDao;
import ru.vichukano.reminder.bot.dao.InMemoryUserStateDao;
import ru.vichukano.reminder.bot.dao.RemindEntity;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

class ConfirmMessageHandlerTest {
    private final Map<String, BotUser> mockStore = new HashMap<>();
    private final Dao<RemindEntity> mockDao = Mockito.mock(FileSystemMessageToRemindDao.class);
    private final ConfirmMessageHandler testTarget = new ConfirmMessageHandler(
        new InMemoryUserStateDao(mockStore),
        mockDao
    );

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
        mockStore.put(user.getId(), user);
        final var context = MessageContext.builder()
            .chatId("3")
            .userId(user.getId())
            .message("confirm message")
            .build();

        final SendMessage result = testTarget.handle(context);

        Assertions.assertThat(result.getText()).isEqualTo(ConfirmMessageHandler.MESSAGE);
        Assertions.assertThat(mockStore.get(user.getId())).isNull();
        Mockito.verify(mockDao, Mockito.times(1)).add(ArgumentMatchers.any(RemindEntity.class));
    }

    @Test
    void shouldThrowException() {
        Assertions.assertThatThrownBy(() -> testTarget.handle(MessageContext.builder().build()))
            .isInstanceOf(HandlerException.class);
    }
}