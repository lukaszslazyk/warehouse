package studia.ium.warehouseandroidclient.application.ui.product.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
import studia.ium.warehouseandroidclient.R;
import studia.ium.warehouseandroidclient.application.configuration.WarehouseClientApplication;
import studia.ium.warehouseandroidclient.application.ui.product.viewmodel.ProductSyncErrorsListViewModel;

import static java.util.Objects.requireNonNull;

public class ProductSyncErrorsListActivity extends AppCompatActivity {
    private static final String TITLE = "Synchronization errors";
    @Inject
    ProductSyncErrorsListViewModel viewModel;

    @BindView(R.id.product_sync_errors_list)
    RecyclerView productSyncErrorsListView;
    private ProductSyncErrorsListViewAdapter productSyncErrorsListViewAdapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ProductSyncErrorsListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_sync_errors_list);
        initActivity();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    private void initActivity() {
        injectDependencies();
        initViews();
        initViewModelObservers();
        loadSyncErrorMessages();
    }

    private void injectDependencies() {
        ((WarehouseClientApplication) getApplication()).getProductComponent().inject(this);
    }

    private void initViews() {
        ButterKnife.bind(this);
        initToolbar();
        initProductSyncErrorsListView();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(TITLE);
        setSupportActionBar(toolbar);
        requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void initProductSyncErrorsListView() {
        productSyncErrorsListView.setHasFixedSize(true);
        productSyncErrorsListView.setLayoutManager(new LinearLayoutManager(this));
        productSyncErrorsListView.setItemAnimator(new DefaultItemAnimator());
        productSyncErrorsListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        productSyncErrorsListViewAdapter = new ProductSyncErrorsListViewAdapter(this);
        productSyncErrorsListView.setAdapter(productSyncErrorsListViewAdapter);
    }

    private void initViewModelObservers() {
        initProductsObserver();
    }

    private void initProductsObserver() {
        viewModel
            .getSyncErrorMessages()
            .observe(this, this::updateProductSyncErrorsListView);
    }

    private void updateProductSyncErrorsListView(List<String> errorMessages) {
        productSyncErrorsListViewAdapter.getErrorMessages().clear();
        productSyncErrorsListViewAdapter.getErrorMessages().addAll(errorMessages);
        productSyncErrorsListViewAdapter.notifyDataSetChanged();
    }


    private void loadSyncErrorMessages() {
        viewModel.loadSyncErrorMessages();
    }
}
