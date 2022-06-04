package ru.vichukano.reminder.bot.handler;

import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

abstract class SkeletonHandler implements Handler<Context, VisibleContext<SendMessage>> {

    @Override
    public VisibleContext<SendMessage> handle(Context in) {
        try {
            log().info("Start to handle context with UUID: {}", in.getUid());
            log().trace("Start to handle context: {}", in);
            final VisibleContext<SendMessage> out = handleContext(in);
            log().trace("Out: {}", out);
            log().info("Finish handle context with UUID: {}", in.getUid());
            return out;
        } catch (Exception e) {
            throw new HandlerException("Failed to process context: " + in, e);
        }
    }

    protected abstract VisibleContext<SendMessage> handleContext(Context in);

    protected abstract Logger log();
}
