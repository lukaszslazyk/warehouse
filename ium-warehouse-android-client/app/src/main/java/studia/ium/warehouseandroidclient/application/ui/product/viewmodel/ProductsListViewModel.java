package studia.ium.warehouseandroidclient.application.ui.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import studia.ium.warehouseandroidclient.application.ui.product.model.PresentableProduct;
import studia.ium.warehouseandroidclient.business.product.boundary.ProductService;
import studia.ium.warehouseandroidclient.business.product.boundary.ProductSynchronizationService;
import studia.ium.warehouseandroidclient.business.product.entity.Product;

public class ProductsListViewModel extends ViewModel {
    private ProductService productService;
    private ProductSynchronizationService productSynchronizationService;
    private MutableLiveData<List<PresentableProduct>> products = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private Executor backgroundTaskExecutor = Executors.newSingleThreadExecutor();

    public ProductsListViewModel(ProductService productService,
                                 ProductSynchronizationService productSynchronizationService) {
        this.productService = productService;
        this.productSynchronizationService = productSynchronizationService;
    }

    public LiveData<List<PresentableProduct>> getProducts() {
        return products;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void reloadProducts() {
        executeInBackground(this::performFindAllProductsCall);
    }

    public void toggleOfflineMode() {
        executeInBackground(this::performToggleOfflineMode);
    }

    public void clearLocalData() {
        executeInBackground(this::performClearLocalData);
        reloadProducts();
    }

    private void executeInBackground(Runnable task) {
        backgroundTaskExecutor.execute(task);
    }

    private void performFindAllProductsCall() {
        try {
            products.postValue(mapToPresentableProducts(productService.findAll()));
        } catch (RuntimeException e) {
            errorMessage.postValue(e.getMessage());
        }
    }

    private List<PresentableProduct> mapToPresentableProducts(List<Product> products) {
        return products.stream()
            .map(PresentableProduct::from)
            .collect(Collectors.toList());
    }

    private void performToggleOfflineMode() {
        try {
            productSynchronizationService.toggleOfflineMode();
        } catch (RuntimeException e) {
            errorMessage.postValue(e.getMessage());
        }
    }

    private void performClearLocalData() {
        try {
            productSynchronizationService.clearLocalData();
        } catch (RuntimeException e) {
            errorMessage.postValue(e.getMessage());
        }
    }
}
