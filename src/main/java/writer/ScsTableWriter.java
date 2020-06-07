package writer;

import entity.Table;
import translator.LanguageTranslator;
import translator.Translator;
import translator.TranslatorInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static entity.Table.Row;

public class ScsTableWriter extends TableWriter {
    private static final Logger logger;
    private static final String CITY;
    private static final String CONCEPT_CITY;
    private static final String SCS;
    private static final String EN;
    private static final String NREL;
    private static final String NAME_COLUMN;
    private static final String NREL_MAIN_IDTF;

    static {
        logger = LogManager.getLogger(ScsTableWriter.class);
        CITY = "city";
        CONCEPT_CITY = "concept_city";
        SCS = "scs";
        EN = "en";
        NREL = "nrel";
        NAME_COLUMN = "название";
        NREL_MAIN_IDTF = "nrel_main_idtf";
    }

    public boolean write(Table table) {
        boolean result = true;
        String directoryName = CITY + "_" + getDate().replaceAll(":", "");
        boolean mkdir;
        try {
            Files.createDirectory(new File(directoryName).toPath());
            mkdir = true;
        } catch (IOException e) {
            logger.error(e);
            mkdir = false;
        }
        if (mkdir) {
            Map<String, String> columnsTranslate = new HashMap<>();
            Writer textWriter = new TextWriter();
            TranslatorInterface translator = new Translator();
            for (Row row : table.getAllRows()) {
                String idRow = row.getId();
                try {
                    String idScs = CITY + "_" + idRow.toLowerCase();
                    String fileName = directoryName + "/" + idScs + "." + SCS;
                    File file = new File(fileName);
                    boolean mkfile = file.createNewFile();
                    if (mkfile) {
                        textWriter.write(fileName, idScs, true);
                        textWriter.write(fileName, "\n<-" + CONCEPT_CITY + ";", true);
                        List<String> header = table.getHeader();
                        for (String column : header) {
                            String value = row.getValue(column);
                            if (value != null && !value.equals("")) {
                                String relation;
                                if (column.equals(NAME_COLUMN)) {
                                    value = "[" + value + "]";
                                    relation = NREL_MAIN_IDTF;
                                } else {
                                    String regex = "(.)*(\\d)(.)*";
                                    Pattern pattern = Pattern.compile(regex);
                                    value = pattern.matcher(value).matches() ?
                                            "[" + value + "]" :
                                            translator.translate(value).replaceAll(" ", "_");
                                    String columnTranslate;
                                    if (columnsTranslate.containsKey(column)) {
                                        columnTranslate = columnsTranslate.get(column);
                                    } else {
                                        columnTranslate = LanguageTranslator.translate("en", column).
                                                toLowerCase();
                                        columnsTranslate.put(column, columnTranslate);
                                    }
                                    relation = NREL + "_" + columnTranslate.replaceAll(" ", "_");
                                }
                                textWriter.write(fileName, "\n=>" +
                                        relation + ":" + value + ";", true);
                            }
                        }
                        textWriter.write(fileName, ";", true);
                    } else {
                        logger.warn(file + " wasn't created.");
                    }
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        } else {
            result = false;
        }
        return result;
    }
}
