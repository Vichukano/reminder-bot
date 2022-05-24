package ru.vichukano.reminder.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;

class UnknownMessageHandler implements Handler<MessageContext, SendMessage> {

    @Override
    public SendMessage handle(MessageContext context) {
        return SendMessage.builder()
            .chatId(context.getChatId())
            .text(
                "Can not handle message: "
                    + context.getMessage()
                    + " please type "
                    + BotCommand.HELP.getVal()
            )
            .build();
    }

}
