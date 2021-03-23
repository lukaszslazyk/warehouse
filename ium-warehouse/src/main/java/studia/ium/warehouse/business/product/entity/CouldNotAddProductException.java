package studia.ium.warehouse.business.product.entity;

import java.util.List;

public class CouldNotAddProductException extends ProductException {
    public CouldNotAddProductException(List<String> errorMessages) {
        super(errorMessages);
    }
}
