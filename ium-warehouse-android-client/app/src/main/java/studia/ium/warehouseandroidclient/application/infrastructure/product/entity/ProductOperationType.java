package studia.ium.warehouseandroidclient.application.infrastructure.product.entity;

public enum ProductOperationType {
    ADD("ADD"),
    EDIT("EDIT"),
    DELETE("DELETE"),
    INCREASE_QUANTITY("INCREASE_QUANTITY"),
    DECREASE_QUANTITY("DECREASE_QUANTITY");

    private String value;

    ProductOperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
