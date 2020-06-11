package main;

import parser.CSVParser;
import parser.Parser;
import reader.Reader;

import java.util.List;

public class Main {
    public static void main(String... args) {
        String file = "src/main/resources/csv/file.csv";
        Parser parser = new CSVParser();
        System.out.println(parser.parse(file));
    }
}
