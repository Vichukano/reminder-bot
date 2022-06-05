package ru.vichukano.reminder.bot.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
public class FileSystemMessageToRemindDao implements Dao<RemindEntity> {
    private static final String SFX = ".ser";
    private final Path path;
    private final ObjectMapper mapper;

    public FileSystemMessageToRemindDao(@Value("${app.dao.remind.path}") String path,
                                        ObjectMapper mapper) {
        this.path = Path.of(path);
        this.mapper = mapper;
    }

    @Override
    public Optional<RemindEntity> find(String id) {
        log.trace("Start to find message to remind by id: {}", id);
        try (InputStream is = Files.newInputStream(path.resolve(Path.of(id + SFX)))) {
            final RemindEntity remindEntity = mapper.readValue(is, RemindEntity.class);
            log.trace("Found message to remind: {}", remindEntity);
            return Optional.ofNullable(remindEntity);
        } catch (Exception e) {
            log.error("Got exception:", e);
            return Optional.empty();
        }
    }

    @Override
    public void add(RemindEntity remindEntity) {
        try {
            log.trace("Start to add message to remind: {}", remindEntity);
            final Path path = this.path.resolve(Path.of(remindEntity.getUid() + SFX));
            final String content = mapper.writeValueAsString(remindEntity);
            Files.writeString(path, content);
            log.trace("Successfully add message to remind: {}", remindEntity);
        } catch (IOException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void remove(RemindEntity remindEntity) {
        log.trace("Start to remove message to remind: {}", remindEntity);
        try {
            final Path path = this.path.resolve(Path.of(remindEntity.getUid() + SFX));
            final boolean result = Files.deleteIfExists(path);
            log.trace("Result of delete file in path: {} is {}", path, result);
        } catch (IOException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Stream<RemindEntity> findAll() {
        log.trace("Start to find all");
        try {
            return Files.list(path)
                .filter(Objects::nonNull)
                .map(p -> find(p.getFileName().toString().replace(SFX, "")))
                .filter(Optional::isPresent)
                .map(Optional::get);
        } catch (IOException e) {
            return Stream.empty();
        }
    }
}
