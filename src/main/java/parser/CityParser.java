package parser;

import entity.Table;
import entity.Table.Row;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class CityParser extends Parser {
    private static final Logger logger;
    private static final String NAME_COLUMN;
    private static final String URL;
    private static final String INFOBOX_VCARD;
    private static final String TR_TAG;
    private static final String TH_TAG;
    private static final String TD_TAG;
    private static final String COMA;

    static {
        logger = LogManager.getLogger(CityParser.class);
        NAME_COLUMN = "название";
        URL = "https://ru.wikipedia.org/wiki/";
        INFOBOX_VCARD = "infobox vcard";
        TR_TAG = "tr";
        TH_TAG = "th";
        TD_TAG = "td";
        COMA = ",";
    }

    public CityParser() {
        super();
    }

    @Override
    public Table parse(String fileName) {
        return null;
    }

    public Table parse(List<String> cities) {
        Table table = new Table(new String[]{NAME_COLUMN});
        for (String city : cities) {
            String id = getTranslator().translate(city);
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
                            addValueInTable(table, row, value, column);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
