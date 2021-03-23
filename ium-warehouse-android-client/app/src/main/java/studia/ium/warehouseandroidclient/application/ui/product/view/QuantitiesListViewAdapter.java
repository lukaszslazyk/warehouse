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
import java.util.Map;

import studia.ium.warehouseandroidclient.R;

public class QuantitiesListViewAdapter extends RecyclerView.Adapter<QuantitiesListViewAdapter.ViewHolder> {
    private Context context;
    private List<Map.Entry<Integer, Integer>> quantities;

    QuantitiesListViewAdapter(Context context) {
        this.context = context;
        this.quantities = new ArrayList<>();
    }

    List<Map.Entry<Integer, Integer>> getQuantities() {
        return quantities;
    }

    @NonNull
    @Override
    public QuantitiesListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.quantities_list_row, parent, false);

        return new QuantitiesListViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuantitiesListViewAdapter.ViewHolder holder, int position) {
        Map.Entry<Integer, Integer> quantityInWarehouse = quantities.get(position);
        holder.warehouseId.setText(getTextForWarehouseId(quantityInWarehouse));
        holder.quantity.setText(quantityInWarehouse.getValue().toString());
    }

    private String getTextForWarehouseId(Map.Entry<Integer, Integer> quantityInWarehouse) {
        return String.format("Warehouse No. %d:", quantityInWarehouse.getKey());
    }

    @Override
    public int getItemCount() {
        return quantities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView warehouseId;
        TextView quantity;

        ViewHolder(View view) {
            super(view);
            warehouseId = view.findViewById(R.id.warehouse_id);
            quantity = view.findViewById(R.id.quantity);
        }
    }
}
