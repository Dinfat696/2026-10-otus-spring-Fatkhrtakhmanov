package ru.otus.quiz.service;

import org.springframework.stereotype.Service;
import ru.otus.quiz.domain.Student;

@Service
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;
    private final MessageService messageService;

    public StudentServiceImpl(IOService ioService, MessageService messageService) {
        this.ioService = ioService;
        this.messageService = messageService;
    }

    @Override
    public Student determineStudent() {
        String firstName = askNonEmpty("ask.first-name");
        String lastName = askNonEmpty("ask.last-name");
        return new Student(firstName, lastName);
    }

    private String askNonEmpty(String messageKey) {
        while (true) {
            ioService.printLine(messageService.getMessage(messageKey));
            String value = ioService.readLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            ioService.printLine(messageService.getMessage("error.empty-input"));
        }
    }
}
