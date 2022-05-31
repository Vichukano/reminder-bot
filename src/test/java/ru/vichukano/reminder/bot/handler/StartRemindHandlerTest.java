package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.InMemoryUserStateDao;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.util.HashMap;
import java.util.Map;

class StartRemindHandlerTest {
    private final Map<String, BotUser> mockStore = new HashMap<>();
    private final StartRemindHandler testTarget = new StartRemindHandler(
        new InMemoryUserStateDao(
            mockStore
        )
    );

    @Test
    void shouldSaveUserWithCorrectState() {
        final var context = MessageContext.builder()
            .chatId("1")
            .userId("2")
            .message("bla bla bla")
            .build();

        final SendMessage result = testTarget.handle(context);

        Assertions.assertThat(result.getText()).isEqualTo(StartRemindHandler.MESSAGE);
        Assertions.assertThat(mockStore.get("2").getState()).isEqualTo(UserState.INPUT_MESSAGE);
        Assertions.assertThat(mockStore.get("2").getId()).isEqualTo("2");
    }
}