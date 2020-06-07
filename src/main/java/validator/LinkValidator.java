package validator;

public class LinkValidator extends Validator {
    private static final String VALUE_REGEX;

    static {
        VALUE_REGEX = "\\[(\\w|\\W)*\\]|\\((\\w|\\W)*\\)";
    }

    @Override
    public String validate(String str) {
        str = str.trim();
        str = str.replaceAll(VALUE_REGEX, "");
        str = str.trim();
        return next(str);
    }
}
