package studia.ium.warehouseandroidclient.application.configuration.di.product;

import javax.inject.Singleton;

import dagger.Component;
import studia.ium.warehouseandroidclient.application.ui.product.view.AddProductActivity;
import studia.ium.warehouseandroidclient.application.ui.product.view.DecreaseQuantityOfProductActivity;
import studia.ium.warehouseandroidclient.application.ui.product.view.EditProductActivity;
import studia.ium.warehouseandroidclient.application.ui.product.view.IncreaseQuantityOfProductActivity;
import studia.ium.warehouseandroidclient.application.ui.product.view.ProductDetailsActivity;
import studia.ium.warehouseandroidclient.application.ui.product.view.ProductSyncErrorsListActivity;
import studia.ium.warehouseandroidclient.application.ui.product.view.ProductsListActivity;

@Singleton
@Component(modules = ProductModule.class)
public interface ProductComponent {
    void inject(ProductsListActivity productsListActivity);

    void inject(ProductDetailsActivity productDetailsActivity);

    void inject(AddProductActivity addProductActivity);

    void inject(EditProductActivity editProductActivity);

    void inject(IncreaseQuantityOfProductActivity increaseQuantityOfProductActivity);

    void inject(DecreaseQuantityOfProductActivity decreaseQuantityOfProductActivity);

    void inject(ProductSyncErrorsListActivity productSyncErrorsListActivity);
}
