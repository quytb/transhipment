package com.havaz.transport.api.common;

public enum NotificationMainURL {
    URL_TCD("/trung-chuyen-don"), URL_TCT("/trung-chuyen-tra");


    private final String mainUrl;

    public String getMainUrl() {
        return mainUrl;
    }

    NotificationMainURL(String mainUrl) {
        this.mainUrl = mainUrl;
    }
}
