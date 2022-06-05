package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.time.LocalDate;
import java.util.UUID;

class InputDateHandlerTest {
    private final InputDateHandler testTarget = new InputDateHandler();

    @Test
    void shouldUpdateUserWithCorrectStateAndReturnMessage() {
        final var user = BotUser.builder()
            .id("1")
            .context(
                BotUser.RemindContext.builder()
                    .text("test test")
                    .build()
            )
            .build();
        final var date = LocalDate.now();
        final var context = InMessageContext.builder()
            .uid(UUID.randomUUID())
            .user(user)
            .message(date.toString())
            .build();

        final VisibleContext<SendMessage> result = testTarget.handle(context);

        Assertions.assertThat(result.getMessage()).isEqualTo(String.format(InputDateHandler.MESSAGE, date));
        Assertions.assertThat(result.getUser().getState()).isEqualTo(UserState.INPUT_TIME);
    }

    @Test
    void shouldNotChangeUserStateIfParseException() {
        final var context = InMessageContext.builder()
            .uid(UUID.randomUUID())
            .user(
                BotUser.builder()
                    .state(UserState.INPUT_DATE)
                    .build()
            )
            .message("Not Date!")
            .build();

        final VisibleContext<SendMessage> result = testTarget.handle(context);

        Assertions.assertThat(result.getMessage()).isEqualTo(InputDateHandler.WRONG_DATE_MESSAGE);
        Assertions.assertThat(result.getUser().getState()).isEqualTo(UserState.INPUT_DATE);
    }
}
