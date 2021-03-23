package studia.ium.warehouseandroidclient.application.configuration;

import android.app.Application;

import androidx.room.Room;

import studia.ium.warehouseandroidclient.application.configuration.database.WarehouseDatabase;
import studia.ium.warehouseandroidclient.application.configuration.database.migrations.AllMigrations;
import studia.ium.warehouseandroidclient.application.configuration.di.product.DaggerProductComponent;
import studia.ium.warehouseandroidclient.application.configuration.di.product.ProductComponent;
import studia.ium.warehouseandroidclient.application.configuration.di.product.ProductModule;
import studia.ium.warehouseandroidclient.application.configuration.properties.PropertiesProvider;

public class WarehouseClientApplication extends Application {
    private final static String DATABASE_NAME = "warehouse-database";
    private final static String CONFIG_PROPERTIES_FILE_NAME = "config.properties";
    private ProductComponent productComponent;
    private PropertiesProvider propertiesProvider;
    private WarehouseDatabase warehouseDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        initDatabase();
        initPropertiesProvider();
        initInjection();
    }

    public ProductComponent getProductComponent() {
        return productComponent;
    }

    private void initDatabase() {
        warehouseDatabase =
            Room.databaseBuilder(getApplicationContext(), WarehouseDatabase.class, DATABASE_NAME)
                .addMigrations(AllMigrations.get())
                .build();
    }

    private void initPropertiesProvider() {
        propertiesProvider = new PropertiesProvider(this, CONFIG_PROPERTIES_FILE_NAME);
    }

    private void initInjection() {
        initProductComponent();
    }

    private void initProductComponent() {
        productComponent = DaggerProductComponent
            .builder()
            .productModule(new ProductModule(warehouseDatabase, propertiesProvider))
            .build();
    }
}
