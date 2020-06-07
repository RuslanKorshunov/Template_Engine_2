package validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidator extends Validator {
    private static final String NUMBER_REGEX;
    private static final String COMMA;
    private static final String DOT;
    private static final Logger logger;

    static {
        NUMBER_REGEX = "(\\d+,\\d+)|(\\d+(\\s\\d+)+)";
        COMMA = ",";
        DOT = ".";
        logger = LogManager.getLogger(NumberValidator.class);
    }

    @Override
    public String validate(String str) {
        str = str.trim();
        Pattern pattern = Pattern.compile(NUMBER_REGEX);
        Matcher matcher = pattern.matcher(str);
        try {
            while (matcher.find()) {
                int indexStart = matcher.start();
                int indexEnd = matcher.end();
                String data = str.substring(indexStart, indexEnd);
                String result = data.replaceAll(COMMA, DOT).
                        replaceAll("\\s", "");
                str = str.replace(data, result);
                matcher = pattern.matcher(str);
            }
        } catch (NumberFormatException e) {
            logger.error(e);
        }
        str = str.trim();
        return next(str);
    }
}
