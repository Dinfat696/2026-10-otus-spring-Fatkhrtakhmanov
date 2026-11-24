package ru.otus.quiz.support;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import ru.otus.quiz.config.QuizProperties;
import ru.otus.quiz.service.MessageService;
import ru.otus.quiz.service.ResourceMessageService;

public final class TestMessages {

    private TestMessages() {
    }

    public static MessageService messageService() {
        return new ResourceMessageService(messageSource(), quizProperties("en"));
    }

    public static MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    private static QuizProperties quizProperties(String locale) {
        QuizProperties properties = new QuizProperties();
        properties.setLocale(locale);
        return properties;
    }
}
