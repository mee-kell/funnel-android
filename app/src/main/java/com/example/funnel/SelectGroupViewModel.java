package com.example.funnel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
public class SelectGroupViewModel extends ViewModel {

    private final MutableLiveData<String> groupName = new MutableLiveData<>();
    private final MutableLiveData<DataSnapshot> userSnapshot = new MutableLiveData<>();

    public LiveData<String> getSelectedGroup() {
        return groupName;
    }

    public LiveData<DataSnapshot> getUserSnapshot() {
        return userSnapshot;
    }

    public void setGroupName(String name) {
        groupName.setValue(name);
    }

    public void setUserSnapshot(DataSnapshot snapshot) {
        userSnapshot.setValue(snapshot);
    }

}
