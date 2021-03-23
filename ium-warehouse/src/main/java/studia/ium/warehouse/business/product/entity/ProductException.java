package studia.ium.warehouse.business.product.entity;

import java.util.List;
import java.util.stream.Collectors;

public class ProductException extends RuntimeException {
    private final List<String> errorMessages;

    ProductException(List<String> errorMessages) {
        super(String.join("; ", errorMessages));
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getErrorMessagesInSingleString() {
        return errorMessages.stream()
            .map(msg -> "- " + msg)
            .collect(Collectors.joining("\n"));
    }
}
