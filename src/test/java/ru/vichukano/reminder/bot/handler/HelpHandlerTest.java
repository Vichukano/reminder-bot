package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;

class HelpHandlerTest {
    private final HelpHandler testTarget = new HelpHandler();

    @Test
    void shouldAnswerWithHelpMessage() {
        final var context = MessageContext.builder()
            .chatId("1")
            .build();

        final SendMessage result = testTarget.handle(context);

        Assertions.assertThat(result.getText())
            .isEqualTo(String.format(HelpHandler.MESSAGE, BotCommand.REMIND.getVal()));
    }
}