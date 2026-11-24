package ru.otus.quiz.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.quiz.config.QuizProperties;

import java.util.Locale;

/**
 * Resolves localized messages using the {@link MessageSource} autoconfigured by Spring Boot
 * and the application locale defined in {@link QuizProperties}.
 */
@Service
public class ResourceMessageService implements MessageService {

    private final MessageSource messageSource;
    private final Locale locale;

    public ResourceMessageService(MessageSource messageSource, QuizProperties quizProperties) {
        this.messageSource = messageSource;
        this.locale = quizProperties.getLocale();
    }

    @Override
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, locale);
    }
}
