package com.example.funnel;

import android.net.Uri;

public class Snippet {

    private final Uri uri;
    private final String summary;
    private final String summaryPath;

    public Snippet(Uri uri, String summary, String summaryPath) {
        this.uri = uri;
        this.summary = summary;
        this.summaryPath = summaryPath;
    }

    public Uri getURI() {
        return uri;
    }

    public String getSummary() {
        return summary;
    }

    public String getSummaryPath() {
        return summaryPath;
    }

}
