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

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ListItem> items;
    private OnItemClickListener onItemClickListener;

    /**
     * Initialize the dataset of the Adapter
     *
     * @param items List<ListItem> containing the data to populate views to be used
     *                by RecyclerView
     */

    public ListAdapter(List<ListItem> items) {
        this.items = items;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.item_title);
            imageView = (ImageView) view.findViewById(R.id.item_image);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
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

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(items.get(position).getTitle());
        ListItem item = items.get(position);
        viewHolder.imageView.setImageResource(item.getImageResId());
        viewHolder.textView.setText(item.getTitle());

        viewHolder.itemView.setOnClickListener( v -> {
            Log.e("click", "Touched");
            item.setImageResId(R.drawable.button_press_background);
            notifyItemChanged(position);
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

}