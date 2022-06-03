package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;

class UnknownMessageHandlerTest {
    private final UnknownMessageHandler testTarget = new UnknownMessageHandler();

    @Test
    void shouldHandleMessageContext() {
        final var context = MessageContext.builder()
            .chatId("1")
            .message("bla bla")
            .build();

        final SendMessage result = testTarget.handle(context);

        Assertions.assertThat(result.getText())
            .isEqualTo(String.format(UnknownMessageHandler.MESSAGE, context.getMessage(), BotCommand.HELP.getVal()));
        Assertions.assertThat(result.getChatId())
            .isEqualTo(context.getChatId());
    }
}