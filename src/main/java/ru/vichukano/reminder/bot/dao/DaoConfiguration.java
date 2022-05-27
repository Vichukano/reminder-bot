package ru.vichukano.reminder.bot.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vichukano.reminder.bot.domain.BotUser;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DaoConfiguration {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public Map<String, BotUser> store() {
        return new ConcurrentHashMap<>();
    }

}
