package ru.otus.library.shell;

import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableRenderer {

    public String render(String[] headers, List<String[]> rows) {
        String[][] data = new String[rows.size() + 1][];
        data[0] = headers;
        for (int i = 0; i < rows.size(); i++) {
            data[i + 1] = rows.get(i);
        }
        TableModel model = new ArrayTableModel(data);
        return new TableBuilder(model)
                .addHeaderAndVerticalsBorders(BorderStyle.fancy_light)
                .addFullBorder(BorderStyle.fancy_light)
                .build()
                .render(120);
    }
}
