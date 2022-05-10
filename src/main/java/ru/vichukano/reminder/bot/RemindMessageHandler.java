package ru.vichukano.reminder.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vichukano.reminder.bot.domain.BotCommand;
import ru.vichukano.reminder.bot.domain.MessageToReminde;
import ru.vichukano.reminder.bot.domain.TelegramMessage;
import java.time.LocalDate;

@Slf4j
@Component
public class RemindMessageHandler implements Handler<Update, SendMessage> {

    @Override
    public SendMessage handle(Update update) {
        log.debug("Start to handle update: {}", update);
        final TelegramMessage telegramMessage = new TelegramMessage(update);
        final String text = telegramMessage.getText();
        SendMessage out = new SendMessage();
        out.setChatId(telegramMessage.getChatId());
        if (text.contains(BotCommand.HELP.getVal())) {
            //todo: add help message
            out.setText("You type help");
        } else if (text.contains(BotCommand.REMIND.getVal())) {
            out.setText("I remind your message and will notify you");
            final String remindMessageWithDate = text.split(BotCommand.REMIND.getVal())[1];
            final String remindMessage = remindMessageWithDate.split(":")[0].strip();
            final String dateString = remindMessageWithDate.split(":")[1].strip();
            final MessageToReminde toRemind = MessageToReminde.builder()
                .reminderId(telegramMessage.getUserId())
                .messageText(remindMessage)
                .remindDate(LocalDate.parse(dateString))
                .build();
            //todo: reminder logic
            log.debug("Message to remind: {}", toRemind);
        } else {
            out.setText("Unknown command. Send "
                + BotCommand.HELP.getVal()
                + " or "
                + BotCommand.REMIND.getVal()
            );
        }
        log.debug("Out message: {}", out);
        return out;
    }

}
