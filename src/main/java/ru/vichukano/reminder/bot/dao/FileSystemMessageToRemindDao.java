package ru.vichukano.reminder.bot.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Component
public class FileSystemMessageToRemindDao implements Dao<MessageToRemind> {
    private final Path path;

    public FileSystemMessageToRemindDao(@Value("${app.dao.remind.path}") String path) {
        this.path = Path.of(path);
    }

    @Override
    public Optional<MessageToRemind> find(String id) {
        return Optional.empty();
    }

    @Override
    public void add(MessageToRemind messageToRemind) {

    }

    @Override
    public void remove(MessageToRemind messageToRemind) {

    }
}
