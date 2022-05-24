package ru.vichukano.reminder.bot.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.vichukano.reminder.bot.dao.BotUser;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.dao.MessageToRemind;
import ru.vichukano.reminder.bot.telegram.Factory;

@Configuration
public class HandlerConfiguration {

    @Bean(name = "help")
    Handler<MessageContext, SendMessage> helpHandler() {
        return new LoggableHandler(new HelpHandler());
    }

    @Bean(name = "start")
    Handler<MessageContext, SendMessage> startRemindHandler(Dao<BotUser> userDao) {
        return new LoggableHandler(new StartRemindHandler(userDao));
    }

    @Bean(name = "message")
    Handler<MessageContext, SendMessage> inputMessageHandler(Dao<BotUser> userDao,
                                                             Factory<InlineKeyboardMarkup> factory) {
        return new LoggableHandler(new InputMessageHandler(userDao, factory));
    }

    @Bean(name = "date")
    Handler<MessageContext, SendMessage> inputDateHandler(Dao<BotUser> userDao,
                                                          Factory<InlineKeyboardMarkup> factory) {
        return new LoggableHandler(new InputDateHandler(userDao, factory));
    }

    @Bean(name = "time")
    Handler<MessageContext, SendMessage> inputTimeHandler(Dao<BotUser> userDao) {
        return new LoggableHandler(new InputTimeHandler(userDao));
    }

    @Bean(name = "confirm")
    Handler<MessageContext, SendMessage> confirmMessageHandler(Dao<BotUser> userDao,
                                                               Dao<MessageToRemind> remindDao) {
        return new LoggableHandler(new ConfirmMessageHandler(userDao, remindDao));
    }

    @Bean(name = "unknown")
    Handler<MessageContext, SendMessage> unknownMessageHandler() {
        return new LoggableHandler(new UnknownMessageHandler());
    }

}
