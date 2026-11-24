package ru.otus.quiz.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.domain.Student;
import ru.otus.quiz.service.IOService;
import ru.otus.quiz.service.MessageService;
import ru.otus.quiz.service.QuestionService;
import ru.otus.quiz.service.QuizService;
import ru.otus.quiz.service.QuizSession;
import ru.otus.quiz.service.StudentService;

import java.util.List;

@ShellComponent
public class QuizCommands {

    private final QuizService quizService;
    private final StudentService studentService;
    private final QuestionService questionService;
    private final QuizSession quizSession;
    private final IOService ioService;
    private final MessageService messageService;

    public QuizCommands(QuizService quizService,
                        StudentService studentService,
                        QuestionService questionService,
                        QuizSession quizSession,
                        IOService ioService,
                        MessageService messageService) {
        this.quizService = quizService;
        this.studentService = studentService;
        this.questionService = questionService;
        this.quizSession = quizSession;
        this.ioService = ioService;
        this.messageService = messageService;
    }

    @ShellMethod(key = "hello", value = "Print a greeting")
    public String hello() {
        return messageService.getMessage("greeting");
    }

    @ShellMethod(key = "login", value = "Log in as a student (prompts for first and last name)")
    public String login() {
        Student student = studentService.determineStudent();
        quizSession.login(student);
        return messageService.getMessage("shell.logged-in", student.getFirstName(), student.getLastName());
    }

    @ShellMethod(key = "logout", value = "Log out the current student")
    public String logout() {
        quizSession.logout();
        return messageService.getMessage("shell.logged-out");
    }

    @ShellMethod(key = "whoami", value = "Show the current student")
    public String whoAmI() {
        return quizSession.getStudent()
                .map(student -> messageService.getMessage(
                        "shell.whoami", student.getFirstName(), student.getLastName()))
                .orElseGet(() -> messageService.getMessage("shell.not-logged-in"));
    }

    @ShellMethod(key = "start", value = "Start the quiz for the logged-in student")
    public void start() {
        quizSession.getStudent().ifPresentOrElse(
                quizService::run,
                () -> ioService.printLine(messageService.getMessage("shell.not-logged-in")));
    }

    @ShellMethod(key = "quiz", value = "Run the full quiz: greeting, login and testing")
    public void quiz() {
        quizService.start();
    }

    @ShellMethod(key = "questions", value = "List the available questions")
    public String questions() {
        List<Question> questions = questionService.getAllQuestions();
        if (questions.isEmpty()) {
            return messageService.getMessage("shell.questions.empty");
        }

        StringBuilder result = new StringBuilder(messageService.getMessage("shell.questions.header"));
        String separator = System.lineSeparator();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            result.append(separator)
                    .append(messageService.getMessage("shell.questions.line",
                            i + 1, question.getText(), question.getAnswers().size()));
        }
        return result.toString();
    }
}
