package studia.ium.warehouseandroidclient.application.ui.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import studia.ium.warehouseandroidclient.business.product.boundary.ProductSynchronizationService;

public class ProductSyncErrorsListViewModel {
    private ProductSynchronizationService productSynchronizationService;
    private MutableLiveData<List<String>> syncErrorMessages = new MutableLiveData<>();
    private Executor backgroundTaskExecutor = Executors.newSingleThreadExecutor();

    public ProductSyncErrorsListViewModel(ProductSynchronizationService productSynchronizationService) {
        this.productSynchronizationService = productSynchronizationService;
    }

    public LiveData<List<String>> getSyncErrorMessages() {
        return syncErrorMessages;
    }

    public void loadSyncErrorMessages() {
        executeInBackground(this::performLoadSyncErrorsCall);
    }

    private void executeInBackground(Runnable task) {
        backgroundTaskExecutor.execute(task);
    }

    private void performLoadSyncErrorsCall() {
        syncErrorMessages.postValue(productSynchronizationService.getAllSyncErrorMessages());
    }
}
