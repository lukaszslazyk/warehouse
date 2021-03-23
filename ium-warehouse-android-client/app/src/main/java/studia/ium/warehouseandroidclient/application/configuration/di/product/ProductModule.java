package studia.ium.warehouseandroidclient.application.configuration.di.product;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import studia.ium.warehouseandroidclient.application.configuration.database.WarehouseDatabase;
import studia.ium.warehouseandroidclient.application.configuration.properties.PropertiesProvider;
import studia.ium.warehouseandroidclient.application.infrastructure.product.control.APIResourceProductRepository;
import studia.ium.warehouseandroidclient.application.infrastructure.product.control.MemoryProductRepository;
import studia.ium.warehouseandroidclient.application.infrastructure.product.control.ProductAPIClient;
import studia.ium.warehouseandroidclient.application.infrastructure.product.control.ProductOperationsRepository;
import studia.ium.warehouseandroidclient.application.infrastructure.product.control.ProductSyncErrorMessagesRepository;
import studia.ium.warehouseandroidclient.application.infrastructure.product.control.ProductSyncErrorsManager;
import studia.ium.warehouseandroidclient.application.infrastructure.product.control.SynchronizableProductRepository;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.AddProductViewModel;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.DecreaseQuantityOfProductViewModel;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.EditProductViewModel;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.IncreaseQuantityOfProductViewModel;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.ProductDetailsViewModel;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.ProductSyncErrorsListViewModel;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.ProductsListViewModel;
import studia.ium.warehouseandroidclient.business.product.boundary.ProductService;
import studia.ium.warehouseandroidclient.business.product.boundary.ProductSynchronizationService;
import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;
import studia.ium.warehouseandroidclient.business.product.control.ProductSynchronizationManager;

@Module
public class ProductModule {
    private WarehouseDatabase warehouseDatabase;
    private PropertiesProvider propertiesProvider;

    public ProductModule(WarehouseDatabase warehouseDatabase,
                         PropertiesProvider propertiesProvider) {
        this.warehouseDatabase = warehouseDatabase;
        this.propertiesProvider = propertiesProvider;
    }

    @Provides
    @Singleton
    ProductService provideProductService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }

    @Provides
    @Singleton
    ProductSynchronizationService provideProductSynchronizationService(ProductSynchronizationManager productSynchronizationManager) {
        return new ProductSynchronizationService(productSynchronizationManager);
    }

    @Provides
    @Singleton
    ProductRepository provideProductRepository(SynchronizableProductRepository synchronizableProductRepository) {
        return synchronizableProductRepository;
    }

    @Provides
    @Singleton
    ProductSynchronizationManager provideProductSynchronizationManger(SynchronizableProductRepository synchronizableProductRepository) {
        return synchronizableProductRepository;
    }

    @Provides
    @Singleton
    SynchronizableProductRepository provideSynchronizableProductRepository(MemoryProductRepository memoryProductRepository,
                                                                           APIResourceProductRepository apiResourceProductRepository,
                                                                           ProductOperationsRepository productOperationsRepository,
                                                                           ProductSyncErrorsManager productSyncErrorsManager) {
        return new SynchronizableProductRepository(memoryProductRepository, apiResourceProductRepository,
            productOperationsRepository, productSyncErrorsManager);
    }

    @Provides
    @Singleton
    APIResourceProductRepository provideAPIResourceProductRepository(@Named("server_url") String serverUrl) {
        return new APIResourceProductRepository(new ProductAPIClient(serverUrl));
    }

    @Provides
    @Singleton
    MemoryProductRepository provideMemoryProductRepository() {
        return new MemoryProductRepository(warehouseDatabase.providedMemoryProductRepository());
    }

    @Provides
    @Singleton
    ProductOperationsRepository provideProductOperationsRepository() {
        return new ProductOperationsRepository(warehouseDatabase.providedMemoryProductOperationsRepository());
    }

    @Provides
    @Singleton
    ProductSyncErrorsManager provideProductOperationsSyncErrorsProcessor(ProductSyncErrorMessagesRepository productSyncErrorMessagesRepository,
                                                                         MemoryProductRepository memoryProductRepository) {
        return new ProductSyncErrorsManager(productSyncErrorMessagesRepository, memoryProductRepository);
    }

    @Provides
    @Singleton
    ProductSyncErrorMessagesRepository provideProductSyncErrorMessagesRepository() {
        return new ProductSyncErrorMessagesRepository();
    }

    @Provides
    @Named("server_url")
    String provideServerUrl() {
        return propertiesProvider.getStringProperty("server_url");
    }

    @Provides
    ProductsListViewModel provideProductsListViewModel(ProductService productService,
                                                       ProductSynchronizationService productSynchronizationService) {
        return new ProductsListViewModel(productService, productSynchronizationService);
    }

    @Provides
    ProductDetailsViewModel provideProductDetailsViewModel(ProductService productService) {
        return new ProductDetailsViewModel(productService);
    }

    @Provides
    AddProductViewModel provideAddProductViewModel(ProductService productService) {
        return new AddProductViewModel(productService);
    }

    @Provides
    EditProductViewModel provideEditProductViewModel(ProductService productService) {
        return new EditProductViewModel(productService);
    }

    @Provides
    IncreaseQuantityOfProductViewModel provideIncreaseQuantityOfProductViewModel(ProductService productService) {
        return new IncreaseQuantityOfProductViewModel(productService);
    }

    @Provides
    DecreaseQuantityOfProductViewModel provideDecreaseQuantityOfProductViewModel(ProductService productService) {
        return new DecreaseQuantityOfProductViewModel(productService);
    }

    @Provides
    ProductSyncErrorsListViewModel provideProductSyncErrorsListViewModel(ProductSynchronizationService productSynchronizationService) {
        return new ProductSyncErrorsListViewModel(productSynchronizationService);
    }
}
