package studia.ium.warehouseandroidclient.business.product.boundary;

import java.util.List;

import studia.ium.warehouseandroidclient.business.product.control.ProductSynchronizationManager;

public class ProductSynchronizationService {
    private ProductSynchronizationManager productSynchronizationManager;

    public ProductSynchronizationService(ProductSynchronizationManager productSynchronizationManager) {
        this.productSynchronizationManager = productSynchronizationManager;
    }

    public void toggleOfflineMode() {
        productSynchronizationManager.toggleOfflineMode();
    }

    public List<String> getAllSyncErrorMessages() {
        return productSynchronizationManager.getAllSyncErrorMessages();
    }

    public void clearLocalData() {
        productSynchronizationManager.clearLocalData();
    }
}
