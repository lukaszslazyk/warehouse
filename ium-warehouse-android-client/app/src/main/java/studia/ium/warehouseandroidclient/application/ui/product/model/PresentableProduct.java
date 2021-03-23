package studia.ium.warehouseandroidclient.application.ui.product.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import studia.ium.warehouseandroidclient.business.product.entity.Product;

import static java.util.Objects.nonNull;

public class PresentableProduct implements Serializable {
    private String id = UUID.randomUUID().toString();
    private String manufacturerName;
    private String modelName;
    private Double price;
    private Long expirationDate;
    private Map<Integer, Integer> quantitiesInWarehouses = new HashMap<>();

    public static Product mapToProduct(PresentableProduct presentableProduct) {
        Product product = new Product();
        product.setId(presentableProduct.getId());
        product.setManufacturerName(presentableProduct.getManufacturerName());
        product.setModelName(presentableProduct.getModelName());
        product.setPrice(presentableProduct.getPrice());
        product.setExpirationDate(presentableProduct.getExpirationDate());
        product.setQuantitiesInWarehouses(presentableProduct.getQuantitiesInWarehouses());

        return product;
    }

    public static PresentableProduct from(Product product) {
        PresentableProduct presentableProduct = new PresentableProduct();
        presentableProduct.setId(product.getId());
        presentableProduct.setManufacturerName(product.getManufacturerName());
        presentableProduct.setModelName(product.getModelName());
        presentableProduct.setPrice(product.getPrice());
        presentableProduct.setExpirationDate(product.getExpirationDate());
        presentableProduct.setQuantitiesInWarehouses(product.getQuantitiesInWarehouses());

        return presentableProduct;
    }

    public String getPresentablePrice() {
        return nonNull(price) ?
            new DecimalFormat("#.##").format(price) :
            "n/a";
    }

    public String getEditablePrice() {
        return nonNull(price) ?
            new DecimalFormat("#.##").format(price) :
            "";
    }

    public String getPresentableExpirationDate() {
        return nonNull(expirationDate) ?
            new SimpleDateFormat("dd/MM/yyyy").format(new Date(expirationDate)) :
            "n/a";
    }

    public String getEditableExpirationDate() {
        return nonNull(expirationDate) ?
            new SimpleDateFormat("dd/MM/yyyy").format(new Date(expirationDate)) :
            "";
    }

    public Optional<Integer> getQuantityInWarehouse(Integer warehouseId) {
        return Optional.ofNullable(quantitiesInWarehouses.get(warehouseId));
    }

    public List<Map.Entry<Integer, Integer>> getQuantitiesInWarehousesAsList() {
        return quantitiesInWarehouses.entrySet().stream()
            .sorted(Comparator.comparingInt(Map.Entry::getKey))
            .collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Map<Integer, Integer> getQuantitiesInWarehouses() {
        return quantitiesInWarehouses;
    }

    public void setQuantitiesInWarehouses(Map<Integer, Integer> quantitiesInWarehouses) {
        this.quantitiesInWarehouses = quantitiesInWarehouses;
    }
}
