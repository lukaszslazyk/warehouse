package studia.ium.warehouse.business.product.entity;

import java.util.List;

public class CouldNotDeleteProductException extends ProductException {
    public CouldNotDeleteProductException(List<String> errorMessages) {
        super(errorMessages);
    }
}
