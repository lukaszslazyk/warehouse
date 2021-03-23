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

public class ProductSyncErrorsListViewAdapter extends RecyclerView.Adapter<ProductSyncErrorsListViewAdapter.ViewHolder> {
    private Context context;
    private List<String> errorMessages;

    ProductSyncErrorsListViewAdapter(Context context) {
        this.context = context;
        this.errorMessages = new ArrayList<>();
    }

    List<String> getErrorMessages() {
        return errorMessages;
    }

    @NonNull
    @Override
    public ProductSyncErrorsListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.product_sync_errors_list_row, parent, false);

        return new ProductSyncErrorsListViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductSyncErrorsListViewAdapter.ViewHolder holder, int position) {
        String errorMessage = errorMessages.get(position);
        holder.errorMessage.setText(errorMessage);
    }

    @Override
    public int getItemCount() {
        return errorMessages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView errorMessage;

        ViewHolder(View view) {
            super(view);
            errorMessage = view.findViewById(R.id.product_sync_error_message);
        }
    }
}
