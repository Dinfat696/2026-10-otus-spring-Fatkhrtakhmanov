package ru.otus.quiz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.domain.TestResult;
import ru.otus.quiz.support.TestMessages;

import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    private static final Student STUDENT = new Student("John", "Doe");

    @Mock
    private IOService ioService;

    @Mock
    private StudentService studentService;

    @Mock
    private TestingService testingService;

    private final MessageSource messageSource = TestMessages.messageSource();
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizServiceImpl(ioService, studentService, testingService, messageSource);
    }

    @Test
    void asksStudentAndPrintsPassedResult() {
        when(studentService.determineStudent()).thenReturn(STUDENT);
        when(testingService.test(STUDENT)).thenReturn(new TestResult(STUDENT, 2, 2, 2));

        quizService.start();

        verify(ioService).printLine("Welcome to the student testing application!");
        verify(ioService).printLine("\nTest results for John Doe");
        verify(ioService).printLine("Correct answers: 2 of 2");
        verify(ioService).printLine("Congratulations! You passed the test.");
        verify(studentService).determineStudent();
        verify(testingService).test(STUDENT);
    }

    @Test
    void printsFailedResultWithRequiredScore() {
        when(studentService.determineStudent()).thenReturn(STUDENT);
        when(testingService.test(STUDENT)).thenReturn(new TestResult(STUDENT, 1, 5, 4));

        quizService.start();

        verify(ioService).printLine("Correct answers: 1 of 5");
        verify(ioService).printLine("Sorry, you failed the test. Required correct answers: 4");
        verify(ioService, never()).printLine("Congratulations! You passed the test.");
    }

    @Test
    void printsMessageWhenThereAreNoQuestions() {
        when(studentService.determineStudent()).thenReturn(STUDENT);
        when(testingService.test(STUDENT)).thenReturn(new TestResult(STUDENT, 0, 0, 4));

        quizService.start();

        verify(ioService).printLine("No questions available for testing.");
        verify(ioService, never()).printLine(startsWith("Correct answers:"));
    }
}
