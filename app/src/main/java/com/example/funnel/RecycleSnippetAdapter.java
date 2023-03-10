package com.example.funnel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleSnippetAdapter extends RecyclerView.Adapter<RecycleSnippetAdapter.ViewHolder> {

    private ArrayList<Snippet> snippets = new ArrayList<>();

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        private final FloatingActionButton editButton;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.snippet_summary);
            imageView = view.findViewById(R.id.snippet_image);
            editButton = view.findViewById(R.id.editButton);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public FloatingActionButton getEditButton() {
            return editButton;
        }
    }

    public RecycleSnippetAdapter(ArrayList<Snippet> dataset) {
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
        Snippet currentSnippet = snippets.get(position);
        Uri imageUri = currentSnippet.getURI();
        ImageView imageView = viewHolder.getImageView();
        Picasso.with(imageView.getContext())
                .load(imageUri)
                .noPlaceholder()
                .fit().centerInside()
                .into(imageView);
        String currentSummary = currentSnippet.getSummary();
        viewHolder.getTextView().setText(currentSummary);

        FloatingActionButton editButton = viewHolder.getEditButton();
        Context buttonContext = editButton.getContext();
        editButton.setOnClickListener(view -> {
            Intent editIntent = new Intent(buttonContext, EditSummaryActivity.class);
            editIntent.putExtra("userId", currentSnippet.getUserId());
            editIntent.putExtra("groupName", currentSnippet.getGroupName());
            editIntent.putExtra("imageName", currentSnippet.getImageName());
            editIntent.putExtra("imageUri", imageUri.toString());
            editIntent.putExtra("currentSummary", currentSummary);
            buttonContext.startActivity(editIntent);
        });
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return snippets.size();
    }
}
