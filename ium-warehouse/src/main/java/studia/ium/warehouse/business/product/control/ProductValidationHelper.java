package studia.ium.warehouse.business.product.control;

import java.util.ArrayList;
import java.util.List;

public class ProductValidationHelper {
    private List<String> errorMessages = new ArrayList<>();
    private List<String> warningMessages = new ArrayList<>();

    public void prepareValidation() {
        errorMessages.clear();
        warningMessages.clear();
    }

    public boolean validate(boolean condition, String errorMessage) {
        if (!condition)
            errorMessages.add(errorMessage);

        return condition;
    }

    public boolean validateWithWaringOnFail(boolean condition, String warningMessage) {
        if (!condition)
            warningMessages.add(warningMessage);

        return condition;
    }

    public boolean isValid() {
        return errorMessages.isEmpty();
    }

    public boolean anyWarningsOccurred() {
        return !warningMessages.isEmpty();
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public List<String> getWarningMessages() {
        return warningMessages;
    }
}
