package ru.vichukano.reminder.bot.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vichukano.reminder.bot.domain.BotUser;
import ru.vichukano.reminder.bot.domain.UserState;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryUserStateDaoTest {
    private final InMemoryUserStateDao testTarget
        = new InMemoryUserStateDao(new ConcurrentHashMap<>());

    @Test
    void shouldAddAndFind() {
        final var id = UUID.randomUUID().toString();
        final var user = BotUser.builder()
            .id(id)
            .state(UserState.START)
            .build();

        testTarget.add(user);
        final Optional<BotUser> botUserOpt = testTarget.find(id);

        Assertions.assertThat(botUserOpt).isNotEmpty();
        Assertions.assertThat(botUserOpt.get()).isEqualTo(user);
    }

    @Test
    void shouldRemove() {
        final var id = UUID.randomUUID().toString();
        final var user = BotUser.builder()
            .id(id)
            .state(UserState.START)
            .build();

        testTarget.add(user);
        testTarget.remove(user);
        final Optional<BotUser> botUserOpt = testTarget.find(id);

        Assertions.assertThat(botUserOpt).isEmpty();
    }

    @Test
    void shouldFindAll() {
        final var one = BotUser.builder()
            .id(UUID.randomUUID().toString())
            .state(UserState.START)
            .build();
        final var two = BotUser.builder()
            .id(UUID.randomUUID().toString())
            .state(UserState.START)
            .build();
        final var three = BotUser.builder()
            .id(UUID.randomUUID().toString())
            .state(UserState.START)
            .build();

        testTarget.add(one);
        testTarget.add(two);
        testTarget.add(three);

        Assertions.assertThat(testTarget.findAll().count()).isEqualTo(3L);
    }
}
