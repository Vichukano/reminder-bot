package ru.vichukano.reminder.bot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vichukano.reminder.bot.handler.Handler;
import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ReminderBot extends TelegramLongPollingBot {
    private final String botName;
    private final String botToken;
    private final Handler<Update, SendMessage> handler;

    public ReminderBot(String botName, String botToken, Handler<Update, SendMessage> handler) {
        this.botName = botName;
        this.botToken = botToken;
        this.handler = handler;
    }

    @PostConstruct
    public void init() {
        log.info("Successfully init [{}]", botName);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            final Integer updateId = update.getUpdateId();
            log.info("Receive update with id: {}", updateId);
            final SendMessage answer = handler.handle(update);
            execute(answer);
            log.info("Finish process update with id: {}, answer message chat id: {}", updateId, answer.getChatId());
        } catch (Exception e) {
            log.error("Failed to process update: {}, cause: ", update, e);
        }
    }
}
