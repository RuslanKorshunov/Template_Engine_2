package validator.factory;

import validator.*;

import java.util.List;

public class ValueValidatorFactory extends ValidatorFactory {
    public ValueValidatorFactory() {
        super();
        List<Validator> validators = getValidators();
        validators.add(new LinkValidator());
        validators.add(new UnitValidator());
        validators.add(new PostcodeValidator());
        validators.add(new NumberValidator());
        validators.add(new SymbolValidator());
    }
}
