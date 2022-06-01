package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.InMemoryUserStateDao;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

class InputTimeHandlerTest {
    private final Map<String, BotUser> mockStore = new HashMap<>();
    private final InputTimeHandler testTarget = new InputTimeHandler(
        new InMemoryUserStateDao(
            mockStore
        )
    );

    @Test
    void shouldUpdateUserAndReturnMessage() {
        final var user = BotUser.builder()
            .id("1")
            .context(
                BotUser.RemindContext.builder()
                    .text("test test")
                    .date(LocalDate.now())
                    .build()
            )
            .build();
        mockStore.put(user.getId(), user);
        final var time = LocalTime.now();
        final var context = MessageContext.builder()
            .chatId("111")
            .userId(user.getId())
            .message(time.toString())
            .build();

        final SendMessage result = testTarget.handle(context);

        Assertions.assertThat(result.getText())
            .isEqualTo(String.format(
                InputTimeHandler.MESSAGE,
                BotCommand.CONFIRM.getVal(),
                BotCommand.CANCEL.getVal(),
                user.getContext().getText(),
                user.getContext().getDate(),
                time
            ));
        Assertions.assertThat(mockStore.get(user.getId()).getState()).isEqualTo(UserState.CONFIRM);
        Assertions.assertThat(mockStore.get(user.getId()).getContext().getTime()).isEqualTo(time);
    }

    @Test
    void shouldThrowHandlerException() {
        final var context = MessageContext.builder()
            .chatId("111")
            .userId("2")
            .message("not time")
            .build();

        Assertions.assertThatThrownBy(() -> testTarget.handle(context)).isInstanceOf(HandlerException.class);
    }
}
