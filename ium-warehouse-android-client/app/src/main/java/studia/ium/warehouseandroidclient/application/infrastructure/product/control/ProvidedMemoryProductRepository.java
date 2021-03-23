package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import studia.ium.warehouseandroidclient.business.product.entity.Product;

@Dao
public interface ProvidedMemoryProductRepository {
    @Query("SELECT * FROM PRODUCT WHERE deleted = 0")
    List<Product> findAll();

    @Query("SELECT * FROM PRODUCT WHERE id like :productId")
    Product findById(String productId);

    @Query("SELECT * FROM PRODUCT WHERE manufacturerName like :manufacturerName AND modelName like :modelName")
    Product findByManufacturerNameAndModelName(String manufacturerName, String modelName);

    @Insert
    void insert(Product product);

    @Insert
    void insertAll(Product... products);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM PRODUCT")
    void deleteAll();
}
