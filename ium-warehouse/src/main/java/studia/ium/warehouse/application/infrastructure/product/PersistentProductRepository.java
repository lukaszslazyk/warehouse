package studia.ium.warehouse.application.infrastructure.product;

import studia.ium.warehouse.business.product.control.ProductRepository;
import studia.ium.warehouse.business.product.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersistentProductRepository implements ProductRepository {
    private ProvidedPersistentProductRepository providedPersistentProductRepository;

    public PersistentProductRepository(ProvidedPersistentProductRepository providedPersistentProductRepository) {
        this.providedPersistentProductRepository = providedPersistentProductRepository;
    }

    @Override
    public List<Product> findAll() {
        return providedPersistentProductRepository
            .findAll();
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return providedPersistentProductRepository
            .findById(id);
    }

    @Override
    public Optional<Product> findByManufacturerNameAndModelName(String manufacturerName, String modelName) {
        return providedPersistentProductRepository
            .findByManufacturerNameAndModelName(manufacturerName, modelName);
    }

    @Override
    public boolean existsById(UUID id) {
        return providedPersistentProductRepository
            .existsById(id);
    }

    @Override
    public Product save(Product product) {
        return providedPersistentProductRepository
            .save(product);
    }

    @Override
    public void deleteById(UUID id) {
        providedPersistentProductRepository
            .deleteById(id);
    }
}
