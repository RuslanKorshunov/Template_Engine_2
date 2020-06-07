package validator;

public abstract class Validator {
    private Validator validator;

    public void setNextValidator(Validator next) {
        validator = next;
    }

    public abstract String validate(String str);

    final String next(String str) {
        return validator != null && !str.equals("") ?
                validator.validate(str) : str;
    }
}
