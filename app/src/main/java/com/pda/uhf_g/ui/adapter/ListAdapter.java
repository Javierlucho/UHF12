package com.pda.uhf_g.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pda.uhf_g.R;
import com.pda.uhf_g.data.local.entities.ListItem;
import com.pda.uhf_g.ui.viewmodel.InventoryViewModel;

import org.w3c.dom.Text;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final InventoryViewModel viewModel;
    private List<ListItem> items;

    /**
     * Initialize the dataset of the Adapter
     *
     * @param items     List<ListItem> containing the data to populate views to be used
     *                  by RecyclerView
     * @param viewModel
     */

    public ListAdapter(List<ListItem> items, InventoryViewModel viewModel) {
        this.items = items;
        this.viewModel = viewModel;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final ImageView imageView;
        private final TextView description;
        private final TextView brand;
        private final TextView serial_number;
        private final TextView codigo_campo;

        public ViewHolder(View view) {
            super(view);

            // Define click listener for the ViewHolder's View
            title = (TextView) view.findViewById(R.id.item_title);
            imageView = (ImageView) view.findViewById(R.id.item_image);
            description = (TextView) view.findViewById(R.id.item_description);
            brand = (TextView) view.findViewById(R.id.item_brand);
            serial_number = (TextView) view.findViewById(R.id.item_serial_number);
            codigo_campo = (TextView) view.findViewById(R.id.item_codigo_campo);

        }

        public TextView getTitle() {
            return title;
        }
        public ImageView getImageView() {
            return imageView;
        }
        public TextView getDescription() {
            return description;
        }
        public TextView getBrand() {
            return brand;
        }
        public TextView getSerial() {
            return serial_number;
        }
        public TextView getCodigoCampo() {
            return codigo_campo;
        }

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public void deselectItem(ListItem item) {
        item.setImageResId(R.drawable.button_disenabled_background);
        item.setSelected(false);
    }

    public void selectItem(ListItem item) {
        item.setImageResId(R.drawable.button_press_background);
        item.setSelected(true);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        ListItem item = items.get(position);
        viewHolder.getImageView().setImageResource(item.getImageResId());
        viewHolder.getTitle().setText(item.getTitle());
        viewHolder.getDescription().setText(item.getDescription());
        viewHolder.getBrand().setText(item.getBrand());
        viewHolder.getSerial().setText(item.getSerial());
        viewHolder.getCodigoCampo().setText(item.getFarmCode());

        viewHolder.itemView.setOnClickListener( v -> {

            if (item.isSelected()) {
                Log.e("touch", "Deselected");
                deselectItem(item);
                viewModel.deselectItem();
            } else {
                Log.e("touch", "Selected");

                ListItem old_selected = viewModel.getPotentialSelectedItem();
                if (old_selected != null) {
                    deselectItem(old_selected);
                    notifyItemChanged(old_selected.getPosition());
                }
                selectItem(item);
                viewModel.setPotentialSelectedItem(item);

            }
            notifyItemChanged(position);

        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

}