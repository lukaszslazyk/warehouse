package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.APICallFailedException;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperation;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationSyncError;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ServerUnreachableException;
import studia.ium.warehouseandroidclient.business.product.entity.Product;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;

public class ProductAPIClient {
    private final String baseUrl;
    private Retrofit retrofit;
    private ProductAPI productAPI;

    public ProductAPIClient(String baseUrl) {
        this.baseUrl = baseUrl;

        initRetrofit();
        initAPIClient();
    }

    List<Product> findAll() {
        try {
            return productAPI
                .findAllProducts()
                .execute()
                .body();
        } catch (IOException e) {
            throw createRelevantAPICallFailedException(e);
        }
    }

    Product findById(String id) {
        try {
            return productAPI
                .findProductById(id)
                .execute()
                .body();
        } catch (IOException e) {
            throw createRelevantAPICallFailedException(e);
        }
    }

    void addProduct(Product product) {
        try {
            productAPI
                .addProduct(product)
                .execute();
        } catch (IOException e) {
            throw createRelevantAPICallFailedException(e);
        }
    }

    void editProduct(String id, List<ProductPatch> productPatches) {
        try {
            productAPI
                .editProduct(id, productPatches)
                .execute();
        } catch (IOException e) {
            throw createRelevantAPICallFailedException(e);
        }
    }

    void deleteProduct(String id) {
        try {
            productAPI
                .deleteProduct(id)
                .execute();
        } catch (IOException e) {
            throw createRelevantAPICallFailedException(e);
        }
    }

    void increaseQuantityOfProduct(String id, Integer amount, Integer warehouseId) {
        try {
            productAPI
                .increaseQuantityOfProduct(id, amount, warehouseId)
                .execute();
        } catch (IOException e) {
            throw createRelevantAPICallFailedException(e);
        }
    }

    void decreaseQuantityOfProduct(String id, Integer amount, Integer warehouseId) {
        try {
            productAPI
                .decreaseQuantityOfProduct(id, amount, warehouseId)
                .execute();
        } catch (IOException e) {
            throw createRelevantAPICallFailedException(e);
        }
    }

    List<ProductOperationSyncError> synchronize(List<ProductOperation> productOperations) {
        try {
            return productAPI
                .synchronize(productOperations)
                .execute()
                .body();
        } catch (IOException e) {
            throw createRelevantAPICallFailedException(e);
        }
    }

    private void initRetrofit() {
        Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();
        retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    }

    private void initAPIClient() {
        productAPI = retrofit.create(ProductAPI.class);
    }

    private APICallFailedException createRelevantAPICallFailedException(IOException e) {
        return isServerUnreachableException(e) ?
            new ServerUnreachableException() :
            new APICallFailedException("Unexpected error occurred");
    }

    private boolean isServerUnreachableException(IOException e) {
        return e.getMessage().contains("Failed to connect");
    }
}
