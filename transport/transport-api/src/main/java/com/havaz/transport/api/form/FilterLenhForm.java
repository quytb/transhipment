package com.havaz.transport.api.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class FilterLenhForm {
    private int taiXeId;
    private int trangThai;
    private int kieuLenh;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayTao;

    private int tuyenId;
    private PagingForm paging = new PagingForm();
    private int lenhId;

    public int getTaiXeId() {
        return taiXeId;
    }

    public void setTaiXeId(int taiXeId) {
        this.taiXeId = taiXeId;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getKieuLenh() {
        return kieuLenh;
    }

    public void setKieuLenh(int kieuLenh) {
        this.kieuLenh = kieuLenh;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getTuyenId() {
        return tuyenId;
    }

    public void setTuyenId(int tuyenId) {
        this.tuyenId = tuyenId;
    }

    public PagingForm getPaging() {
        return paging;
    }

    public void setPaging(PagingForm paging) {
        this.paging = paging;
    }

    public int getLenhId() {
        return lenhId;
    }

    public void setLenhId(int lenhId) {
        this.lenhId = lenhId;
    }
}
