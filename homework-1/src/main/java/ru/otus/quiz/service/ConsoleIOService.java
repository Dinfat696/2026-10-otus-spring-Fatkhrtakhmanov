package ru.otus.quiz.service;

import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;

/**
 * Interactive {@link IOService} backed by the Spring Shell (JLine) terminal, so it does not
 * compete with the shell for standard input.
 *
 * <p>{@link LineReader} and {@link Terminal} are resolved lazily through {@link ObjectProvider}
 * to avoid a dependency cycle: Shell -> command catalog -> command -> IOService -> LineReader -> Shell.
 */
@Service
public class ConsoleIOService implements IOService {

    private final ObjectProvider<LineReader> lineReaderProvider;
    private final ObjectProvider<Terminal> terminalProvider;

    public ConsoleIOService(ObjectProvider<LineReader> lineReaderProvider,
                            ObjectProvider<Terminal> terminalProvider) {
        this.lineReaderProvider = lineReaderProvider;
        this.terminalProvider = terminalProvider;
    }

    @Override
    public String readLine() {
        String line = lineReaderProvider.getObject().readLine();
        return line == null ? "" : line;
    }

    @Override
    public void print(String text) {
        PrintWriter out = writer();
        out.print(text);
        out.flush();
    }

    @Override
    public void printLine(String text) {
        PrintWriter out = writer();
        out.println(text);
        out.flush();
    }

    private PrintWriter writer() {
        return terminalProvider.getObject().writer();
    }
}
