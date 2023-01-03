package com.example.funnel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RecycleSnippetsFragment extends Fragment {
    private static final String TAG = "RecycleSnippetsFragment";
    protected RecyclerView recyclerView;
    protected RecycleSnippetAdapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<Snippet> dataset;
    private SelectGroupViewModel viewModel;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        dataset = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(SelectGroupViewModel.class);
        // Update the images shown.
        viewModel.getSelectedGroup().observe(getViewLifecycleOwner(), this::initDataset);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycle_snippets, container, false);
        rootView.setTag(TAG);

        recyclerView = rootView.findViewById(R.id.recycleSnippetsView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecycleSnippetAdapter(dataset);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void initDataset(String selectedGroup) {
        Set<Snippet> snippetSet = new HashSet<>();
        // Do not return images if no group is selected
        if (selectedGroup == null || selectedGroup.length() == 0) {
            Log.d(TAG, "No group selected.");
            return;
        }
        viewModel.getUserSnapshot().observe(getViewLifecycleOwner(), userSnapshot -> {
            if (userSnapshot == null) {
                Log.d(TAG, "User snapshot is null.");
                return;
            }
            // Iterate through all groups owned by the user.
            Log.d(TAG, "Selected group is " + selectedGroup);
            dataset.clear();
            adapter.notifyDataSetChanged();

            for (DataSnapshot groupSnapshot : userSnapshot.getChildren()) {
                String groupName = groupSnapshot.getKey();
                assert groupName != null;
                Log.d(TAG, "Group name is: " + groupName);
                if (!groupName.equals(selectedGroup)) {
                    continue;
                }
                // Iterate through all images in the selected group.
                for (DataSnapshot imageSnapshot : groupSnapshot.getChildren()) {
                    String summary = (String) imageSnapshot.child("summary").getValue();
                    String summaryPath = String.format("%s/%s/%s/summary",
                            user.getUid(), groupName, imageSnapshot.getKey());

                    Log.d(TAG, "Summary: " + summary);

                    String url = (String) imageSnapshot.child("imgPath").getValue();
                    String path = String.format("%s/%s/%s", user.getUid(), groupName, url);

                    Log.d(TAG, "Image storage path: " + path);

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference snippetRef = storageRef.child(path);

                    Log.d(TAG, "Storage reference: " + snippetRef);

                    snippetRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Snippet newSnippet = new Snippet(uri, summary, summaryPath);
                        Log.d(TAG, "Snippet uri: " + uri.toString());
//                        snippetSet.add(newSnippet);
//                        Log.d(TAG, "Add to snippet set: " + newSnippet.getURI());

                        int index = dataset.size();
                        dataset.add(index, newSnippet);
                        adapter.notifyItemInserted(index);
                    });
                }
            }

//            dataset.clear();
//            Log.d(TAG, "Final snippet set: " + snippetSet);
//            dataset.addAll(snippetSet);
//            Log.d(TAG, "New dataset: " + dataset);
//            adapter.notifyDataSetChanged();
        });

    }

}