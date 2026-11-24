package ru.otus.quiz.service;

import org.springframework.stereotype.Service;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.domain.TestResult;

@Service
public class QuizServiceImpl implements QuizService {

    private final IOService ioService;
    private final StudentService studentService;
    private final TestingService testingService;
    private final MessageService messageService;

    public QuizServiceImpl(IOService ioService,
                           StudentService studentService,
                           TestingService testingService,
                           MessageService messageService) {
        this.ioService = ioService;
        this.studentService = studentService;
        this.testingService = testingService;
        this.messageService = messageService;
    }

    @Override
    public void start() {
        ioService.printLine(messageService.getMessage("greeting"));

        Student student = studentService.determineStudent();
        TestResult result = testingService.test(student);

        printResult(result);
    }

    private void printResult(TestResult result) {
        if (result.getTotalQuestions() == 0) {
            ioService.printLine(messageService.getMessage("no.questions"));
            return;
        }

        ioService.printLine(messageService.getMessage("result.header",
                result.getStudent().getFirstName(), result.getStudent().getLastName()));
        ioService.printLine(messageService.getMessage("result.score",
                result.getCorrectAnswers(), result.getTotalQuestions()));

        if (result.isPassed()) {
            ioService.printLine(messageService.getMessage("result.passed"));
        } else {
            ioService.printLine(messageService.getMessage("result.failed", result.getPassingScore()));
        }
    }
}
