package parser;

import entity.Table;

import java.util.List;

public interface Parser {
    Table parse(List<String> values);
}
