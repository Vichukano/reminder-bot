package ru.vichukano.reminder.bot.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

class FileSystemMessageToRemindDaoTest {
    @TempDir
    private File temp;
    private FileSystemMessageToRemindDao testTarget;

    @BeforeEach
    void setUp() {
        testTarget = new FileSystemMessageToRemindDao(
            temp.getPath(),
            new ObjectMapper().registerModule(new JavaTimeModule())
        );
    }

    @Test
    void shouldSaveAndFind() {
        final var uid = UUID.randomUUID().toString();
        final var entity = RemindEntity.builder()
            .uid(uid)
            .reminderId(UUID.randomUUID().toString())
            .remindDateTime(LocalDateTime.now())
            .messageText("remind me something")
            .build();

        testTarget.add(entity);

        final Optional<RemindEntity> resultOpt = testTarget.find(uid);
        Assertions.assertThat(resultOpt).isNotEmpty();
        Assertions.assertThat(resultOpt.get()).isEqualTo(entity);
    }

    @Test
    void shouldRemove() {
        final var uid = UUID.randomUUID().toString();
        final var entity = RemindEntity.builder()
            .uid(uid)
            .reminderId(UUID.randomUUID().toString())
            .remindDateTime(LocalDateTime.now())
            .messageText("remind me something")
            .build();
        testTarget.add(entity);
        final Optional<RemindEntity> resultOpt = testTarget.find(uid);
        Assertions.assertThat(resultOpt).isNotEmpty();
        testTarget.remove(entity);
        final Optional<RemindEntity> resultOptSecond = testTarget.find(uid);
        Assertions.assertThat(resultOptSecond).isEmpty();
    }

    @Test
    void shouldReturnStreamOfFiles() {
        final var one = RemindEntity.builder()
            .uid(UUID.randomUUID().toString())
            .reminderId(UUID.randomUUID().toString())
            .remindDateTime(LocalDateTime.now())
            .messageText("remind me something")
            .build();
        final var two = RemindEntity.builder()
            .uid(UUID.randomUUID().toString())
            .reminderId(UUID.randomUUID().toString())
            .remindDateTime(LocalDateTime.now())
            .messageText("remind me something")
            .build();
        final var three = RemindEntity.builder()
            .uid(UUID.randomUUID().toString())
            .reminderId(UUID.randomUUID().toString())
            .remindDateTime(LocalDateTime.now())
            .messageText("remind me something")
            .build();

        testTarget.add(one);
        testTarget.add(two);
        testTarget.add(three);

        try (final Stream<RemindEntity> all = testTarget.findAll()) {
            Assertions.assertThat(all.count()).isEqualTo(3L);
        }
    }
}