package studia.ium.warehouseandroidclient.application.configuration.database.product;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationType;
import studia.ium.warehouseandroidclient.business.product.entity.Product;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;

import static java.util.Objects.nonNull;

public class ProductTypeConverters {
    @TypeConverter
    public static String productToJson(Product product) {
        return new Gson().toJson(product);
    }

    @TypeConverter
    public static Product productFromJson(String value) {
        return new Gson().fromJson(value, Product.class);
    }

    @TypeConverter
    public static String productOperationTypeToString(ProductOperationType type) {
        return type.getValue();
    }

    @TypeConverter
    public static ProductOperationType productOperationTypeFromString(String value) {
        return ProductOperationType.valueOf(value);
    }

    @TypeConverter
    public static String quantitiesInWarehousesToJson(Map<Integer, Integer> quantitiesInWarehouses) {
        return new Gson().toJson(quantitiesInWarehouses);
    }

    @TypeConverter
    public static Map<Integer, Integer> quantitiesInWarehousesFromJson(String value) {
        Type type = new TypeToken<Map<Integer, Integer>>() {
        }.getType();

        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String productPatchesToJson(List<ProductPatch> productPatches) {
        return new Gson().toJson(productPatches);
    }

    @TypeConverter
    public static List<ProductPatch> productPatchesFromJson(String value) {
        Type type = new TypeToken<List<ProductPatch>>() {
        }.getType();
        List<ProductPatch> converted = new Gson().fromJson(value, type);
        if (nonNull(converted))
            fixConversionIssuesIn(converted);

        return converted;
    }

    private static void fixConversionIssuesIn(List<ProductPatch> convertedPatches) {
        convertedPatches.forEach(patch -> {
            if (patch.getFieldName().equals("expirationDate"))
                patch.setValue(((Double) patch.getValue()).longValue());
        });
    }
}
