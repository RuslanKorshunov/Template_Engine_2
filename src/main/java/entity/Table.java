package entity;

import stemmer.Stemmer;
import stemmer.TextStemmer;
import stemmer.entity.StemmerEntity;

import java.util.*;
import java.util.stream.Collectors;

public class Table {
    private long counter;
    private Map<Name, String> header;
    private List<String> headerString;
    private List<Row> rows;
    private Map<Name, Table> tables;

    public Table() {
        counter = 0;
        header = new HashMap<>();
        headerString = new ArrayList<>();
        rows = new ArrayList<>();
        tables = new HashMap<>();
    }

    public Table(String... header) {
        counter = 0;
        this.header = new HashMap<>();
        headerString = new ArrayList<>();
        rows = new ArrayList<>();
        tables = new HashMap<>();
        for (String column : header) {
            addColumn(column);
        }
    }

    public String addColumn(String columnName) {
        String result = getColumn(columnName);
        if (result == null) {
            Name name = new Name(columnName);
            header.put(name, columnName);
            headerString.add(columnName);
            rows.forEach(row -> row.columns.put(columnName, ""));
            result = columnName;
        }
        return result;
    }

    public void deleteColumn(String columnName) {
        String result = getColumn(columnName);
        if (result != null) {
            Name name = new Name(columnName);
            header.remove(name);
            headerString.remove(result);
            rows.forEach(row -> row.columns.remove(result));
        }
    }

    public String getColumn(String columnName) {
        String result = null;
        if (headerString.contains(columnName)) {
            result = columnName;
        } else {
            Name name = new Name(columnName);
            if (header.containsKey(name)) {
                result = header.get(name);
            }
        }
        return result;
    }

    public Row newRow(String id) {
        Row row = new Row(id, headerString, this);
        rows.add(row);
        return row;
    }

    public Table addTable(String tableName) {
        Name name = new Name(tableName);
        Table table = tables.get(name);
        if (table == null) {
            table = new Table();
            table.addColumn(tableName);
            tables.put(name, table);
            String columnName = getColumn(tableName);
            if (columnName != null) {
                for (Row r : rows) {
                    String id = r.id;
                    String value = r.columns.get(columnName);
                    if (!value.equals("")) {
                        Row row = table.newRow(id);
                        row.setValue(columnName, value);
                    }
                }
                deleteColumn(columnName);
            }
        }
        return table;
    }

    public void deleteTable(String tableName) {
        Name name = new Name(tableName);
        if (tables.containsKey(name)) {
            tables.remove(name);
            header.remove(name);
            headerString.remove(tableName);
        }
    }

    public List<String> getHeader() {
        List<String> header = new ArrayList<>(headerString);
        for (Map.Entry<Name, Table> entry : tables.entrySet()) {
            Name name = entry.getKey();
            header.add(name.value);
        }
        return header;
    }

    public Map<String, List<String>> get(String id) {
        Map<String, List<String>> result = new HashMap<>();
        Optional<Row> optionalRow = rows.stream().filter(r -> r.getId().equals(id)).findFirst();
        if (optionalRow.isPresent()) {
            Row row = optionalRow.get();
            for (String column : headerString) {
                List<String> values = new ArrayList<>();
                values.add(row.getValue(column));
                result.put(column, values);
            }
            for (Map.Entry<Name, Table> entry : tables.entrySet()) {
                Name key = entry.getKey();
                Table value = entry.getValue();
                List<String> values = value.getAllRows().stream().
                        filter(r ->
                                r.getId().equals(id)).
                        map(r ->
                                r.getValue(key.value)
                        ).collect(Collectors.toList());
                result.put(key.value, values);
            }
        }
        return result;
    }

    public List<Row> getAllRows() {
        return rows.stream().collect(Collectors.toList());
    }

    public boolean isTable(String name) {
        return tables.containsKey(new Name(name));
    }

    @Override
    public String toString() {
        String result = "===========================================================\n";
        result += toStringTable(this);
        if (tables.size() != 0) {
            result += "\n============================" + "Подтаблицы" + "============================\n";
            for (Map.Entry<Name, Table> entry : tables.entrySet()) {
                Name name = entry.getKey();
                Table table = entry.getValue();
                result += "\n============================" + name.value + "============================\n";
                result += toStringTable(table);
            }
        }
        return result;
    }

    private String toStringTable(Table table) {
        String result = "\n";
        for (Row row : table.rows) {
            result += row + "\n";
        }
        result += "\n";
        return result;
    }

    public static final class Row {
        private String id;
        private Map<String, String> columns;
        private Table table;

        private Row(String id, List<String> header, Table table) {
            this.id = id;
            columns = new HashMap<>();
            header.forEach(column -> {
                columns.put(column, "");
            });
            this.table = table;
        }

        public String getId() {
            return id;
        }

        public boolean setValue(String column, String value) {
            boolean result = false;
            if (columns.containsKey(column)) {
                columns.put(column, value);
                result = true;
            }
            return result;
        }

        public String getValue(String column) {
            return columns.get(column);
        }

        @Override
        public String toString() {
            String result = "id:" + id + "\n";
            for (String column : table.headerString) {
                result += column + ":" + columns.get(column) + "\n";
            }
            return result;
        }
    }

    public static final class Name {
        private String value;
        private List<StemmerEntity> stemmerEntities;

        public Name(String value) {
            this.value = value;
            this.stemmerEntities = !value.equals("") && value != null ?
                    generateStemmerEntities(value) : new ArrayList<>();
        }

        private List<StemmerEntity> generateStemmerEntities(String text) {
            Stemmer stemmer = new TextStemmer();
            return stemmer.stem(text, false);
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return stemmerEntities.stream().mapToInt(StemmerEntity::hashCode).sum();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Name column = (Name) o;
            List<StemmerEntity> entities = column.stemmerEntities;
            boolean equal = true;
            int i = 0;
            if (entities.size() == this.stemmerEntities.size()) {
                while (i < column.stemmerEntities.size() && equal) {
                    StemmerEntity entityA = column.stemmerEntities.get(i);
                    if (stemmerEntities.contains(entityA)) {
                        i++;
                    } else {
                        equal = false;
                    }
                }
            } else {
                equal = false;
            }
            return equal;
        }
    }
}
