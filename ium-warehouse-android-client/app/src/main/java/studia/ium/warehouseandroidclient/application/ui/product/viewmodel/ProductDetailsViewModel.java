package studia.ium.warehouseandroidclient.application.ui.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import studia.ium.warehouseandroidclient.application.ui.product.model.PresentableProduct;
import studia.ium.warehouseandroidclient.business.product.boundary.ProductService;
import studia.ium.warehouseandroidclient.business.product.entity.Product;

public class ProductDetailsViewModel extends ViewModel {
    private ProductService productService;
    private String productId;
    private MutableLiveData<PresentableProduct> product = new MutableLiveData<>();
    private MutableLiveData<String> loadProductErrorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> productDeleteSuccess = new MutableLiveData<>();
    private MutableLiveData<String> productDeleteErrorMessage = new MutableLiveData<>();
    private Executor backgroundTaskExecutor = Executors.newSingleThreadExecutor();

    public ProductDetailsViewModel(ProductService productService) {
        this.productService = productService;
    }

    public ProductDetailsViewModel withProductId(String productId) {
        this.productId = productId;

        return this;
    }

    public void init() {
        loadProduct();
    }

    public void refreshData() {
        loadProduct();
    }

    public void deleteProduct() {
        executeInBackground(this::performDeleteProductCall);
    }

    public LiveData<PresentableProduct> getProduct() {
        return product;
    }

    public LiveData<String> getLoadProductErrorMessage() {
        return loadProductErrorMessage;
    }

    public LiveData<Boolean> getProductDeleteSuccess() {
        return productDeleteSuccess;
    }

    public LiveData<String> getProductDeleteErrorMessage() {
        return productDeleteErrorMessage;
    }

    private void loadProduct() {
        executeInBackground(this::performLoadProductCall);
    }

    private void executeInBackground(Runnable task) {
        backgroundTaskExecutor.execute(task);
    }

    private void performLoadProductCall() {
        try {
            product.postValue(mapToPresentableProduct(productService.findById(productId)));
        } catch (Exception e) {
            loadProductErrorMessage.postValue(e.getMessage());
        }
    }

    private void performDeleteProductCall() {
        try {
            productService.delete(productId);
            productDeleteSuccess.postValue(true);
        } catch (Exception e) {
            loadProductErrorMessage.postValue(e.getMessage());
        }
    }

    private PresentableProduct mapToPresentableProduct(Product product) {
        return PresentableProduct.from(product);
    }
}
