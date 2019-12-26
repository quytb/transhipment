package com.havaz.transport.api.model;

 public class ConDataDTO {
    private String con_page_name;
    private String con_page_name_short;
    private String con_company_name;
    private String con_company_addr;
    private String con_code;

    public void setCon_page_name(String con_page_name) {
        this.con_page_name = con_page_name;
    }

    public void setCon_page_name_short(String con_page_name_short) {
        this.con_page_name_short = con_page_name_short;
    }

    public void setCon_company_name(String con_company_name) {
        this.con_company_name = con_company_name;
    }

    public void setCon_company_addr(String con_company_addr) {
        this.con_company_addr = con_company_addr;
    }

    public void setCon_code(String con_code) {
        this.con_code = con_code;
    }

    public String getCon_page_name() {
        return con_page_name;
    }

    public String getCon_page_name_short() {
        return con_page_name_short;
    }

    public String getCon_company_name() {
        return con_company_name;
    }

    public String getCon_company_addr() {
        return con_company_addr;
    }

    public String getCon_code() {
        return con_code;
    }

    public ConDataDTO() {
    }

    public ConDataDTO(String con_page_name, String con_page_name_short, String con_company_name, String con_company_addr, String con_code) {
        this.con_page_name = con_page_name;
        this.con_page_name_short = con_page_name_short;
        this.con_company_name = con_company_name;
        this.con_company_addr = con_company_addr;
        this.con_code = con_code;
    }
}
