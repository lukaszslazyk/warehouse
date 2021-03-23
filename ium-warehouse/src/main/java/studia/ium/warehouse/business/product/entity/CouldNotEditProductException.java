package studia.ium.warehouse.business.product.entity;

import java.util.List;

public class CouldNotEditProductException extends ProductException {
    public CouldNotEditProductException(List<String> errorMessages) {
        super(errorMessages);
    }
}
