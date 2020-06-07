package parser;

import entity.Table;
import entity.Table.Row;
import separator.Separator;
import separator.SeparatorInterface;
import translator.Translator;
import validator.Validator;
import validator.factory.ColumnValidatorFactory;
import validator.factory.ValueValidatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class CityParser implements Parser {
    private static final Logger logger;
    private static final String NAME_COLUMN;
    private static final String URL;
    private static final String INFOBOX_VCARD;
    private static final String TR_TAG;
    private static final String TH_TAG;
    private static final String TD_TAG;

    static {
        logger = LogManager.getLogger(CityParser.class);
        NAME_COLUMN = "название";
        URL = "https://ru.wikipedia.org/wiki/";
        INFOBOX_VCARD = "infobox vcard";
        TR_TAG = "tr";
        TH_TAG = "th";
        TD_TAG = "td";
    }

    private Validator valueValidator;
    private Validator columnValidator;
    private SeparatorInterface separator;
    private Translator translator;

    public CityParser() {
        valueValidator = new ValueValidatorFactory().create();
        columnValidator = new ColumnValidatorFactory().create();
        separator = new Separator();
        translator = new Translator();
    }

    public Table parse(List<String> cities) {
        Table table = new Table(new String[]{NAME_COLUMN});
        for (String city : cities) {
            String id = translator.translate(city);
            Row row = table.newRow(id);
            row.setValue(NAME_COLUMN, city);
            String url = URL + city;
            parse(table, row, url);
        }
        return table;
    }

    private void parse(Table table, Row row, String url) {
        try {
            Connection connection = Jsoup.connect(url);
            Elements elementsIV = connection.get().getElementsByClass(INFOBOX_VCARD);
            for (Element eIV : elementsIV) {
                Elements elementsTR = eIV.getElementsByTag(TR_TAG);
                for (Element tr : elementsTR) {
                    Elements elementsTH = tr.getElementsByTag(TH_TAG);
                    if (!elementsTH.isEmpty()) {
                        Element th = elementsTH.first();
                        String column = th.text().toLowerCase();
                        Elements elementsTD = tr.getElementsByTag(TD_TAG);
                        if (!elementsTD.isEmpty()) {
                            Element td = elementsTD.first();
                            String value = td.text();
                            value = valueValidator.validate(value);
                            column = columnValidator.validate(column);
                            List<String> values = separator.separate(value);
                            if (values.size() > 1 || table.isTable(column)) {
                                addSubtable(column, values, table, row.getId());
                            } else {
                                addColumn(column, values.get(0), table, row);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void addColumn(String column, String value, Table table, Row row) {
        String realColumn = table.addColumn(column);
        row.setValue(column, value);
    }

    private void addSubtable(String column, List<String> values, Table table, String id) {
        Table subtable = table.addTable(column);
        String realColumn = subtable.addColumn(column);
        values.forEach(value -> {
            Row row = subtable.newRow(id);
            row.setValue(realColumn, value);
        });
    }
}
