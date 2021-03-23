package studia.ium.warehouse.business.product.entity;

import java.util.List;

public class CouldNotIncreaseQuantityOfProductException extends ProductException {
    public CouldNotIncreaseQuantityOfProductException(List<String> errorMessages) {
        super(errorMessages);
    }
}
