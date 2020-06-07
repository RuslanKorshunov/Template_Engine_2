package validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostcodeValidator extends Validator {
    private static final String POSTCARD_REGEX;
    private static final String DASH;
    private static final Logger logger;

    static {
        DASH = "-|—";
        POSTCARD_REGEX = "\\d{6}\\s?[" + DASH + "]\\s?\\d{6}";
        logger = LogManager.getLogger(PostcodeValidator.class);
    }

    @Override
    public String validate(String str) {
        str = str.trim();
        Pattern pattern = Pattern.compile(POSTCARD_REGEX);
        Matcher matcher = pattern.matcher(str);
        try {
            while (matcher.find()) {
                int indexStart = matcher.start();
                int indexEnd = matcher.end();
                String data = str.substring(indexStart, indexEnd);
                String[] numbers = data.replaceAll("\\s", "").split(DASH);
                int first = Integer.valueOf(numbers[0]);
                int last = Integer.valueOf(numbers[1]);
                String result = "";
                for (int i = first; i <= last; i++) {
                    result += i;
                    if (i != last) {
                        result += ", ";
                    }
                }
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
