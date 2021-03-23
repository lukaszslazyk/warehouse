package studia.ium.warehouse.business.product.entity;

import java.util.List;

public class CouldNotEntirelyEditProductException extends ProductException {
    public CouldNotEntirelyEditProductException(List<String> errorMessages) {
        super(errorMessages);
    }
}
