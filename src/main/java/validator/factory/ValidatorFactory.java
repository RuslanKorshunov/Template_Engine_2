package validator.factory;

import validator.Validator;

import java.util.ArrayList;
import java.util.List;

public abstract class ValidatorFactory {
    private List<Validator> validators;

    public ValidatorFactory() {
        validators = new ArrayList<>();
    }

    public final List<Validator> getValidators() {
        return validators;
    }

    public final Validator create() {
        for (int i = 0; i < validators.size(); i++) {
            Validator validator = validators.get(i);
            if (i != validators.size() - 1) {
                validator.setNextValidator(validators.get(i + 1));
            } else {
                validator.setNextValidator(null);
            }
        }
        return validators.get(0);
    }
}
