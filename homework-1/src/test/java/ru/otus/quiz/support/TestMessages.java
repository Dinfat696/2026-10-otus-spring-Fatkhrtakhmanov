package ru.otus.quiz.support;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

public final class TestMessages {

    private TestMessages() {
    }

    public static MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }
}
