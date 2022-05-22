package ru.vichukano.reminder.bot.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeyboardFactory implements Factory<InlineKeyboardMarkup> {
    private static final LocalTime TIME = LocalTime.of(12, 0, 0, 0);
    private static final Collection<LocalTime> TIMES = List.of(
        TIME.plusHours(1),
        TIME.plusHours(2),
        TIME.plusHours(3),
        TIME.plusHours(4),
        TIME.plusHours(5),
        TIME.plusHours(6),
        TIME.plusHours(7),
        TIME.plusHours(8),
        TIME.plusHours(9),
        TIME.plusHours(10),
        TIME.plusHours(11),
        TIME.plusHours(12)
    );

    @Override
    public InlineKeyboardMarkup construct(Item item) {
        switch (item) {
            case DATE:
                return dateKeyboard();
            case TIME:
                return timeKeyboard();
            default:
                throw new IllegalArgumentException("Can't construct for: " + item);
        }
    }

    private InlineKeyboardMarkup dateKeyboard() {
        var markup = new InlineKeyboardMarkup();
        var now = LocalDate.now();
        List<List<InlineKeyboardButton>> buttons = Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
            .map(i -> {
                var date = now.plusDays(i);
                var row = new ArrayList<InlineKeyboardButton>();
                row.add(
                    InlineKeyboardButton.builder()
                        .text(date.toString())
                        .callbackData(date.toString())
                        .build()
                );
                return row;
            })
            .collect(Collectors.toList());
        markup.setKeyboard(buttons);
        return markup;
    }

    private InlineKeyboardMarkup timeKeyboard() {
        var markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = TIMES.stream()
            .map(i -> {
                var row = new ArrayList<InlineKeyboardButton>();
                row.add(
                    InlineKeyboardButton.builder()
                        .text(i.toString())
                        .callbackData(i.toString())
                        .build()
                );
                return row;
            })
            .collect(Collectors.toList());
        markup.setKeyboard(buttons);
        return markup;
    }
}
