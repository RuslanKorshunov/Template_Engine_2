package controller;

import entity.Table;
import parser.*;
import parser.Parser;
import reader.Reader;
import stemmer.entity.StemmerEntity;
import writer.ScsTableWriter;
import writer.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static entity.Table.Row;
import static entity.Table.Name;

public class CityController implements Controller {
    private static final String NAME;
    private static final String COLUMN_FILE;
    private static Parser parser;
    private static TableWriter writer;
    private static Reader reader;
    private static List<Name> fixColumns;

    static {
        NAME = "название";
        COLUMN_FILE = "src/main/resources/txt/columns.txt";
    }

    private Table table;

    @Override
    public Map<String, String> parseData(List<String> cities, int mode) {
        if (parser == null) {
            parser = new CityParser();
        }
        table = parser.parse(cities);
        if (mode == 1) {
            if (fixColumns == null) {
                getFixColumns();
            }
            List<String> header = table.getHeader();
            for (String column : header) {
                Name name = new Name(column);
                if (!fixColumns.contains(name)) {
                    table.deleteColumn(column);
                    table.deleteTable(column);
                }
            }
        }
        Map<String, String> result = new HashMap<>();
        for (Row row : table.getAllRows()) {
            String id = row.getId();
            String city = row.getValue(NAME);
            if (!city.equals("")) {
                result.put(city, id);
            }
        }
        return result;
    }

    @Override
    public Map<String, List<String>> getData(String id) {
        return table.get(id);
    }

    @Override
    public void writeInScs() {
        if (writer == null)
            writer = new ScsTableWriter();
        writer.write(table);
    }

    private static void getFixColumns() {
        reader = new Reader();
        List<String> columns = reader.read(COLUMN_FILE);
        fixColumns = new ArrayList<>();
        for (String column : columns) {
            Name name = new Name(column);
            fixColumns.add(name);
        }
    }
}
