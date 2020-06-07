package validator.factory;

import validator.AndValidator;
import validator.Validator;

import java.util.List;

public class ColumnValidatorFactory extends ValidatorFactory {
    public ColumnValidatorFactory() {
        super();
        List<Validator> validators = getValidators();
        validators.add(new AndValidator());
    }
}
