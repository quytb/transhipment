package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tc_ve")
@EntityListeners(AuditingEntityListener.class)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "getKhachTrungChuyen",
                query = "SELECT " +
                        "tcv.bvv_ten_khach_hang_di tenKhachDi, " +
                        "tcv.bvv_phone_di sdtKhachDi, " +
                        "tcv.bvv_diem_don_khach diaChiDon, " +
                        "tcv.thu_tu_don thuTuDon, " +
                        "tcv.thoi_gian_don thoiGianDon, " +
                        "tcv.tc_trang_thai_don trangThaiTrungChuyen, " +
                        "tcv.tc_lenh_id lenhId, " +
                        "tcv.lai_xe_id_don taiXeId, " +
                        "T.tuy_ten tuyenDi, " +
                        "DD.did_gio_xuat_ben_that gioXuatBen, " +
                        "XE.xe_bien_kiem_soat bienSoXe, " +
                        "tcv.tc_khach_hang_moi khachHangMoi, " +
                        "GROUP_CONCAT(tcv.bvv_ma_ghe SEPARATOR ',') AS soGhe, " +
                        "COUNT(tcv.bvv_diem_don_khach) AS soKhach, " +
                        "GROUP_CONCAT(tcv.bvv_id SEPARATOR ',') AS veId, " +
                        "XE.xe_so_dien_thoai AS soDTXe, " +
                        "DD.did_gio_dieu_hanh gioDieuHanh, " +
                        "tcv.ghi_chu_don ghiChuVe, " +
                        "tcv.bvv_ghi_chu ghiChuVeBvv," +
                        "tcv.bvv_lat_start latStart," +
                        "tcv.bvv_long_start lngStart, " +
                        "tcv.tc_distance_don, " +
                        "tcv.tc_distance_to_hub_don, " +
                        "tcv.tc_time_to_hub_don, " +
                        "lenh.created_date, " +
                        "T.tuy_thoi_gian_chay, " +
                        "tcv.tc_hub_diem_don, " +
                        "FROM_UNIXTIME(DD.did_time)," +
                        "tcv.tc_is_havaz_now " +
                        " FROM " +
                        "tc_ve tcv " +
                        " LEFT JOIN " +
                        " tc_lenh lenh " +
                        " ON tcv.tc_lenh_id = lenh.tc_lenh_id " +
                        " LEFT JOIN " +
                        " dieu_do_temp DD " +
                        " ON tcv.did_id = DD.did_id " +
                        " LEFT JOIN " +
                        " xe XE " +
                        " ON DD.did_xe_id = XE.xe_id " +
                        " LEFT JOIN " +
                        " not_tuyen NOT_T " +
                        " ON DD.did_not_id = NOT_T.not_id " +
                        " LEFT JOIN " +
                        " tuyen T " +
                        " ON NOT_T.not_tuy_id = T.tuy_id " +
                        " WHERE " +
                        " tcv.tc_trang_thai_don IN (1,2,3,5) AND " +
                        " tcv.tc_lenh_id = :tc_lenh_id AND " +
                        " lenh.kieu_lenh = 1 AND " +
                        " tcv.is_tc_don = 1 " +
                        " GROUP BY sdtKhachDi, diaChiDon, tcv.tc_trang_thai_don ORDER BY thuTuDon "
        ),
        @NamedNativeQuery(
                name = "getKhachTrungChuyenTra",
                query = "SELECT " +
                        "tcv.bvv_ten_khach_hang_di tenKhachDi, " +
                        "tcv.bvv_phone_di sdtKhachDi, " +
                        "tcv.bvv_diem_tra_khach diaChiTra, " +
                        "tcv.thu_tu_tra thuTuTra, " +
                        "tcv.thoi_gian_tra thoiGianTra, " +
                        "tcv.tc_trang_thai_tra trangThaiTrungChuyen, " +
                        "tcv.tc_lenh_tra_id tcLenhTraId, " +
                        "tcv.lai_xe_id_tra taiXeId, " +
                        "T.tuy_ten tuyenDi, " +
                        "DD.did_gio_xuat_ben_that gioXuatBen, " +
                        "XE.xe_bien_kiem_soat bienSoXe, " +
                        "tcv.tc_khach_hang_moi khachHangMoi, " +
                        "GROUP_CONCAT(tcv.bvv_ma_ghe SEPARATOR ',') AS soGhe, " +
                        "COUNT(tcv.bvv_diem_tra_khach) AS soKhach, " +
                        "GROUP_CONCAT(tcv.bvv_id SEPARATOR ',') AS veId, " +
                        "XE.xe_so_dien_thoai AS soDTXe, " +
                        "DD.did_gio_dieu_hanh gioDieuHanh, " +
                        "tcv.ghi_chu_tra ghiChuVe, " +
                        "tcv.bvv_ghi_chu ghiChuVeBvv, " +
                        "tcv.bvv_lat_end latEnd," +
                        "tcv.bvv_long_end lngEnd, " +
                        "tcv.tc_distance_tra, " +
                        "tcv.tc_distance_to_hub_tra, " +
                        "tcv.tc_time_to_hub_tra, " +
                        "lenh.created_date, " +
                        "T.tuy_thoi_gian_chay, " +
                        "tcv.tc_hub_diem_tra " +
                        " FROM " +
                        "tc_ve tcv" +
                        " LEFT JOIN " +
                        " tc_lenh lenh " +
                        " ON tcv.tc_lenh_tra_id = lenh.tc_lenh_id " +
                        " LEFT JOIN " +
                        " dieu_do_temp DD " +
                        " ON tcv.did_id = DD.did_id " +
                        " LEFT JOIN " +
                        " xe XE " +
                        " ON DD.did_xe_id = XE.xe_id " +
                        " LEFT JOIN " +
                        " not_tuyen NOT_T " +
                        " ON DD.did_not_id = NOT_T.not_id " +
                        " LEFT JOIN " +
                        " tuyen T " +
                        " ON NOT_T.not_tuy_id = T.tuy_id " +
                        " WHERE " +
                        " tcv.tc_trang_thai_tra IN (1,2,3) AND " +
                        " tcv.tc_lenh_tra_id = :tc_lenh_tra_id AND " +
                        " lenh.kieu_lenh = 2 AND " +
                        " tcv.is_tc_tra = 1 " +
                        " GROUP BY sdtKhachDi, diaChiTra, tcv.tc_trang_thai_tra ORDER BY thuTuTra "
        ),
        @NamedNativeQuery(
                name = "getKhachDangDon",
                query = "SELECT " +
                        "tcv.bvv_ten_khach_hang_di tenKhachDi," +
                        "tcv.bvv_phone_di sdtKhachDi," +
                        "tcv.bvv_diem_don_khach diaChiDon," +
                        "tcv.thu_tu_don thuTuDon," +
                        "tcv.thoi_gian_don thoiGianDon," +
                        "tcv.tc_trang_thai_don trangThaiTrungChuyen," +
                        "tcv.tc_lenh_id lenhId," +
                        "tcv.lai_xe_id_don taiXeId," +
                        "T.tuy_ten tuyenDi," +
                        "DD.did_gio_xuat_ben_that gioXuatBen," +
                        "XE.xe_bien_kiem_soat bienSoXe," +
                        "tcv.tc_khach_hang_moi khachHangMoi," +
                        "GROUP_CONCAT(tcv.bvv_ma_ghe SEPARATOR ',') AS soGhe," +
                        "COUNT(tcv.bvv_diem_don_khach) AS soKhach," +
                        "GROUP_CONCAT(tcv.bvv_id SEPARATOR ',') AS veId," +
                        "XE.xe_so_dien_thoai AS soDTXe," +
                        "DD.did_gio_dieu_hanh gioDieuHanh" +
                        " FROM " +
                        "tc_ve tcv" +
                        " LEFT JOIN " +
                        " tc_lenh lenh " +
                        " ON tcv.tc_lenh_id = lenh.tc_lenh_id " +
                        " LEFT JOIN " +
                        " dieu_do_temp DD " +
                        " ON tcv.did_id = DD.did_id " +
                        " LEFT JOIN " +
                        " xe XE" +
                        " ON DD.did_xe_id = XE.xe_id " +
                        " LEFT JOIN " +
                        " not_tuyen NOT_T " +
                        " ON DD.did_not_id = NOT_T.not_id " +
                        " LEFT JOIN " +
                        " tuyen T " +
                        " ON NOT_T.not_tuy_id = T.tuy_id " +
                        " WHERE " +
                        " tcv.tc_trang_thai_don = 3 AND " +
                        " tcv.tc_lenh_id = :tc_lenh_id AND " +
                        " lenh.kieu_lenh = 1 AND " +
                        " tcv.is_tc_don = 1 " +
                        " GROUP BY sdtKhachDi, diaChiDon, tcv.tc_trang_thai_don ORDER BY bienSoXe"
        ),
        @NamedNativeQuery(
                name = "getListTripDonObject",
                query = "SELECT GROUP_CONCAT(IFNULL(TV.bvv_phone_di,'-')), " +
                        "GROUP_CONCAT(IFNULL(LENH.last_updated_date,'')), " +
                        "GROUP_CONCAT(IFNULL(TV.thoi_gian_don,'-')), " +
                        "XE.xe_bien_kiem_soat, " +
                        "XE.xe_so_dien_thoai, " +
                        "TV.lai_xe_id_don, " +
                        "XE.xe_trung_tam, " +
                        "LENH.trang_thai, " +
                        "GROUP_CONCAT(IFNULL(TV.thu_tu_don,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.bvv_long_start,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.bvv_lat_start,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.tc_trang_thai_don,'-')), " +
                        "ADMIN.adm_name, " +
                        "GROUP_CONCAT(IFNULL(TV.bvv_source,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.tc_distance_don,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.bvv_id,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.tc_is_picked,'-')) " +
                        "FROM tc_ve TV " +
                        "LEFT JOIN tc_lenh LENH ON LENH.tc_lenh_id = TV.tc_lenh_id " +
                        "LEFT JOIN xe XE ON LENH.xe_id = XE.xe_id " +
                        "LEFT JOIN admin_lv2_user ADMIN ON ADMIN.adm_id = TV.lai_xe_id_don " +
                        "WHERE TV.did_id = :tripId " +
                        "AND XE.xe_trung_tam IN :listAcceptXe " +
                        "AND TV.lai_xe_id_don IS NOT NULL " +
                        "AND is_tc_don = 1 " +
                        "GROUP BY TV.lai_xe_id_don "
        ),
        @NamedNativeQuery(
                name = "getListTripTraObject",
                query = "SELECT GROUP_CONCAT(IFNULL(TV.bvv_phone_di,'-')), " +
                        "GROUP_CONCAT(IFNULL(LENH.last_updated_date,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.thoi_gian_tra,'-')), " +
                        "XE.xe_bien_kiem_soat, " +
                        "XE.xe_so_dien_thoai, " +
                        "TV.lai_xe_id_tra, " +
                        "XE.xe_trung_tam, " +
                        "LENH.trang_thai, " +
                        "GROUP_CONCAT(IFNULL(TV.thu_tu_tra,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.bvv_long_end,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.bvv_lat_end,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.tc_trang_thai_tra,'-')), " +
                        "ADMIN.adm_name, " +
                        "GROUP_CONCAT(IFNULL(TV.bvv_source,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.tc_distance_tra,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.bvv_id,'-')), " +
                        "GROUP_CONCAT(IFNULL(TV.tc_is_dropped,'-')) " +
                        "FROM tc_ve TV " +
                        "LEFT JOIN tc_lenh LENH ON LENH.tc_lenh_id = TV.tc_lenh_tra_id " +
                        "LEFT JOIN xe XE ON LENH.xe_id = XE.xe_id " +
                        "LEFT JOIN admin_lv2_user ADMIN ON ADMIN.adm_id = TV.lai_xe_id_tra " +
                        "WHERE TV.did_id = :tripId " +
                        "AND XE.xe_trung_tam IN :listAcceptXe " +
                        "AND TV.lai_xe_id_tra IS NOT NULL " +
                        "AND is_tc_tra = 1 " +
                        "GROUP BY TV.lai_xe_id_tra "
        )
})
@Getter
@Setter
public class TcVeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tc_ve_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tcVeId;

    @Column(name = "bvv_id")
    private Integer bvvId;

    @Column(name = "did_id")
    private Integer didId;

    @Column(name = "is_tc_don", columnDefinition = "INTEGER")
    private Boolean isTcDon;

    @Column(name = "is_tc_tra", columnDefinition = "INTEGER")
    private Boolean isTcTra;

    @Column(name = "bvv_ten_khach_hang_di")
    private String bvvTenKhachHangDi;

    @Column(name = "bvv_phone_di")
    private String bvvPhoneDi;

    @Column(name = "bvv_diem_don_khach")
    private String bvvDiemDonKhach;

    @Column(name = "bvv_diem_tra_khach")
    private String bvvDiemTraKhach;

    @Column(name = "bvv_ma_ghe")
    private String bvvMaGhe;

    @Column(name = "bvv_ghi_chu", columnDefinition = "TEXT")
    private String bvvGhiChu;

    @Column(name = "ve_action")
    private Integer veAction;

    @Column(name = "tc_lenh_id")
    private Integer tcLenhId;

    @Column(name = "tc_trang_thai_don")
    private Integer tcTrangThaiDon;

    @Column(name = "ly_do_tu_choi_don")
    private String lyDoTuChoiDon;

    @Column(name = "thu_tu_don")
    private Integer thuTuDon;

    @Column(name = "thoi_gian_don")
    private Integer thoiGianDon;

    @Column(name = "lai_xe_id_don")
    private Integer laiXeIdDon;

    @Column(name = "ghi_chu_don", columnDefinition = "TEXT")
    private String ghiChuDon;

    @Column(name = "tc_trang_thai_tra")
    private Integer tcTrangThaiTra;

    @Column(name = "ly_do_tu_choi_tra")
    private String lyDoTuChoiTra;

    @Column(name = "thu_tu_tra")
    private Integer thuTuTra;

    @Column(name = "thoi_gian_tra")
    private Integer thoiGianTra;

    @Column(name = "lai_xe_id_tra")
    private Integer laiXeIdTra;

    @Column(name = "ghi_chu_tra", columnDefinition = "TEXT")
    private String ghiChuTra;

    @Column(name = "tc_khach_hang_moi")
    private Integer khachHangMoi;

    @Column(name = "tc_lenh_tra_id")
    private Integer tcLenhTraId;

    @OneToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "lai_xe_id_don", insertable = false, updatable = false)
    private AdminLv2UserEntity adminLv2UserDon;

    @OneToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "lai_xe_id_tra", insertable = false, updatable = false)
    private AdminLv2UserEntity adminLv2UserTra;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "last_updated_by")
    @LastModifiedBy
    private Integer lastUpdatedBy;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "bvv_long_start")
    private Double bvvLongStart;

    @Column(name = "bvv_lat_start")
    private Double bvvLatStart;

    @Column(name = "bvv_long_end")
    private Double bvvLongEnd;

    @Column(name = "bvv_lat_end")
    private Double bvvLatEnd;

    @Column(name = "bvv_source")
    private Integer bvvSource;

    @Column(name = "tc_vtt_id_don")
    private Integer vungTCDon;

    @Column(name = "tc_vtt_id_tra")
    private Integer vungTCTra;

    @Column(name = "tc_hub_diem_don")
    private Integer tcHubDiemDon;

    @Column(name = "tc_hub_diem_tra")
    private Integer tcHubDiemTra;

    @Column(name = "tc_distance_don")
    private Integer tcDistanceDon;

    @Column(name = "tc_distance_tra")
    private Integer tcDistanceTra;

    @Column(name = "tc_is_picked")
    private Integer tcIsPicked;

    @Column(name = "tc_is_dropped")
    private Integer tcIsDropped;

    @Column(name = "tc_is_havaz_now", columnDefinition = "integer")
    private Boolean tcIsHavazNow = false;

    @Column(name = "tc_time_to_hub_don")
    private Integer tcTimeToHubDon;

    @Column(name = "tc_time_to_hub_tra")
    private Integer tcTimeToHubTra;

    @Column(name = "tc_distance_to_hub_don")
    private Double tcDistanceToHubDon;

    @Column(name = "tc_distance_to_hub_tra")
    private Double tcDistanceToHubTra;

    @Column(name = "tc_bvv_bex_id_a")
    private Integer tcBvvBexIdA;

    @Column(name = "tc_bvv_bex_id_b")
    private Integer tcBvvBexIdB;

    @Column(name = "last_location")
    private Integer lastLocation;

    @Version
    @Column(name = "version")
    private Integer version;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("tcVeId", tcVeId)
                .append("bvvId", bvvId)
                .append("didId", didId)
                .append("isTcDon", isTcDon)
                .append("isTcTra", isTcTra)
                .append("bvvTenKhachHangDi", bvvTenKhachHangDi)
                .append("bvvPhoneDi", bvvPhoneDi)
                .append("bvvDiemDonKhach", bvvDiemDonKhach)
                .append("bvvDiemTraKhach", bvvDiemTraKhach)
                .append("bvvMaGhe", bvvMaGhe)
                .append("bvvGhiChu", bvvGhiChu)
                .append("veAction", veAction)
                .append("tcLenhId", tcLenhId)
                .append("tcTrangThaiDon", tcTrangThaiDon)
                .append("lyDoTuChoiDon", lyDoTuChoiDon)
                .append("thuTuDon", thuTuDon)
                .append("thoiGianDon", thoiGianDon)
                .append("laiXeIdDon", laiXeIdDon)
                .append("ghiChuDon", ghiChuDon)
                .append("tcTrangThaiTra", tcTrangThaiTra)
                .append("lyDoTuChoiTra", lyDoTuChoiTra)
                .append("thuTuTra", thuTuTra)
                .append("thoiGianTra", thoiGianTra)
                .append("laiXeIdTra", laiXeIdTra)
                .append("ghiChuTra", ghiChuTra)
                .append("khachHangMoi", khachHangMoi)
                .append("tcLenhTraId", tcLenhTraId)
                .append("adminLv2UserDon", adminLv2UserDon)
                .append("adminLv2UserTra", adminLv2UserTra)
                .append("createdBy", createdBy)
                .append("createdDate", createdDate)
                .append("lastUpdatedBy", lastUpdatedBy)
                .append("lastUpdatedDate", lastUpdatedDate)
                .append("bvvLongStart", bvvLongStart)
                .append("bvvLatStart", bvvLatStart)
                .append("bvvLongEnd", bvvLongEnd)
                .append("bvvLatEnd", bvvLatEnd)
                .append("bvvSource", bvvSource)
                .append("vungTCDon", vungTCDon)
                .append("vungTCTra", vungTCTra)
                .append("tcHubDiemDon", tcHubDiemDon)
                .append("tcHubDiemTra", tcHubDiemTra)
                .append("tcDistanceDon", tcDistanceDon)
                .append("tcDistanceTra", tcDistanceTra)
                .append("tcIsPicked", tcIsPicked)
                .append("tcIsDropped", tcIsDropped)
                .append("tcIsHavazNow", tcIsHavazNow)
                .append("tcTimeToHubDon", tcTimeToHubDon)
                .append("tcTimeToHubTra", tcTimeToHubTra)
                .append("tcDistanceToHubDon", tcDistanceToHubDon)
                .append("tcDistanceToHubTra", tcDistanceToHubTra)
                .append("tcBvvBexIdA", tcBvvBexIdA)
                .append("tcBvvBexIdB", tcBvvBexIdB)
                .append("lastLocation", lastLocation)
                .toString();
    }
}
