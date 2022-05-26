package ru.vichukano.reminder.bot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class NotificationSender implements Sender<NotificationInfo> {
    private final String url;
    private final String path;
    private final RestTemplate restTemplate;

    public NotificationSender(@Value("${app.notification.url}") String url,
                              @Value("${app.notification.path}") String path,
                              RestTemplate restTemplate) {
        this.url = url;
        this.path = path;
        this.restTemplate = restTemplate;
    }

    @Override
    public void send(NotificationInfo message) {
        log.debug("Start to send notification: {}", message);
        final ResponseEntity<Object> responseEntity = restTemplate.postForEntity(url + path, message, Object.class);
        log.debug("Response code: {}", responseEntity.getStatusCode());
    }
}
