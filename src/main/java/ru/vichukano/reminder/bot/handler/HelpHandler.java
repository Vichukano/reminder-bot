package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vichukano.reminder.bot.domain.BotCommand;

@Slf4j
@Component
public class HelpHandler extends AbstractUpdateHandler {

    @Override
    public SendMessage handle(Update update) {
        log.debug("Start to handle update: {}", update);
        final var out = SendMessage.builder()
            .chatId(chatId(update))
            .text(
                "Hello! I am reminder bot and I help you to remind something important for you\n"
                    + "Type remind command: "
                    + BotCommand.REMIND.getVal()
                    + " and input your remind message, choose remind date and time.\n"
                    + " Message to remind will send to your notification source."
            )
            .build();
        log.debug("Message out: {}", out);
        return out;
    }

}
