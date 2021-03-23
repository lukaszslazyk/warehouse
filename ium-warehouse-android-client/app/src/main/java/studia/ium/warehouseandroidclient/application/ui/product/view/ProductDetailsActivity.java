package studia.ium.warehouseandroidclient.application.ui.product.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studia.ium.warehouseandroidclient.R;
import studia.ium.warehouseandroidclient.application.configuration.WarehouseClientApplication;
import studia.ium.warehouseandroidclient.application.ui.product.model.PresentableProduct;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.ProductDetailsViewModel;
import studia.ium.warehouseandroidclient.databinding.ActivityProductDetailsBinding;

import static java.util.Objects.requireNonNull;

public class ProductDetailsActivity extends AppCompatActivity {
    private static final String TITLE = "Product details";
    @Inject
    ProductDetailsViewModel viewModel;
    private String productId;

    @BindView(R.id.quantities_list)
    RecyclerView quantitiesListView;
    private QuantitiesListViewAdapter quantitiesListViewAdapter;

    public static Intent getCallingIntent(Context context, String productId) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra("PRODUCT_ID", productId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @OnClick(R.id.increase_quantity_of_product_button)
    void onIncreaseQuantityOfProductButtonClicked() {
        startIncreaseQuantityOfProductActivity();
    }

    @OnClick(R.id.decrease_quantity_of_product_button)
    void onDecreaseQuantityOfProductButtonClicked() {
        startDecreaseQuantityOfProductActivity();
    }

    @OnClick(R.id.edit_product_button)
    void onEditProductButtonClicked() {
        startEditProductActivity();
    }

    @OnClick(R.id.delete_product_button)
    void onDeleteProductButtonClicked() {
        showDeleteConfirmationDialog();
    }

    private void initActivity() {
        injectDependencies();
        loadDataFromIntent();
        initViewModel();
        initDataBinding();
        initViews();
        initViewModelObservers();
    }

    private void injectDependencies() {
        ((WarehouseClientApplication) getApplication()).getProductComponent().inject(this);
    }

    private void loadDataFromIntent() {
        productId = (String) getIntent().getSerializableExtra("PRODUCT_ID");
    }

    private void initViewModel() {
        viewModel
            .withProductId(productId)
            .init();
    }

    private void initDataBinding() {
        ActivityProductDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initViews() {
        ButterKnife.bind(this);
        initToolbar();
        initQuantitiesListView();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(TITLE);
        setSupportActionBar(toolbar);
        requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void initQuantitiesListView() {
        quantitiesListView.setHasFixedSize(true);
        quantitiesListView.setLayoutManager(new LinearLayoutManager(this));
        quantitiesListView.setItemAnimator(new DefaultItemAnimator());
        quantitiesListViewAdapter = new QuantitiesListViewAdapter(this);
        quantitiesListView.setAdapter(quantitiesListViewAdapter);
    }

    private void initViewModelObservers() {
        initProductObserver();
        initLoadProductErrorMessageObserver();
        initProductDeleteSuccessObserver();
        initProductDeleteErrorMessageObserver();
    }

    private void initProductObserver() {
        viewModel
            .getProduct()
            .observe(this, this::updateQuantitiesList);
    }

    private void initLoadProductErrorMessageObserver() {
        viewModel
            .getLoadProductErrorMessage()
            .observe(this, this::handleLoadProductFailure);
    }

    private void initProductDeleteSuccessObserver() {
        viewModel
            .getProductDeleteSuccess()
            .observe(this, deleted -> finish());
    }

    private void initProductDeleteErrorMessageObserver() {
        viewModel
            .getProductDeleteErrorMessage()
            .observe(this, this::showErrorMessage);
    }

    private void startIncreaseQuantityOfProductActivity() {
        startActivity(IncreaseQuantityOfProductActivity.getCallingIntent(this, getProduct().getId()));
    }

    private void startDecreaseQuantityOfProductActivity() {
        startActivity(DecreaseQuantityOfProductActivity.getCallingIntent(this, getProduct()));
    }

    private void startEditProductActivity() {
        startActivity(EditProductActivity.getCallingIntent(this, getProduct()));
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Delete product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton(android.R.string.yes, (dialog, which) -> onDeleteConfirmed())
            .setNegativeButton(android.R.string.no, null)
            .show();
    }

    private void onDeleteConfirmed() {
        viewModel.deleteProduct();
    }

    private void updateQuantitiesList(PresentableProduct product) {
        quantitiesListViewAdapter.getQuantities().clear();
        quantitiesListViewAdapter.getQuantities().addAll(product.getQuantitiesInWarehousesAsList());
        quantitiesListViewAdapter.notifyDataSetChanged();
    }

    private PresentableProduct getProduct() {
        return viewModel.getProduct().getValue();
    }

    private void handleLoadProductFailure(String errorMessage) {
        showErrorMessage(errorMessage);
        finish();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
