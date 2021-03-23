package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperation;

@Dao
public interface ProvidedMemoryProductOperationsRepository {
    @Query("SELECT * FROM PRODUCTOPERATION")
    List<ProductOperation> findAll();

    @Insert
    void insert(ProductOperation productOperation);

    @Query("DELETE FROM PRODUCTOPERATION")
    void deleteAll();

    @Query("SELECT count(*) FROM PRODUCTOPERATION")
    Integer findAllCount();
}
