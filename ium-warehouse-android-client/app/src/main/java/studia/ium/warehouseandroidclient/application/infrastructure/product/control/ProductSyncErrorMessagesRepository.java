package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductSyncErrorMessagesRepository {
    private Map<UUID, String> messages = new LinkedHashMap<>();

    public List<String> findAll() {
        return new ArrayList<>(messages.values());
    }

    public void add(String message) {
        messages.put(UUID.randomUUID(), message);
    }
}
