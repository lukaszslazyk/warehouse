package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperation;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationSyncError;
import studia.ium.warehouseandroidclient.business.product.entity.Product;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;

public interface ProductAPI {
    @GET("/api/products")
    Call<List<Product>> findAllProducts();

    @POST("/api/products")
    Call<ResponseBody> addProduct(@Body Product product);

    @GET("/api/products/{id}")
    Call<Product> findProductById(@Path("id") String id);

    @PATCH("/api/products/{id}")
    Call<ResponseBody> editProduct(@Path("id") String id, @Body List<ProductPatch> productPatches);

    @DELETE("/api/products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") String id);

    @POST("/api/products/{id}/increaseQuantity")
    Call<ResponseBody> increaseQuantityOfProduct(@Path("id") String id,
                                                 @Query("amount") Integer amount,
                                                 @Query("warehouseId") Integer warehouseId);

    @POST("/api/products/{id}/decreaseQuantity")
    Call<ResponseBody> decreaseQuantityOfProduct(@Path("id") String id,
                                                 @Query("amount") Integer amount,
                                                 @Query("warehouseId") Integer warehouseId);

    @POST("/api/products/synchronize")
    Call<List<ProductOperationSyncError>> synchronize(@Body List<ProductOperation> productOperations);
}
