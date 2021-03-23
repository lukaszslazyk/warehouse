package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import java.util.List;

import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperation;

public class ProductOperationsRepository {
    private ProvidedMemoryProductOperationsRepository providedMemoryProductOperationsRepository;

    public ProductOperationsRepository(ProvidedMemoryProductOperationsRepository providedMemoryProductOperationsRepository) {
        this.providedMemoryProductOperationsRepository = providedMemoryProductOperationsRepository;
    }

    public List<ProductOperation> findAll() {
        return providedMemoryProductOperationsRepository.findAll();
    }

    public void add(ProductOperation productOperation) {
        providedMemoryProductOperationsRepository.insert(productOperation);
    }

    public void deleteAll() {
        providedMemoryProductOperationsRepository.deleteAll();
    }

    public boolean isEmpty() {
        return providedMemoryProductOperationsRepository.findAllCount() == 0;
    }
}
