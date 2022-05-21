package ru.vichukano.reminder.bot.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Component
public class FileSystemMessageToRemindDao implements Dao<MessageToRemind> {
    private static final String SFX = ".ser";
    private final Path path;
    private final ObjectMapper mapper;

    public FileSystemMessageToRemindDao(@Value("${app.dao.remind.path}") String path,
                                        ObjectMapper mapper) {
        this.path = Path.of(path);
        this.mapper = mapper;
    }

    @Override
    public Optional<MessageToRemind> find(String id) {
        log.trace("Start to find message to remind by id: {}", id);
        try (InputStream is = Files.newInputStream(path.resolve(Path.of(id + SFX)))) {
            final MessageToRemind messageToRemind = mapper.readValue(is, MessageToRemind.class);
            log.trace("Found message to remind: {}", messageToRemind);
            return Optional.ofNullable(messageToRemind);
        } catch (Exception e) {
            log.error("Got exception:", e);
            return Optional.empty();
        }
    }

    @Override
    public void add(MessageToRemind messageToRemind) {
        try {
            log.trace("Start to add message to remind: {}", messageToRemind);
            final Path path = this.path.resolve(Path.of(messageToRemind.getReminderId() + SFX));
            final String content = mapper.writeValueAsString(messageToRemind);
            Files.writeString(path, content);
            log.trace("Successfully add message to remind: {}", messageToRemind);
        } catch (IOException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void remove(MessageToRemind messageToRemind) {
        log.trace("Start to remove message to remind: {}", messageToRemind);
        try {
            final Path path = this.path.resolve(Path.of(messageToRemind.getReminderId() + SFX));
            final boolean result = Files.deleteIfExists(path);
            log.trace("Result of delete file in path: {} is {}", path, result);
        } catch (IOException e) {
            throw new DaoException(e.getMessage());
        }
    }
}
