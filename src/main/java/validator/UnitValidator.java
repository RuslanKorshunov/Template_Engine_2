package validator;

import reader.Reader;

import java.util.List;

public class UnitValidator extends Validator {
    private static final String PLUS_MINUS;

    static {
        PLUS_MINUS = "±";
    }

    private List<String> units;

    public UnitValidator() {
        String unitFile = "src/main/resources/txt/units.txt";
        Reader reader = new Reader();
        units = reader.read(unitFile);
    }

    @Override
    public String validate(String str) {
        str = str.trim();
        for (String unit : units) {
            str = str + " ";
            str = str.replaceAll(unit + "\\s", "");
            if (str.contains(PLUS_MINUS)) {
                int index = str.indexOf(PLUS_MINUS);
                str = str.substring(0, index);
            }
            str = str.trim();
        }
        return next(str);
    }
}
