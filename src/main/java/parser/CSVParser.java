package parser;

import entity.Table;
import reader.Reader;

import java.util.List;

import static entity.Table.Row;

public class CSVParser extends Parser {
    private static final String COMA;
    private static final String SEMICOLON;
    private static final String NAME_COLUMN;

    static {
        COMA = ",";
        SEMICOLON = ";";
        NAME_COLUMN = "название";
    }

    private Reader reader;

    public CSVParser() {
        super();
        reader = new Reader();
    }


    @Override
    public Table parse(String fileName) {
        Table table = null;
        List<String> entities = reader.read(fileName);
        if (entities.size() > 0) {
            List<String> header = getSeparator().separate(entities.get(0), COMA);
            String[] itemsArray = new String[header.size()];
            itemsArray = header.toArray(itemsArray);
            table = new Table(itemsArray);
            for (int i = 1; i < entities.size(); i++) {
                List<String> values = getSeparator().separate(entities.get(i), COMA);
                if (values.size() != 0) {
                    String id = getTranslator().translate(values.get(0));
                    Row row = table.newRow(id);
                    for (int j = 0; j < values.size(); j++) {
                        String value = values.get(j);
                        value = value.replaceAll(SEMICOLON, COMA);
                        if (!value.equals("") && value != null) {
                            addValueInTable(table, row, value, header.get(j));
                        }
                    }
                }
            }
        }
        return table;
    }

    @Override
    public Table parse(List<String> values) {
        return null;
    }
}