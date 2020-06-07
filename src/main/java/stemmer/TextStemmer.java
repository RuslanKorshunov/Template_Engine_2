package stemmer;

import stemmer.entity.StemmerEntity;
import validator.Validator;
import writer.TextWriter;
import writer.Writer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TextStemmer implements Stemmer {
    private static final String OPTIONS;
    private static final String MYSTEM_PATH;
    private static final String CONTENT_FILE;
    private static final String LEFT_BRACE;
    private static final String RIGHT_BRACE;
    private static final String VERTICAL_BAR;
    private static final Logger logger;

    static {
        OPTIONS = "-n";
        MYSTEM_PATH = "src/main/resources/exe/mystem.exe";
        CONTENT_FILE = "src/main/resources/txt/content.txt";
        LEFT_BRACE = "{";
        RIGHT_BRACE = "}";
        VERTICAL_BAR = "\\|";
        logger = LogManager.getLogger(TextStemmer.class);
    }

    private Writer writer;
    private Validator validator;

    public TextStemmer() {
        writer = new TextWriter();
        validator = new QuestionValidator();
    }

    @Override
    public List<StemmerEntity> stem(String text, boolean append) {
        List<StemmerEntity> words = new ArrayList<>();
        boolean isWritten = writer.write(CONTENT_FILE, text, append);
        if (isWritten) {
            ProcessBuilder processBuilder = new ProcessBuilder(MYSTEM_PATH, OPTIONS, CONTENT_FILE);
            try {
                Process process = processBuilder.start();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));
                reader.lines().forEach(line -> {
                    StemmerEntity entity = createStemmerEntity(line);
                    words.add(entity);
                });
            } catch (IOException e) {
                logger.error(e);
            }
        } else {
            logger.warn(text + " wasn't written in file.");
        }
        return words;
    }

    private StemmerEntity createStemmerEntity(String line) {
        line = validator.validate(line);
        int indexLeftBrace = line.indexOf(LEFT_BRACE);
        int indexRightBrace = line.indexOf(RIGHT_BRACE);
        String word = line.substring(0, indexLeftBrace);
        String[] values = line.substring(indexLeftBrace + 1, indexRightBrace).
                split(VERTICAL_BAR);
        return new StemmerEntity(word, values);
    }

    private static class QuestionValidator extends Validator {
        @Override
        public String validate(String str) {
            return str.replaceAll("\\?", "");
        }
    }
}
