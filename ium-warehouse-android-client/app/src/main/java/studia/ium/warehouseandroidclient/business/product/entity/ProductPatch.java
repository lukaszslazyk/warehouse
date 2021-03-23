package studia.ium.warehouseandroidclient.business.product.entity;

public class ProductPatch {
    private String fieldName;
    private Object value;

    public ProductPatch() {
    }

    public ProductPatch(String fieldName, Object value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
