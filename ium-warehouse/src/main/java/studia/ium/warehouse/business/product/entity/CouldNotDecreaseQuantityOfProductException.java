package studia.ium.warehouse.business.product.entity;

import java.util.List;

public class CouldNotDecreaseQuantityOfProductException extends ProductException {
    public CouldNotDecreaseQuantityOfProductException(List<String> errorMessages) {
        super(errorMessages);
    }
}
