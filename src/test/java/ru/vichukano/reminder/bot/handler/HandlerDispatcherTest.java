package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.dao.RemindEntity;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import ru.vichukano.reminder.bot.telegram.KeyboardFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@SpringBootTest(classes = {
    HandlerDispatcher.class,
    ContextVisitor.class,
    KeyboardFactory.class,
    HelpHandler.class,
    StartRemindHandler.class,
    InputMessageHandler.class,
    InputDateHandler.class,
    InputTimeHandler.class,
    ConfirmMessageHandler.class,
    UnknownMessageHandler.class,
    CancelMessageHandler.class
})
class HandlerDispatcherTest {
    @SpyBean(name = "help")
    Handler<Context, VisibleContext<SendMessage>> helpHandler;
    @SpyBean(name = "start")
    Handler<Context, VisibleContext<SendMessage>> startRemindHandler;
    @SpyBean(name = "message")
    Handler<Context, VisibleContext<SendMessage>> inputMessageHandler;
    @SpyBean(name = "date")
    Handler<Context, VisibleContext<SendMessage>> inputDateHandler;
    @SpyBean(name = "time")
    Handler<Context, VisibleContext<SendMessage>> inputTimeHandler;
    @SpyBean(name = "confirm")
    Handler<Context, VisibleContext<SendMessage>> confirmMessageHandler;
    @SpyBean(name = "unknown")
    Handler<Context, VisibleContext<SendMessage>> unknownMessageHandler;
    @SpyBean(name = "cancel")
    Handler<Context, VisibleContext<SendMessage>> cancelMessageHandler;
    @MockBean
    private Dao<BotUser> botUserDao;
    @MockBean
    private Dao<RemindEntity> remindEntityDao;
    @Autowired
    private HandlerDispatcher testTarget;

    @Test
    void smoke() {
        Assertions.assertThat(testTarget).isNotNull();
    }

    @Test
    void shouldThrowIfInvalidMessage() {
        Assertions.assertThatThrownBy(() -> testTarget.handle(new Update()))
            .isInstanceOf(HandlerException.class);
    }

    @Test
    void shouldDispatchToHelpHandler() {
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.empty());
        Update update = update(BotCommand.HELP.getVal());

        testTarget.handle(update);

        Mockito.verify(helpHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
    }

    @Test
    void shouldDispatchToCancel() {
        final var user = BotUser.builder()
            .id("1")
            .state(UserState.INPUT_DATE)
            .build();
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.of(user));
        Update update = update(BotCommand.CANCEL.getVal());

        testTarget.handle(update);

        Mockito.verify(cancelMessageHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
    }

    @Test
    void shouldDispatchToStartRemind() {
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.empty());
        Update update = update(BotCommand.REMIND.getVal());

        testTarget.handle(update);

        Mockito.verify(startRemindHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
    }

    @Test
    void shouldDispatchToInputMessage() {
        final var user = BotUser.builder()
            .id("1")
            .state(UserState.INPUT_MESSAGE)
            .build();
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.of(user));
        Update update = update("message to remind");

        testTarget.handle(update);

        Mockito.verify(inputMessageHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
    }

    @Test
    void shouldDispatchToInputDate() {
        final var user = BotUser.builder()
            .id("1")
            .state(UserState.INPUT_DATE)
            .context(
                BotUser.RemindContext.builder()
                    .text("some text")
                    .build()
            )
            .build();
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.of(user));
        Update update = update(LocalDate.now().toString());

        testTarget.handle(update);

        Mockito.verify(inputDateHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
    }

    @Test
    void shouldDispatchToInputTime() {
        final var user = BotUser.builder()
            .id("1")
            .state(UserState.INPUT_TIME)
            .context(
                BotUser.RemindContext.builder()
                    .text("some text")
                    .date(LocalDate.now())
                    .build()
            )
            .build();
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.of(user));
        Update update = update(LocalTime.now().toString());

        testTarget.handle(update);

        Mockito.verify(inputTimeHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
    }

    @Test
    void shouldDispatchToConfirm() {
        final var user = BotUser.builder()
            .id("1")
            .state(UserState.CONFIRM)
            .context(
                BotUser.RemindContext.builder()
                    .text("remind me")
                    .date(LocalDate.now())
                    .time(LocalTime.now())
                    .build()
            )
            .build();
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.of(user));
        Update update = update(BotCommand.CONFIRM.getVal());

        testTarget.handle(update);

        Mockito.verify(confirmMessageHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
    }

    @Test
    void shouldDispatchToUnknown() {
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.empty());
        Update update = update("Unknown command");

        testTarget.handle(update);

        Mockito.verify(unknownMessageHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
    }

    private Update update(String text) {
        Update u = new Update();
        Message m = new Message();
        User user = new User(1L, "test", false);
        m.setChat(new Chat(2L, "test_chat"));
        m.setFrom(user);
        m.setText(text);
        u.setMessage(m);
        return u;
    }
}
