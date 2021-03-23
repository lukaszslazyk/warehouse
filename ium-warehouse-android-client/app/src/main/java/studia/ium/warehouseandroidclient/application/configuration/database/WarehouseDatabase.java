package studia.ium.warehouseandroidclient.application.configuration.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import studia.ium.warehouseandroidclient.application.configuration.database.product.ProductTypeConverters;
import studia.ium.warehouseandroidclient.application.infrastructure.product.control.ProvidedMemoryProductOperationsRepository;
import studia.ium.warehouseandroidclient.application.infrastructure.product.control.ProvidedMemoryProductRepository;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperation;
import studia.ium.warehouseandroidclient.business.product.entity.Product;

@Database(version = 2,
    entities = {
        Product.class,
        ProductOperation.class
    })
@TypeConverters({
    ProductTypeConverters.class
})
public abstract class WarehouseDatabase extends RoomDatabase {
    public abstract ProvidedMemoryProductRepository providedMemoryProductRepository();

    public abstract ProvidedMemoryProductOperationsRepository providedMemoryProductOperationsRepository();
}
