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
import java.util.UUID;

@Slf4j
@Component
public class HandlerDispatcher implements Handler<Update, SendMessage> {
    private final Handler<Context, VisibleContext<SendMessage>> helpHandler;
    private final Handler<Context, VisibleContext<SendMessage>> startRemindHandler;
    private final Handler<Context, VisibleContext<SendMessage>> inputMessageHandler;
    private final Handler<Context, VisibleContext<SendMessage>> inputDateHandler;
    private final Handler<Context, VisibleContext<SendMessage>> inputTimeHandler;
    private final Handler<Context, VisibleContext<SendMessage>> confirmMessageHandler;
    private final Handler<Context, VisibleContext<SendMessage>> unknownMessageHandler;
    private final Handler<Context, VisibleContext<SendMessage>> cancelMessageHandler;
    private final Dao<BotUser> userDao;
    private final Visitor<SendMessage> visitor;

    public HandlerDispatcher(@Qualifier("help") Handler<Context, VisibleContext<SendMessage>> helpHandler,
                             @Qualifier("start") Handler<Context, VisibleContext<SendMessage>> startRemindHandler,
                             @Qualifier("message") Handler<Context, VisibleContext<SendMessage>> inputMessageHandler,
                             @Qualifier("date") Handler<Context, VisibleContext<SendMessage>> inputDateHandler,
                             @Qualifier("time") Handler<Context, VisibleContext<SendMessage>> inputTimeHandler,
                             @Qualifier("confirm") Handler<Context, VisibleContext<SendMessage>> confirmMessageHandler,
                             @Qualifier("unknown") Handler<Context, VisibleContext<SendMessage>> unknownMessageHandler,
                             @Qualifier("cancel") Handler<Context, VisibleContext<SendMessage>> cancelMessageHandler,
                             Dao<BotUser> userDao,
                             Visitor<SendMessage> visitor) {
        this.helpHandler = helpHandler;
        this.startRemindHandler = startRemindHandler;
        this.inputMessageHandler = inputMessageHandler;
        this.inputDateHandler = inputDateHandler;
        this.inputTimeHandler = inputTimeHandler;
        this.confirmMessageHandler = confirmMessageHandler;
        this.unknownMessageHandler = unknownMessageHandler;
        this.cancelMessageHandler = cancelMessageHandler;
        this.userDao = userDao;
        this.visitor = visitor;
    }

    @Override
    public SendMessage handle(Update update) {
        try {
            log.debug("Start to handle update: {}", update);
            if (!isUpdateValid(update)) {
                throw new IllegalArgumentException("Got invalid update");
            }
            final String userId = userId(update);
            final String chatId = chatId(update);
            final String text = text(update);
            final BotUser user = userDao.find(userId)
                .orElse(
                    BotUser.builder()
                        .id(userId)
                        .state(UserState.START)
                        .build()
                );
            final UserState state = user.getState();
            final var context = InMessageContext.builder()
                .uid(UUID.randomUUID())
                .user(user)
                .message(text)
                .build();
            final VisibleContext<SendMessage> out;
            if (BotCommand.HELP.getVal().equals(text)) {
                out = helpHandler.handle(context);
            } else if (BotCommand.CANCEL.getVal().equals(text) && state != UserState.START) {
                out = cancelMessageHandler.handle(context);
            } else if (UserState.START.equals(state) && BotCommand.REMIND.getVal().equals(text)) {
                out = startRemindHandler.handle(context);
            } else if (UserState.INPUT_MESSAGE.equals(state)) {
                out = inputMessageHandler.handle(context);
            } else if (UserState.INPUT_DATE.equals(state)) {
                out = inputDateHandler.handle(context);
            } else if (UserState.INPUT_TIME.equals(state)) {
                out = inputTimeHandler.handle(context);
            } else if (UserState.CONFIRM.equals(state) && BotCommand.CONFIRM.getVal().equals(text)) {
                out = confirmMessageHandler.handle(context);
            } else {
                out = unknownMessageHandler.handle(context);
            }
            final BotUser updated = out.getUser();
            if (UserState.FINISH.equals(updated.getState())) {
                userDao.remove(updated);
            } else if (!UserState.START.equals(updated.getState())) {
                userDao.add(updated);
            }
            SendMessage answer = out.visit(visitor);
            answer.setChatId(chatId);
            log.debug("Out: {}", out);
            return answer;
        } catch (Exception e) {
            throw new HandlerException("Failed to process update: " + update, e);
        }
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
