package ru.otus.quiz.service;

import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.Scanner;

@Service
public class ConsoleIOService implements IOService {

    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream out = System.out;

    @Override
    public String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine() : "";
    }

    @Override
    public void print(String text) {
        out.print(text);
        out.flush();
    }

    @Override
    public void printLine(String text) {
        out.println(text);
        out.flush();
    }
}
