package studia.ium.warehouseandroidclient.application.ui.product.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import studia.ium.warehouseandroidclient.R;
import studia.ium.warehouseandroidclient.application.ui.product.model.PresentableProduct;

public class ProductsListViewAdapter extends RecyclerView.Adapter<ProductsListViewAdapter.ViewHolder> {
    private Context context;
    private List<PresentableProduct> products;

    ProductsListViewAdapter(Context context) {
        this.context = context;
        this.products = new ArrayList<>();
    }

    List<PresentableProduct> getProducts() {
        return products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.product_list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PresentableProduct product = products.get(position);
        holder.manufacturerName.setText(product.getManufacturerName());
        holder.modelName.setText(product.getModelName());
        holder.setItemClickListener(getItemClickListener(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private View.OnClickListener getItemClickListener(PresentableProduct product) {
        return (view) -> startProductDetailActivity(product.getId());
    }

    private void startProductDetailActivity(String productId) {
        context.startActivity(ProductDetailsActivity.getCallingIntent(context, productId));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView manufacturerName;
        TextView modelName;

        ViewHolder(View view) {
            super(view);
            manufacturerName = view.findViewById(R.id.product_manufacturer_name);
            modelName = view.findViewById(R.id.product_model_name);
        }

        void setItemClickListener(View.OnClickListener itemOnClickListener) {
            itemView.setOnClickListener(itemOnClickListener);
        }
    }
}
