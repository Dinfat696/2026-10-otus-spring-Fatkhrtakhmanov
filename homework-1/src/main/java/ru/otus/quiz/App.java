package ru.otus.quiz;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.quiz.config.AppConfig;
import ru.otus.quiz.service.QuizService;

public class App {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            context.getBean(QuizService.class).start();
        }
    }
}
