package com.havaz.transport.api.form;

public class FirebaseForm {
    private boolean content_available = true;
    private FirebaseNotificationForm notification;
    private int opened_from_tray = 1;
    private Object data;
    private String to;

    public FirebaseForm() {
    }

    public boolean isContent_available() {
        return content_available;
    }

    public void setContent_available(boolean content_available) {
        this.content_available = content_available;
    }

    public FirebaseNotificationForm getNotification() {
        return notification;
    }

    public void setNotification(FirebaseNotificationForm notification) {
        this.notification = notification;
    }

    public int getOpened_from_tray() {
        return opened_from_tray;
    }

    public void setOpened_from_tray(int opened_from_tray) {
        this.opened_from_tray = opened_from_tray;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
