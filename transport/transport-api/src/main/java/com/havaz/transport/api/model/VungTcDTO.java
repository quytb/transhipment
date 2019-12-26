package com.havaz.transport.api.model;

public class VungTcDTO {
    private String tcVttName;
    private String tcVttCode;
    private String tcVttContent;
    private String locale;
    private String tcVttNote;
    private int status;

    public VungTcDTO() {
    }

    public String getTcVttName() {
        return tcVttName;
    }

    public void setTcVttName(String tcVttName) {
        this.tcVttName = tcVttName;
    }

    public String getTcVttCode() {
        return tcVttCode;
    }

    public void setTcVttCode(String tcVttCode) {
        this.tcVttCode = tcVttCode;
    }

    public String getTcVttContent() {
        return tcVttContent;
    }

    public void setTcVttContent(String tcVttContent) {
        this.tcVttContent = tcVttContent;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTcVttNote() {
        return tcVttNote;
    }

    public void setTcVttNote(String tcVttNote) {
        this.tcVttNote = tcVttNote;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
