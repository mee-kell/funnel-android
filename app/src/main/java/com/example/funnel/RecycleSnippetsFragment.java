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
    protected ArrayList<Snippet> allSnippets;
    protected ArrayList<Snippet> displaySnippets;
    private SelectGroupViewModel viewModel;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
//        if (allSnippets == null || allSnippets.size() == 0) {
//            allSnippets = new ArrayList<>();
//        }
        allSnippets = new ArrayList<>();
        displaySnippets = new ArrayList<>();
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
        adapter = new RecycleSnippetAdapter(displaySnippets);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    // Save all snippets in all groups belonging to the user.
    private void getAllUserSnippets() {
        viewModel.getUserSnapshot().observe(getViewLifecycleOwner(), userSnapshot -> {
            if (userSnapshot == null) {
                Log.d(TAG, "User snapshot is null.");
                return;
            }
            // Iterate through all groups owned by the user.
            for (DataSnapshot groupSnapshot : userSnapshot.getChildren()) {
                String groupName = groupSnapshot.getKey();
                assert groupName != null;

                // Iterate through all images in the selected group.
                for (DataSnapshot imageSnapshot : groupSnapshot.getChildren()) {
                    String summary = (String) imageSnapshot.child("summary").getValue();
                    String url = (String) imageSnapshot.child("imgPath").getValue();

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    String path = String.format("%s/%s/%s", user.getUid(), groupName, url);
                    StorageReference snippetRef = storageRef.child(path);

                    snippetRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Snippet newSnippet = new Snippet(
                                uri, summary, user.getUid(), groupName, imageSnapshot.getKey());
                        allSnippets.add(newSnippet);
                    });
                }
            }
        });
    }

    private void initDataset(String selectedGroup) {
        // Do not return images if no group is selected
        if (selectedGroup == null || selectedGroup.length() == 0) {
            Log.d(TAG, "No group selected.");
            return;
        }

        // Fetch all snippets for user if not yet fetched
        if (allSnippets == null || allSnippets.size() == 0) {
            getAllUserSnippets();
        }

        displaySnippets.clear();
        adapter.notifyDataSetChanged();

        for (Snippet snippet : allSnippets) {
            if (snippet.getGroupName().equals(selectedGroup)) {
                int index = displaySnippets.size();
                displaySnippets.add(index, snippet);
                adapter.notifyItemInserted(index);
            }
        }
    }

}