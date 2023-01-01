package com.example.funnel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleSnippetAdapter extends RecyclerView.Adapter<RecycleSnippetAdapter.ViewHolder> {

    private Snippet[] snippets = {};

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.snippet_summary);
            imageView = view.findViewById(R.id.snippet_image);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public RecycleSnippetAdapter(Snippet[] dataset) {
        if (dataset != null) {
            snippets = dataset;
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_snippets_card, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getImageView().setImageURI(snippets[position].getURI());
        viewHolder.getTextView().setText(snippets[position].getSummary());
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return snippets.length;
    }
}
