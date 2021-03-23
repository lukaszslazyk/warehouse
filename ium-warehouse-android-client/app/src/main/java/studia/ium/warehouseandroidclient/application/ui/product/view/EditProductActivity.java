package studia.ium.warehouseandroidclient.application.ui.product.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studia.ium.warehouseandroidclient.R;
import studia.ium.warehouseandroidclient.application.configuration.WarehouseClientApplication;
import studia.ium.warehouseandroidclient.application.ui.product.model.PresentableProduct;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.EditProductViewModel;
import studia.ium.warehouseandroidclient.databinding.ActivityEditProductBinding;

import static java.util.Objects.requireNonNull;

public class EditProductActivity extends AppCompatActivity {
    private static final String TITLE = "Edit product";
    @Inject
    EditProductViewModel viewModel;
    private PresentableProduct product;

    @BindView(R.id.product_manufacturer_name_input)
    TextView productManufacturerNameInput;
    @BindView(R.id.product_model_name_input)
    TextView productModelNameInput;
    @BindView(R.id.product_price_input)
    TextView productPriceInput;
    @BindView(R.id.product_expiration_date_input)
    TextView productExpirationDateInput;

    public static Intent getCallingIntent(Context context, PresentableProduct product) {
        Intent intent = new Intent(context, EditProductActivity.class);
        intent.putExtra("PRODUCT", product);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        initActivity();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @OnClick(R.id.product_edit_save_button)
    void onSaveButtonClicked() {
        validateProduct();
        if (productIsValid())
            saveProduct();
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
        product = (PresentableProduct) getIntent().getSerializableExtra("PRODUCT");
    }

    private void initViewModel() {
        viewModel
            .withProduct(product)
            .init();
    }

    private void initDataBinding() {
        ActivityEditProductBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_product);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void initViews() {
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(TITLE);
        setSupportActionBar(toolbar);
        requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void initViewModelObservers() {
        initManufacturerNameValidationErrorMessageObserver();
        initModelNameValidationErrorMessageObserver();
        initExpirationDateValidationErrorMessageObserver();
        initSaveProductSuccessObserver();
        initSaveProductErrorMessageObserver();
    }

    private void initManufacturerNameValidationErrorMessageObserver() {
        viewModel
            .getManufacturerNameValidationErrorMessage()
            .observe(this, message -> updateErrorMessageForTextInput(productManufacturerNameInput, message));
    }

    private void initModelNameValidationErrorMessageObserver() {
        viewModel
            .getModelNameValidationErrorMessage()
            .observe(this, message -> updateErrorMessageForTextInput(productModelNameInput, message));
    }

    private void initExpirationDateValidationErrorMessageObserver() {
        viewModel
            .getExpirationDateValidationErrorMessage()
            .observe(this, message -> updateErrorMessageForTextInput(productExpirationDateInput, message));
    }

    private void initSaveProductSuccessObserver() {
        viewModel
            .getSaveProductSuccess()
            .observe(this, saved -> finish());
    }

    private void initSaveProductErrorMessageObserver() {
        viewModel
            .getSaveProductErrorMessage()
            .observe(this, this::handleSaveProductFailure);
    }

    private void validateProduct() {
        viewModel.validateProduct();
    }

    private boolean productIsValid() {
        return viewModel.productIsValid();
    }

    private void saveProduct() {
        viewModel.saveProduct();
    }

    private void updateErrorMessageForTextInput(TextView textInput, String errorMessage) {
        if (!errorMessage.isEmpty())
            textInput.setError(errorMessage);
        else
            textInput.setError(null);
    }

    private void handleSaveProductFailure(String errorMessage) {
        showErrorMessage(errorMessage);
        finish();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
