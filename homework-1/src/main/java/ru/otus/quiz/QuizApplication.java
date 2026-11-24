package ru.otus.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.quiz.service.QuizService;

@SpringBootApplication
@ConfigurationPropertiesScan
public class QuizApplication {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context =
                     SpringApplication.run(QuizApplication.class, args)) {
            context.getBean(QuizService.class).start();
        }
    }
}
