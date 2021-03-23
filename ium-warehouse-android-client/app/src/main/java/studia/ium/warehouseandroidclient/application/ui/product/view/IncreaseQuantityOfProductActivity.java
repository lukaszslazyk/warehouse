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
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.IncreaseQuantityOfProductViewModel;
import studia.ium.warehouseandroidclient.databinding.ActivityIncreaseQuantityOfProductBinding;

import static java.util.Objects.requireNonNull;

public class IncreaseQuantityOfProductActivity extends AppCompatActivity {
    private static final String TITLE = "Increase quantity";
    @Inject
    IncreaseQuantityOfProductViewModel viewModel;
    private String productId;

    @BindView(R.id.product_amount_input)
    TextView productAmountInput;
    @BindView(R.id.product_warehouse_id_input)
    TextView productWarehouseIdInput;

    public static Intent getCallingIntent(Context context, String productId) {
        Intent intent = new Intent(context, IncreaseQuantityOfProductActivity.class);
        intent.putExtra("PRODUCT_ID", productId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_increase_quantity_of_product);
        initActivity();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    @OnClick(R.id.product_increase_quantity_save_button)
    void onSaveButtonClicked() {
        validateForm();
        if (formIsValid())
            save();
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
        ActivityIncreaseQuantityOfProductBinding binding =
            DataBindingUtil.setContentView(this, R.layout.activity_increase_quantity_of_product);
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
        initAmountValidationErrorMessageObserver();
        initWarehouseIdValidationErrorMessageObserver();
        initIncreaseQuantitySuccessObserver();
        initIncreaseQuantityErrorMessageObserver();
    }

    private void initAmountValidationErrorMessageObserver() {
        viewModel
            .getAmountValidationErrorMessage()
            .observe(this, message -> updateErrorMessageForTextInput(productAmountInput, message));
    }

    private void initWarehouseIdValidationErrorMessageObserver() {
        viewModel
            .getWarehouseIdValidationErrorMessage()
            .observe(this, message -> updateErrorMessageForTextInput(productAmountInput, message));
    }

    private void initIncreaseQuantitySuccessObserver() {
        viewModel
            .getIncreaseQuantitySuccess()
            .observe(this, increased -> finish());
    }

    private void initIncreaseQuantityErrorMessageObserver() {
        viewModel
            .getIncreaseQuantityErrorMessage()
            .observe(this, this::handleIncreaseQuantityFailure);
    }

    private void validateForm() {
        viewModel.validateForm();
    }

    private boolean formIsValid() {
        return viewModel.formIsValid();
    }

    private void save() {
        viewModel.save();
    }

    private void updateErrorMessageForTextInput(TextView textInput, String errorMessage) {
        if (!errorMessage.isEmpty())
            textInput.setError(errorMessage);
        else
            textInput.setError(null);
    }

    private void handleIncreaseQuantityFailure(String errorMessage) {
        showErrorMessage(errorMessage);
        finish();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
