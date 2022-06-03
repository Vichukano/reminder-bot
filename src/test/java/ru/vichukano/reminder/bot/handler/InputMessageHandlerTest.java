package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.InMemoryUserStateDao;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.KeyboardFactory;
import java.util.HashMap;
import java.util.Map;

class InputMessageHandlerTest {
    private final Map<String, BotUser> mockStore = new HashMap<>();
    private final InputMessageHandler testTarget = new InputMessageHandler(
        new InMemoryUserStateDao(mockStore),
        new KeyboardFactory()
    );

    @Test
    void shouldUpdateUserAndReturnMessage() {
        final var user = BotUser.builder()
            .id("1")
            .build();
        final var message = "Message to remind!!!";
        mockStore.put(user.getId(), user);
        final var context = MessageContext.builder()
            .chatId("111")
            .userId(user.getId())
            .message(message)
            .build();

        final SendMessage result = testTarget.handle(context);

        Assertions.assertThat(result.getText()).isEqualTo(InputMessageHandler.MESSAGE);
        Assertions.assertThat(mockStore.get(user.getId()).getState()).isEqualTo(UserState.INPUT_DATE);
        Assertions.assertThat(mockStore.get(user.getId()).getContext().getText()).isEqualTo(message);

    }

    @Test
    void shouldThrowException() {
        Assertions.assertThatThrownBy(() -> testTarget.handle(null)).isInstanceOf(HandlerException.class);
    }
}
