package validator;

import stemmer.Stemmer;
import stemmer.TextStemmer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndValidator extends Validator {
    private static final String REGEX;
    private static final String COMA;

    static {
        REGEX = "\\sи\\s";
        COMA = ",";
    }

    private Stemmer stemmer;

    public AndValidator() {
        stemmer = new TextStemmer();
    }

    @Override
    public String validate(String str) {
        str = str.trim();
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int indexStart = matcher.start();
            int indexEnd = matcher.end();
            String and = str.substring(indexStart, indexEnd);
            str = str.replaceAll(and, COMA + " ");
            matcher = pattern.matcher(str);
        }
        str = str.trim();
        return next(str);
    }
}
