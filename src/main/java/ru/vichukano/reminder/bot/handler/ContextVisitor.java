package ru.vichukano.reminder.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.vichukano.reminder.bot.telegram.Factory;

@Slf4j
@Component
@RequiredArgsConstructor
class ContextVisitor implements Visitor<SendMessage> {
    private final Factory<InlineKeyboardMarkup> keyboardFactory;

    @Override
    public SendMessage visit(SimpleAnswerContext sCtx) {
        log.trace("Visit simple context: {}", sCtx);
        var out = new SendMessage();
        out.setText(sCtx.getMessage());
        return out;
    }

    @Override
    public SendMessage visit(KeyboardAnswerContext kCtx) {
        log.trace("Visit keyboard context: {}", kCtx);
        var out = new SendMessage();
        out.setText(kCtx.getMessage());
        out.setReplyMarkup(keyboardFactory.construct(kCtx.getKeyboardType()));
        return out;
    }
}
