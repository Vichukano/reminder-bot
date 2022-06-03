package ru.vichukano.reminder.bot.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.vichukano.reminder.bot.domain.BotUser;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
public class InMemoryUserStateDao implements Dao<BotUser> {
    private final Map<String, BotUser> store;

    public InMemoryUserStateDao(Map<String, BotUser> store) {
        this.store = store;
    }

    @Override
    public Optional<BotUser> find(String id) {
        log.trace("Start to find user by id: {}", id);
        final var found = Optional.ofNullable(store.get(id));
        log.trace("Found user: {}", found);
        return found;
    }

    @Override
    public void add(BotUser user) {
        log.trace("Start to add user: {}", user);
        store.put(user.getId(), user);
        log.trace("Successfully add user");
    }

    @Override
    public void remove(BotUser botUser) {
        log.trace("Start to remove user: {}", botUser);
        store.remove(botUser.getId());
        log.trace("User removed");
    }

    @Override
    public Stream<BotUser> findAll() {
        log.trace("Start to find all");
        return store.values().stream();
    }
}
