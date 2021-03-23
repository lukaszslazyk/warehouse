package studia.ium.warehouse.application.rest.product.entity;

import studia.ium.warehouse.business.product.entity.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ProductRestResource {
    private UUID id = UUID.randomUUID();
    private String manufacturerName;
    private String modelName;
    private Double price;
    private Integer quantity;
    private Long expirationDate;
    private Map<Integer, Integer> quantitiesInWarehouses = new HashMap<>();

    public ProductRestResource() {
    }

    public static List<ProductRestResource> fromMultiple(List<Product> products) {
        return products.stream()
            .map(ProductRestResource::from)
            .collect(Collectors.toList());
    }

    public static ProductRestResource from(Product product) {
        ProductRestResource productRestResource = new ProductRestResource();
        productRestResource.setId(product.getId());
        productRestResource.setManufacturerName(product.getManufacturerName());
        productRestResource.setModelName(product.getModelName());
        productRestResource.setPrice(product.getPrice());
        productRestResource.setQuantity(product.getQuantityInDefaultWarehouse());
        productRestResource.setExpirationDate(product.getExpirationDate());
        productRestResource.setQuantitiesInWarehouses(product.getQuantitiesInWarehouses());

        return productRestResource;
    }

    public Product toProduct() {
        Product product = new Product();
        product.setId(id);
        product.setManufacturerName(manufacturerName);
        product.setModelName(modelName);
        product.setPrice(price);
        product.setExpirationDate(expirationDate);
        product.setQuantitiesInWarehouses(quantitiesInWarehouses);
        if (nonNull(quantity))
            product.setQuantityInWarehouse(0, quantity);

        return product;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
