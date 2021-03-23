package studia.ium.warehouseandroidclient.application.ui.product.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studia.ium.warehouseandroidclient.R;
import studia.ium.warehouseandroidclient.application.configuration.WarehouseClientApplication;
import studia.ium.warehouseandroidclient.application.ui.product.model.PresentableProduct;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.ProductsListViewModel;

public class ProductsListActivity extends AppCompatActivity {
    @Inject
    ProductsListViewModel viewModel;
    private boolean offlineMode = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.products_list)
    RecyclerView productsListView;
    private ProductsListViewAdapter productsListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        initActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reload)
            reloadProducts();
        else if (id == R.id.action_offline_mode)
            toggleOfflineMode(item);
        else if (id == R.id.action_show_sync_errors)
            startProductSyncErrorsListActivity();
        else if (id == R.id.action_clear_local_data)
            clearLocalData();

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.add_product_button)
    void onAddProductButtonClicked() {
        startAddProductActivity();
    }

    private void initActivity() {
        injectDependencies();
        initViews();
        initViewModelObservers();
        loadInitialProducts();
    }

    private void injectDependencies() {
        ((WarehouseClientApplication) getApplication()).getProductComponent().inject(this);
    }

    private void initViews() {
        ButterKnife.bind(this);
        initToolbar();
        initProductsListView();
    }

    private void initToolbar() {
        toolbar.setTitle(R.string.app_display_name);
        setSupportActionBar(toolbar);
    }

    private void initProductsListView() {
        productsListView.setHasFixedSize(true);
        productsListView.setLayoutManager(new LinearLayoutManager(this));
        productsListView.setItemAnimator(new DefaultItemAnimator());
        productsListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        productsListViewAdapter = new ProductsListViewAdapter(this);
        productsListView.setAdapter(productsListViewAdapter);
    }

    private void initViewModelObservers() {
        initProductsObserver();
        initErrorMessageObserver();
    }

    private void initProductsObserver() {
        viewModel
            .getProducts()
            .observe(this, this::updateProductsListView);
    }

    private void initErrorMessageObserver() {
        viewModel
            .getErrorMessage()
            .observe(this, this::showErrorMessage);
    }

    private void updateProductsListView(List<PresentableProduct> updatedProducts) {
        productsListViewAdapter.getProducts().clear();
        productsListViewAdapter.getProducts().addAll(updatedProducts);
        productsListViewAdapter.notifyDataSetChanged();
    }

    private void loadInitialProducts() {
        viewModel.reloadProducts();
    }

    private void reloadProducts() {
        viewModel.reloadProducts();
    }

    private void toggleOfflineMode(MenuItem item) {
        viewModel.toggleOfflineMode();
        offlineMode = !offlineMode;
        if (offlineMode) {
            item.setTitle("Disable offline mode");
            showShortMessage("Offline mode enabled");
            toolbar.setTitle(getResources().getString(R.string.app_display_name) + " (offline)");
        } else {
            item.setTitle("Enable offline mode");
            showShortMessage("Offline mode disabled");
            toolbar.setTitle(R.string.app_display_name);
        }
    }

    private void startAddProductActivity() {
        startActivity(AddProductActivity.getCallingIntent(this));
    }

    private void startProductSyncErrorsListActivity() {
        startActivity(ProductSyncErrorsListActivity.getCallingIntent(this));
    }

    private void clearLocalData() {
        showClearLocalDataConfirmationDialog();
    }

    private void showClearLocalDataConfirmationDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Clear local data")
            .setMessage("Are you sure you want to clear local data?")
            .setPositiveButton(android.R.string.yes, (dialog, which) -> onClearConfirmed())
            .setNegativeButton(android.R.string.no, null)
            .show();
    }

    private void onClearConfirmed() {
        viewModel.clearLocalData();
    }

    private void showErrorMessage(String message) {
        showLongMessage(message);
    }

    private void showShortMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showLongMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
