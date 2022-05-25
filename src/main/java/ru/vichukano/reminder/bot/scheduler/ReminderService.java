package ru.vichukano.reminder.bot.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.dao.RemindEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class ReminderService implements RepeatableTask {
    private final Dao<RemindEntity> dao;

    public ReminderService(Dao<RemindEntity> dao) {
        this.dao = dao;
    }

    @Override
    @Scheduled(
        initialDelayString = "${app.remind.initialDelay}",
        fixedDelayString = "${app.remind.fixedDelay}"
    )
    public void repeat() {
        searchExpiredRemindEntities();
    }

    private void searchExpiredRemindEntities() {
        log.info("Start to search messages to remind");
        try (final Stream<RemindEntity> entities = dao.findAll()) {
            final List<RemindEntity> toRemind = entities.filter(entity -> entity.getRemindDateTime().isBefore(now()))
                .collect(Collectors.toList());
            toRemind.forEach(entity -> {
                log.info("Remind! : {}", entity.getMessageText());
                dao.remove(entity);
            });
        } catch (Exception e) {
            log.error("Failed to search and remind");
        }
        log.info("Finish processing messages to remind");
    }

    LocalDateTime now() {
        return LocalDateTime.now();
    }
}
