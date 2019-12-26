package com.havaz.transport.api.form;

import java.util.List;

public class FirebaseDataForm {
    private List<?> data;
    private String to;

    public FirebaseDataForm(List<?> data, String to) {
        this.data = data;
        this.to = to;
    }

    public FirebaseDataForm() {

    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
