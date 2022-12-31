package com.example.funnel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecycleSnippetsFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecycleSnippetsFragment extends Fragment {
    private static final String TAG = "RecycleSnippetsFragment";
    protected RecyclerView recyclerView;
    protected RecycleSnippetAdapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected String[] dataset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
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

    private void initDataset() {
        final int DATASET_COUNT = 60;
        dataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            dataset[i] = "This is element #" + i;
        }
    }

}