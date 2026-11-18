package ru.otus.quiz;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.quiz.domain.Question;
import ru.otus.quiz.service.ConsoleService;
import ru.otus.quiz.service.QuestionService;

import java.util.List;

public class App {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("application-context.xml")) {

            QuestionService questionService = context.getBean(QuestionService.class);
            ConsoleService consoleService = context.getBean(ConsoleService.class);

            List<Question> questions = questionService.getAllQuestions();
            consoleService.printQuestions(questions);
        }
    }
}
