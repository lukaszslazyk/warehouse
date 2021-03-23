package studia.ium.warehouse.business.product.control;

import studia.ium.warehouse.business.product.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(UUID id);

    Optional<Product> findByManufacturerNameAndModelName(String manufacturerName, String modelName);

    boolean existsById(UUID id);

    Product save(Product product);

    void deleteById(UUID id);
}
