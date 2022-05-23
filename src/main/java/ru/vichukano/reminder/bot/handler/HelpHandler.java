package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;

@Slf4j
class HelpHandler implements Handler<MessageContext, SendMessage> {

    @Override
    public SendMessage handle(MessageContext context) {
        return SendMessage.builder()
            .chatId(context.getChatId())
            .text(
                "Hello! I am reminder bot and I help you to remind something important for you\n"
                    + "Type remind command: "
                    + BotCommand.REMIND.getVal()
                    + " and input your remind message, choose remind date and time.\n"
                    + " Message to remind will send to your notification source."
            )
            .build();
    }

}
