package studia.ium.warehouse.application.rest.product.control;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import studia.ium.warehouse.business.product.entity.ProductPatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditProductInputParser {
    public List<ProductPatch> parse(String jsonValue) {
        try {
            if (isLegacy(jsonValue))
                return parseLegacy(jsonValue);
            else
                return parseDirectly(jsonValue);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isLegacy(String jsonValue) {
        return !jsonValue.contains("fieldName");
    }

    private List<ProductPatch> parseLegacy(String jsonValue) throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(jsonValue);
        List<ProductPatch> productPatches = new ArrayList<>();
        if (jsonNode.has("manufacturerName"))
            productPatches.add(new ProductPatch("manufacturerName", jsonNode.get("manufacturerName").asText()));
        if (jsonNode.has("modelName"))
            productPatches.add(new ProductPatch("modelName", jsonNode.get("modelName").asText()));
        if (jsonNode.has("price"))
            productPatches.add(new ProductPatch("price", jsonNode.get("price").asDouble()));

        return productPatches;
    }

    private List<ProductPatch> parseDirectly(String jsonValue) throws IOException {
        return new ObjectMapper().readValue(jsonValue, new TypeReference<List<ProductPatch>>() {
        });
    }
}
