package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.util.Optional;

@Slf4j
@Component
public class HandlerDispatcher implements Handler<Update, SendMessage> {
    private final Handler<MessageContext, SendMessage> helpHandler;
    private final Handler<MessageContext, SendMessage> startRemindHandler;
    private final Handler<MessageContext, SendMessage> inputMessageHandler;
    private final Handler<MessageContext, SendMessage> inputDateHandler;
    private final Handler<MessageContext, SendMessage> inputTimeHandler;
    private final Handler<MessageContext, SendMessage> confirmMessageHandler;
    private final Handler<MessageContext, SendMessage> unknownMessageHandler;
    private final Dao<BotUser> userDao;

    public HandlerDispatcher(@Qualifier("help") Handler<MessageContext, SendMessage> helpHandler,
                             @Qualifier("start") Handler<MessageContext, SendMessage> startRemindHandler,
                             @Qualifier("message") Handler<MessageContext, SendMessage> inputMessageHandler,
                             @Qualifier("date") Handler<MessageContext, SendMessage> inputDateHandler,
                             @Qualifier("time") Handler<MessageContext, SendMessage> inputTimeHandler,
                             @Qualifier("confirm") Handler<MessageContext, SendMessage> confirmMessageHandler,
                             @Qualifier("unknown") Handler<MessageContext, SendMessage> unknownMessageHandler,
                             Dao<BotUser> userDao) {
        this.helpHandler = helpHandler;
        this.startRemindHandler = startRemindHandler;
        this.inputMessageHandler = inputMessageHandler;
        this.inputDateHandler = inputDateHandler;
        this.inputTimeHandler = inputTimeHandler;
        this.confirmMessageHandler = confirmMessageHandler;
        this.unknownMessageHandler = unknownMessageHandler;
        this.userDao = userDao;
    }

    @Override
    public SendMessage handle(Update update) {
        log.debug("Start to handle update: {}", update);
        if (!isUpdateValid(update)) {
            throw new IllegalArgumentException("Got invalid update");
        }
        final String userId = userId(update);
        final String chatId = chatId(update);
        final String text = text(update);
        final MessageContext context = MessageContext.builder()
            .userId(userId)
            .chatId(chatId)
            .message(text)
            .build();
        final UserState state = userDao.find(userId).map(BotUser::getState).orElse(UserState.START);
        final SendMessage out;
        if (BotCommand.HELP.getVal().equals(text)) {
            out = helpHandler.handle(context);
        } else if (UserState.START.equals(state) && BotCommand.REMIND.getVal().equals(text)) {
            out = startRemindHandler.handle(context);
        } else if (UserState.INPUT_MESSAGE.equals(state)) {
            out = inputMessageHandler.handle(context);
        } else if (UserState.INPUT_DATE.equals(state)) {
            out = inputDateHandler.handle(context);
        } else if (UserState.INPUT_TIME.equals(state)) {
            out = inputTimeHandler.handle(context);
        } else if (UserState.CONFIRM.equals(state)) {
            out = confirmMessageHandler.handle(context);
        } else {
            out = unknownMessageHandler.handle(context);
        }
        log.debug("Out: {}", out);
        return out;
    }

    private boolean isUpdateValid(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    private String chatId(Update update) {
        return Optional.ofNullable(update)
            .map(Update::getMessage)
            .map(Message::getChatId)
            .map(String::valueOf)
            .orElseThrow(() -> new IllegalStateException("Can't find chat id for update: " + update));
    }

    private String userId(Update update) {
        return Optional.ofNullable(update)
            .map(Update::getMessage)
            .map(Message::getFrom)
            .map(User::getId)
            .map(String::valueOf)
            .orElseThrow(() -> new IllegalStateException("Can't find user id for update: " + update));
    }

    private String text(Update update) {
        return Optional.ofNullable(update)
            .map(Update::getMessage)
            .map(Message::getText)
            .orElseThrow(() -> new IllegalStateException("Can't find text for update: " + update));
    }
}
