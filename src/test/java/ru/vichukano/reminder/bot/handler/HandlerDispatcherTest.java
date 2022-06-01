package ru.vichukano.reminder.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.dao.RemindEntity;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.telegram.KeyboardFactory;
import java.util.Optional;

@SpringBootTest(classes = {
    HandlerConfiguration.class,
    KeyboardFactory.class,
    HandlerDispatcher.class
})
class HandlerDispatcherTest {
    @Autowired
    private HandlerDispatcher testTarget;
    @MockBean
    private Dao<RemindEntity> remindEntityDao;
    @MockBean
    private Dao<BotUser> botUserDao;
    @MockBean(name = "help")
    private LoggableHandler helpHandler;
    @MockBean(name = "unknown")
    private LoggableHandler unknownMessageHandler;

    @Test
    void smoke() {
        Assertions.assertThat(testTarget).isNotNull();
    }

    @Test
    void shouldThrowIfInvalidMessage() {
        Assertions.assertThatThrownBy(() -> testTarget.handle(new Update()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Got invalid update");
    }

    @Test
    void shouldDispatchToHelpHandler() {
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.empty());
        Update update = update(BotCommand.HELP.getVal());

        final SendMessage result = testTarget.handle(update);

        Mockito.verify(helpHandler, Mockito.times(1)).handle(ArgumentMatchers.any());
    }

    @Test
    void shouldDispatchToUnknown() {
        Mockito.when(botUserDao.find(ArgumentMatchers.anyString()))
            .thenReturn(Optional.empty());
        Update update = update("Unknown command");

        final SendMessage result = testTarget.handle(update);

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
