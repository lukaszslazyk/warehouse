package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import java.util.List;
import java.util.Optional;

import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;
import studia.ium.warehouseandroidclient.business.product.entity.Product;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;

public class MemoryProductRepository implements ProductRepository {
    private ProvidedMemoryProductRepository providedMemoryProductRepository;

    public MemoryProductRepository(ProvidedMemoryProductRepository providedMemoryProductRepository) {
        this.providedMemoryProductRepository = providedMemoryProductRepository;
    }

    @Override
    public List<Product> findAll() {
        return providedMemoryProductRepository.findAll();
    }

    @Override
    public Product findById(String productId) {
        return providedMemoryProductRepository.findById(productId);
    }

    @Override
    public void addProduct(Product product) {
        product.setQuantityInWarehouse(0, 0);
        providedMemoryProductRepository.insert(product);
    }

    @Override
    public void editProduct(String productId, List<ProductPatch> productPatches) {
        Product inRepo = providedMemoryProductRepository.findById(productId);
        productPatches.forEach(patch -> applySingle(patch, inRepo));
        providedMemoryProductRepository.update(inRepo);
    }

    @Override
    public void deleteById(String productId) {
        markAsDeleted(productId);
    }

    @Override
    public void increaseQuantity(String productId, Integer amount, Integer warehouseId) {
        Product product = providedMemoryProductRepository.findById(productId);
        Optional<Integer> currentQuantity = product.getQuantityInWarehouse(warehouseId);
        if (currentQuantity.isPresent())
            product.setQuantityInWarehouse(warehouseId, currentQuantity.get() + amount);
        else
            product.setQuantityInWarehouse(warehouseId, amount);
        providedMemoryProductRepository.update(product);
    }

    @Override
    public void decreaseQuantity(String productId, Integer amount, Integer warehouseId) {
        Product product = providedMemoryProductRepository.findById(productId);
        Optional<Integer> currentQuantity = product.getQuantityInWarehouse(warehouseId);
        if (currentQuantity.isPresent())
            product.setQuantityInWarehouse(warehouseId, currentQuantity.get() - amount);
        else
            product.setQuantityInWarehouse(warehouseId, 0);
        providedMemoryProductRepository.update(product);
    }

    @Override
    public boolean existsByManufacturerNameAndModelName(String manufacturerName, String modelName) {
        Product product = providedMemoryProductRepository
            .findByManufacturerNameAndModelName(manufacturerName, modelName);

        return product != null;
    }

    void replaceProduct(Product product) {
        providedMemoryProductRepository.update(product);
    }

    void deleteAll() {
        providedMemoryProductRepository.deleteAll();
    }

    void resetDataTo(List<Product> newProducts) {
        providedMemoryProductRepository.deleteAll();
        providedMemoryProductRepository.insertAll(newProducts.stream().toArray(Product[]::new));
    }

    private void markAsDeleted(String productId) {
        Product product = providedMemoryProductRepository.findById(productId);
        product.setDeleted(true);
        providedMemoryProductRepository.update(product);
    }

    private void applySingle(ProductPatch patch, Product inRepo) {
        if (patch.getFieldName().equals("manufacturerName"))
            inRepo.setManufacturerName((String) patch.getValue());
        else if (patch.getFieldName().equals("modelName"))
            inRepo.setModelName((String) patch.getValue());
        else if (patch.getFieldName().equals("price"))
            inRepo.setPrice((Double) patch.getValue());
        else if (patch.getFieldName().equals("expirationDate"))
            inRepo.setExpirationDate((Long) patch.getValue());
    }
}
