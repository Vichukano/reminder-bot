package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
class LoggableHandler implements Handler<MessageContext, SendMessage> {
    private final Handler<MessageContext, SendMessage> origin;
    private final String className;

    LoggableHandler(Handler<MessageContext, SendMessage> origin) {
        this.origin = origin;
        this.className = origin.getClass().getSimpleName();
    }

    @Override
    public SendMessage handle(MessageContext context) {
        if (log.isTraceEnabled()) {
            log.trace("[{}] start to handle context: {}", className, context);
        }
        var out = origin.handle(context);
        if (log.isTraceEnabled()) {
            log.trace("[{}] message out: {}", className, out);
        }
        return out;
    }
}
