package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

class InputTimeHandlerTest {
    private final InputTimeHandler testTarget = new InputTimeHandler();

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
        final var time = LocalTime.now();
        final var context = InMessageContext.builder()
            .uid(UUID.randomUUID())
            .user(user)
            .message(time.toString())
            .build();

        final VisibleContext<SendMessage> result = testTarget.handle(context);

        Assertions.assertThat(result.getMessage())
            .isEqualTo(String.format(
                InputTimeHandler.MESSAGE,
                BotCommand.CONFIRM.getVal(),
                BotCommand.CANCEL.getVal(),
                user.getContext().getText(),
                user.getContext().getDate(),
                time
            ));
        Assertions.assertThat(result.getUser().getState()).isEqualTo(UserState.CONFIRM);
    }

    @Test
    void shouldNotChangeUserIfTimeParseException() {
        final var context = InMessageContext.builder()
            .uid(UUID.randomUUID())
            .user(BotUser.builder()
                .state(UserState.INPUT_TIME)
                .build())
            .message("not time!!!")
            .build();

        final VisibleContext<SendMessage> result = testTarget.handleContext(context);

        Assertions.assertThat(result.getUser().getState()).isEqualTo(UserState.INPUT_TIME);
        Assertions.assertThat(result.getMessage()).isEqualTo(InputTimeHandler.WRONG_TIME_MESSAGE);
    }
}
