package com.havaz.transport.api.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class FirebaseNotificationForm implements Serializable {
    private String title;
    private String body;
    @JsonProperty("android_channel_id")
    private String androidChannelId;
    private String sound;

    public FirebaseNotificationForm(String title, String body, String androidChannelId, String sound) {
        this.title = title;
        this.body = body;
        this.androidChannelId = androidChannelId;
        this.sound = sound;
    }

    public String getAndroidChannelId() {
        return androidChannelId;
    }

    public void setAndroidChannelId(String androidChannelId) {
        this.androidChannelId = androidChannelId;
    }

    public FirebaseNotificationForm(String title, String body, String sound) {
        this.title = title;
        this.body = body;
        this.sound = sound;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
