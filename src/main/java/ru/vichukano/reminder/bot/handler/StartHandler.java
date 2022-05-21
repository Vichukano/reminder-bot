package ru.vichukano.reminder.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vichukano.reminder.bot.domain.BotCommand;

@Slf4j
@Component
public class StartHandler extends AbstractUpdateHandler {

    @Override
    public SendMessage handle(Update update) {
        log.debug("Start to hande update: {}", update);
        boolean valid = isUpdateValid(update);
        if (!valid) {
            log.debug("Got invalid update, skip");
            SendMessage out = new SendMessage();
            out.setChatId(chatId(update));
            out.setText(
                "Invalid bot command!\nPlease, type "
                    + BotCommand.HELP.getVal()
                    + " or "
                    + BotCommand.REMIND.getVal()
            );
            return out;
        }
        //TODO: implement here
        return null;
    }
}
