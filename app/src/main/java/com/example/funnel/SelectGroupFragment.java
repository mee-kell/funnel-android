package com.example.funnel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectGroupFragment extends Fragment {
    private static final String TAG = "SelectGroupFragment";
    private FirebaseUser user;
    private final ArrayList<String> options = new ArrayList<>();
    private SelectGroupViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SelectGroupViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_group, container, false);
        rootView.setTag(TAG);

        initOptions();
        Spinner spinner = rootView.findViewById(R.id.group_menu);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Save the group name selected from menu.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String groupName = (String) parent.getItemAtPosition(pos);
                viewModel.setGroupName(groupName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                viewModel.setGroupName("");
            }
        });

        return rootView;
    }

    private void initOptions() {
        options.add("Select a group");
        if (user != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(user.getUid());
            ValueEventListener groupsListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userGroupsSnapshot) {
                    viewModel.setUserSnapshot(userGroupsSnapshot);
                    // Add list of groups associated with the current user
                    for (DataSnapshot groupSnapshot : userGroupsSnapshot.getChildren()) {
                        String groupName = groupSnapshot.getKey();
                        options.add(groupName);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };
            databaseRef.addValueEventListener(groupsListener);
        }
    }
}