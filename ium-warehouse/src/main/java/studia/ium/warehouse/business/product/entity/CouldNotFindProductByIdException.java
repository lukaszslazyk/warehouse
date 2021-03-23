package studia.ium.warehouse.business.product.entity;

import java.util.List;

public class CouldNotFindProductByIdException extends ProductException {
    public CouldNotFindProductByIdException(List<String> errorMessages) {
        super(errorMessages);
    }
}
