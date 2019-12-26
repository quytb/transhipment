package com.havaz.transport.api.form;

public class FirebaseNotiDataForm extends AbstractNotificationFirebase {
    private String title;
    private String content;
    private int type;
    private boolean isHavazNow;

    public FirebaseNotiDataForm() {
    }

    public FirebaseNotiDataForm(String title, String content, int type, boolean isHavazNow) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.isHavazNow = isHavazNow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getIsHavazNow() {
        return isHavazNow;
    }

    public void setIsHavazNow(boolean isHavazNow) {
        this.isHavazNow = isHavazNow;
    }
}
