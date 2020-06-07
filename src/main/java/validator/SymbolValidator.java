package validator;

public class SymbolValidator extends Validator {
    private static final String SYMBOL_REGEX;

    static {
        SYMBOL_REGEX = "[^\\s\\wА-яё,+:.-—-%]";
    }

    @Override
    public String validate(String str) {
        return str.trim().replaceAll(SYMBOL_REGEX, "").trim();
    }
}
