package ru.otus.quiz.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.quiz.domain.Answer;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.service.IOService;
import ru.otus.quiz.service.MessageService;
import ru.otus.quiz.service.QuestionService;
import ru.otus.quiz.service.QuizService;
import ru.otus.quiz.service.QuizSession;
import ru.otus.quiz.service.StudentService;
import ru.otus.quiz.support.TestMessages;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizCommandsTest {

    private static final Student STUDENT = new Student("John", "Doe");

    @Mock
    private QuizService quizService;

    @Mock
    private StudentService studentService;

    @Mock
    private QuestionService questionService;

    @Mock
    private IOService ioService;

    private final MessageService messageService = TestMessages.messageService();

    private QuizSession quizSession;
    private QuizCommands quizCommands;

    @BeforeEach
    void setUp() {
        quizSession = new QuizSession();
        quizCommands = new QuizCommands(
                quizService, studentService, questionService, quizSession, ioService, messageService);
    }

    @Test
    void helloReturnsGreeting() {
        assertThat(quizCommands.hello()).isEqualTo("Welcome to the student testing application!");
    }

    @Test
    void loginStoresStudentInSession() {
        when(studentService.determineStudent()).thenReturn(STUDENT);

        String message = quizCommands.login();

        assertThat(message).isEqualTo("Logged in as John Doe");
        assertThat(quizSession.getStudent()).contains(STUDENT);
    }

    @Test
    void whoAmIShowsCurrentStudent() {
        quizSession.login(STUDENT);

        assertThat(quizCommands.whoAmI()).isEqualTo("Current student: John Doe");
    }

    @Test
    void whoAmIShowsMessageWhenNotLoggedIn() {
        assertThat(quizCommands.whoAmI()).isEqualTo("You are not logged in. Use 'login' first.");
    }

    @Test
    void logoutClearsSession() {
        quizSession.login(STUDENT);

        String message = quizCommands.logout();

        assertThat(message).isEqualTo("You are logged out.");
        assertThat(quizSession.getStudent()).isEmpty();
    }

    @Test
    void startRunsQuizForLoggedInStudent() {
        quizSession.login(STUDENT);

        quizCommands.start();

        verify(quizService).run(STUDENT);
        verifyNoInteractions(ioService);
    }

    @Test
    void startAsksToLoginFirstWhenNoStudent() {
        quizCommands.start();

        verify(ioService).printLine("You are not logged in. Use 'login' first.");
        verifyNoInteractions(quizService);
    }

    @Test
    void quizRunsFullFlow() {
        quizCommands.quiz();

        verify(quizService).start();
    }

    @Test
    void questionsListsAllQuestions() {
        when(questionService.getAllQuestions()).thenReturn(List.of(
                new Question("Is the Sun a star?", List.of(new Answer("Yes", true), new Answer("No", false))),
                new Question("What is 2 + 2?", List.of(new Answer("4", true)))));

        String result = quizCommands.questions();

        assertThat(result)
                .contains("Available questions:")
                .contains("1. Is the Sun a star? (options: 2)")
                .contains("2. What is 2 + 2? (options: 1)");
    }

    @Test
    void questionsReportsWhenThereAreNoQuestions() {
        when(questionService.getAllQuestions()).thenReturn(List.of());

        assertThat(quizCommands.questions()).isEqualTo("No questions available.");
    }
}
