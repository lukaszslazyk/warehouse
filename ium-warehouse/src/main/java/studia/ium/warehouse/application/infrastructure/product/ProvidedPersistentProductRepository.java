package studia.ium.warehouse.application.infrastructure.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import studia.ium.warehouse.business.product.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface ProvidedPersistentProductRepository extends CrudRepository<Product, UUID> {
    List<Product> findAll();

    Optional<Product> findByManufacturerNameAndModelName(String manufacturerName, String modelName);
}
