package ru.otus.quiz;

import ru.otus.quiz.service.IOService;

import java.util.ArrayDeque;
import java.util.Deque;

public class ScriptedIOService implements IOService {

    private final Deque<String> inputs = new ArrayDeque<>();
    private final StringBuilder output = new StringBuilder();

    public void provide(String... inputLines) {
        for (String input : inputLines) {
            inputs.addLast(input);
        }
    }

    public void reset() {
        inputs.clear();
        output.setLength(0);
    }

    public String getOutput() {
        return output.toString();
    }

    @Override
    public String readLine() {
        String input = inputs.pollFirst();
        if (input == null) {
            throw new IllegalStateException("No scripted input left for the quiz");
        }
        return input;
    }

    @Override
    public void print(String text) {
        output.append(text);
    }

    @Override
    public void printLine(String text) {
        output.append(text).append(System.lineSeparator());
    }
}
