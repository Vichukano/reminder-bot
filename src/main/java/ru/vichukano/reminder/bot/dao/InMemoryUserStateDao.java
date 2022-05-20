package ru.vichukano.reminder.bot.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStateDao implements Dao<BotUser> {
    private final Map<String, BotUser> store;

    public InMemoryUserStateDao(Map<String, BotUser> store) {
        this.store = store;
    }

    @Override
    public Optional<BotUser> find(String id) {
        log.debug("Start to find user by id: {}", id);
        final var found = Optional.ofNullable(store.get(id));
        log.debug("Found user: {}", found);
        return found;
    }

    @Override
    public void add(BotUser user) {
        log.debug("Start to add user: {}", user);
        store.put(user.getId(), user);
        log.debug("Successfully add user");
    }

    @Override
    public void remove(BotUser botUser) {
        log.debug("Start to remove user: {}", botUser);
        store.remove(botUser.getId());
        log.debug("User removed");
    }
}
