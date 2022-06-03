package ru.vichukano.reminder.bot.handler;

import org.slf4j.Logger;

abstract class SkeletonHandler implements Handler<InMessageContext, OutMessageContext> {

    @Override
    public OutMessageContext handle(InMessageContext in) {
        try {
            log().trace("Start to handle context: {}", in);
            final OutMessageContext out = handleContext(in);
            log().trace("Out: {}", out);
            return out;
        } catch (Exception e) {
            throw new HandlerException("Failed to process context: " + in, e);
        }
    }

    protected abstract OutMessageContext handleContext(InMessageContext in);

    protected abstract Logger log();
}
