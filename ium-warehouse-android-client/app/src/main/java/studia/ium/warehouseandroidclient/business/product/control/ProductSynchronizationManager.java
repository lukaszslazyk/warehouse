package studia.ium.warehouseandroidclient.business.product.control;

import java.util.List;

public interface ProductSynchronizationManager {
    void toggleOfflineMode();

    List<String> getAllSyncErrorMessages();

    void clearLocalData();
}
