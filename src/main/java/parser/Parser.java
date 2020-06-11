package parser;

import entity.Table;
import separator.Separator;
import separator.SeparatorInterface;
import translator.Translator;
import validator.Validator;
import validator.factory.ColumnValidatorFactory;
import validator.factory.ValueValidatorFactory;

import java.util.List;

import static entity.Table.Row;

public abstract class Parser {
    private static final String COMA;

    static {
        COMA = ",";
    }

    private Validator valueValidator;
    private Validator columnValidator;
    private SeparatorInterface separator;
    private Translator translator;

    public Parser() {
        valueValidator = new ValueValidatorFactory().create();
        columnValidator = new ColumnValidatorFactory().create();
        separator = new Separator();
        translator = new Translator();
    }

    public abstract Table parse(String fileName);

    public abstract Table parse(List<String> values);

    void addValueInTable(Table table, Row row, String value, String column) {
        value = valueValidator.validate(value);
        column = columnValidator.validate(column);
        List<String> values = separator.separate(value, COMA);
        if (values.size() > 1 || table.isTable(column)) {
            addSubtable(column, values, table, row.getId());
        } else {
            addColumn(column, values.get(0), table, row);
        }
    }

    void addColumn(String column, String value, Table table, Table.Row row) {
        String realColumn = table.addColumn(column);
        row.setValue(column, value);
    }

    void addSubtable(String column, List<String> values, Table table, String id) {
        Table subtable = table.addTable(column);
        String realColumn = subtable.addColumn(column);
        values.forEach(value -> {
            Table.Row row = subtable.newRow(id);
            row.setValue(realColumn, value);
        });
    }

    public Validator getValueValidator() {
        return valueValidator;
    }

    public Validator getColumnValidator() {
        return columnValidator;
    }

    public SeparatorInterface getSeparator() {
        return separator;
    }

    public Translator getTranslator() {
        return translator;
    }
}
