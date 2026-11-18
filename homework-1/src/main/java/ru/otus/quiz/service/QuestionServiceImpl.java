package ru.otus.quiz.service;

import ru.otus.quiz.dao.QuestionDao;
import ru.otus.quiz.domain.Question;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionDao.getAllQuestions();
    }
}
