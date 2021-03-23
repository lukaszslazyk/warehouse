package studia.ium.warehouseandroidclient.application.infrastructure.product.entity;

public class APICallFailedException extends RuntimeException {
    public APICallFailedException() {
    }

    public APICallFailedException(String message) {
        super(message);
    }
}
