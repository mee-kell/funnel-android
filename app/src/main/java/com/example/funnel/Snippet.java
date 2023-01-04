package com.example.funnel;

import android.net.Uri;

public class Snippet {

    private final Uri uri;
    private final String summary;
    private final String userId;
    private final String groupName;

    private final String imageName;

    public Snippet(Uri uri, String summary, String userId, String groupName, String imageName) {
        this.uri = uri;
        this.summary = summary;
        this.userId = userId;
        this.groupName = groupName;
        this.imageName = imageName;
    }

    public Uri getURI() {
        return uri;
    }

    public String getSummary() {
        return summary;
    }

    public String getUserId() {
        return userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getImageName() {
        return imageName;
    }

}
