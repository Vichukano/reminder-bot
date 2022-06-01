package ru.vichukano.reminder.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.vichukano.reminder.bot.domain.BotCommand;

class UnknownMessageHandler implements Handler<MessageContext, SendMessage> {
    static final String MESSAGE = "Can not handle message: %s, please type: %s";

    @Override
    public SendMessage handle(MessageContext context) {
        return SendMessage.builder()
            .chatId(context.getChatId())
            .text(String.format(MESSAGE, context.getMessage(), BotCommand.HELP.getVal()))
            .build();
    }
}
