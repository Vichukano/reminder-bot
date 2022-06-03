package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.InMemoryUserStateDao;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.KeyboardFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class InputDateHandlerTest {
    private final Map<String, BotUser> mockStore = new HashMap<>();
    private final InputDateHandler testTarget = new InputDateHandler(
        new InMemoryUserStateDao(mockStore),
        new KeyboardFactory()
    );

    @Test
    void shouldSaveUserWithCorrectStateAndReturnMessage() {
        final var user = BotUser.builder()
            .id("1")
            .context(
                BotUser.RemindContext.builder()
                    .text("test test")
                    .build()
            )
            .build();
        mockStore.put(user.getId(), user);
        final var date = LocalDate.now();
        final var context = MessageContext.builder()
            .chatId("111")
            .userId(user.getId())
            .message(date.toString())
            .build();

        final SendMessage result = testTarget.handle(context);

        Assertions.assertThat(result.getText()).isEqualTo(String.format(InputDateHandler.MESSAGE, date));
        Assertions.assertThat(mockStore.get(user.getId()).getState()).isEqualTo(UserState.INPUT_TIME);
        Assertions.assertThat(mockStore.get(user.getId()).getContext().getDate()).isEqualTo(date);
    }

    @Test
    void shouldThrowHandlerException() {
        final var context = MessageContext.builder()
            .chatId("111")
            .userId("2")
            .message("not date")
            .build();

        Assertions.assertThatThrownBy(() -> testTarget.handle(context)).isInstanceOf(HandlerException.class);
    }
}
