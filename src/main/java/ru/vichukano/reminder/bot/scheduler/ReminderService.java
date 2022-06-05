package ru.vichukano.reminder.bot.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vichukano.reminder.bot.dao.Dao;
import ru.vichukano.reminder.bot.dao.RemindEntity;
import ru.vichukano.reminder.bot.web.NotificationInfo;
import ru.vichukano.reminder.bot.web.Sender;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class ReminderService implements RepeatableTask {
    private final String sourceName;
    private final Dao<RemindEntity> dao;
    private final Sender<NotificationInfo> sender;

    public ReminderService(@Value("${app.bot.name}") String sourceName,
                           Dao<RemindEntity> dao,
                           Sender<NotificationInfo> sender) {
        this.sourceName = sourceName;
        this.dao = dao;
        this.sender = sender;
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
        log.debug("Start to search messages to remind");
        try (final Stream<RemindEntity> entities = dao.findAll()) {
            final List<RemindEntity> toRemind = entities.filter(entity -> entity.getRemindDateTime().isBefore(now()))
                .collect(Collectors.toList());
            toRemind.forEach(entity -> {
                log.trace("Remind! : {}", entity.getMessageText());
                final var notification = NotificationInfo.Notification.builder()
                    .notifiedUserId(entity.getReminderId())
                    .notificationText(entity.getMessageText())
                    .build();
                final var notificationInfo = NotificationInfo.builder()
                    .uuid(UUID.randomUUID().toString())
                    .sourceName(sourceName)
                    .requestDateTime(LocalDateTime.now().toString())
                    .notifications(List.of(notification))
                    .build();
                sender.send(notificationInfo);
                dao.remove(entity);
            });
        } catch (Exception e) {
            log.error("Failed to search and remind");
        }
        log.debug("Finish processing messages to remind");
    }

    LocalDateTime now() {
        return LocalDateTime.now();
    }
}
