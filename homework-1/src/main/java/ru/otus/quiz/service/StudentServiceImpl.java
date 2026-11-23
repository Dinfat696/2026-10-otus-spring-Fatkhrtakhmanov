package ru.otus.quiz.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.quiz.domain.Student;

import java.util.Locale;

@Service
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;
    private final MessageSource messageSource;

    public StudentServiceImpl(IOService ioService, MessageSource messageSource) {
        this.ioService = ioService;
        this.messageSource = messageSource;
    }

    @Override
    public Student determineStudent() {
        String firstName = askNonEmpty("ask.first-name");
        String lastName = askNonEmpty("ask.last-name");
        return new Student(firstName, lastName);
    }

    private String askNonEmpty(String messageKey) {
        while (true) {
            ioService.printLine(getMessage(messageKey));
            String value = ioService.readLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            ioService.printLine(getMessage("error.empty-input"));
        }
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}
