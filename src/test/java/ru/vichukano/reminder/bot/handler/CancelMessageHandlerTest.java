package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.dao.InMemoryUserStateDao;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.util.HashMap;
import java.util.Map;

class CancelMessageHandlerTest {
    private final Map<String, BotUser> mockStore = new HashMap<>();
    private final CancelMessageHandler testTarget = new CancelMessageHandler(
        new InMemoryUserStateDao(mockStore)
    );

    @Test
    void shouldRemoveUser() {
        final var user = BotUser.builder()
            .id("1")
            .state(UserState.INPUT_DATE)
            .build();
        mockStore.put(user.getId(), user);
        final var context = MessageContext.builder()
            .chatId("33")
            .userId(user.getId())
            .message(BotCommand.CANCEL.getVal())
            .build();

        final SendMessage result = testTarget.handle(context);

        Assertions.assertThat(result.getText()).isEqualTo(CancelMessageHandler.MESSAGE);
        Assertions.assertThat(mockStore.get(user.getId())).isNull();
    }

    @Test
    void shouldThrowException() {
        Assertions.assertThatThrownBy(() -> testTarget.handle(MessageContext.builder().build()))
            .isInstanceOf(HandlerException.class);
    }
}
