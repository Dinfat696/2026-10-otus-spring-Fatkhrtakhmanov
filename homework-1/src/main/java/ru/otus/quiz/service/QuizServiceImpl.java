package ru.otus.quiz.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.domain.TestResult;

import java.util.Locale;

@Service
public class QuizServiceImpl implements QuizService {

    private final IOService ioService;
    private final StudentService studentService;
    private final TestingService testingService;
    private final MessageSource messageSource;

    public QuizServiceImpl(IOService ioService,
                           StudentService studentService,
                           TestingService testingService,
                           MessageSource messageSource) {
        this.ioService = ioService;
        this.studentService = studentService;
        this.testingService = testingService;
        this.messageSource = messageSource;
    }

    @Override
    public void start() {
        ioService.printLine(getMessage("greeting"));

        Student student = studentService.determineStudent();
        TestResult result = testingService.test(student);

        printResult(result);
    }

    private void printResult(TestResult result) {
        if (result.getTotalQuestions() == 0) {
            ioService.printLine(getMessage("no.questions"));
            return;
        }

        ioService.printLine(getMessage("result.header",
                result.getStudent().getFirstName(), result.getStudent().getLastName()));
        ioService.printLine(getMessage("result.score",
                result.getCorrectAnswers(), result.getTotalQuestions()));

        if (result.isPassed()) {
            ioService.printLine(getMessage("result.passed"));
        } else {
            ioService.printLine(getMessage("result.failed", result.getPassingScore()));
        }
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}
