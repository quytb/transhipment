package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "configuration")
public class ConfigurationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "con_id")
    private Integer conId;

    @Column(name = "con_ban_ve_seri", columnDefinition = "TINYINT")
    private Boolean conBanVeSeri;

    @Column(name = "con_version")
    private Integer conVersion;

    @Column(name = "con_time_nghiem_thu")
    private Integer conTimeNghiemThu;

    @Column(name = "con_in_lenh_top")
    private Integer conInLenhTop;

    @Column(name = "con_in_lenh_left")
    private Integer conInLenhLeft;

    @Column(name = "con_so_do_giuong")
    private Integer conSoDoGiuong;

    @Column(name = "con_thay_giao", columnDefinition = "TEXT")
    private String conThayGiao;

    @Column(name = "con_data", columnDefinition = "TEXT")
    private String conData;

    @Column(name = "con_api_crm_url")
    private String conApiCrmUrl;

    @Column(name = "con_api_crm_token", columnDefinition = "LONGTEXT")
    private String conApiCrmToken;

    @Column(name = "con_api_booking_url")
    private String conApiBookingUrl;

    @Column(name = "con_api_booking_token", columnDefinition = "LONGTEXT")
    private String conApiBookingToken;

    @Column(name = "con_dinh_bien_xe")
    private Integer conDinhBienXe;

    @Column(name = "con_dinh_bien_lai")
    private Integer conDinhBienLai;

    @Column(name = "con_api_erp_url")
    private String conApiErpUrl;

    @Column(name = "con_api_erp_token", columnDefinition = "LONGTEXT")
    private String conApiErpToken;

    @Column(name = "con_erp_url")
    private String conErpUrl;

    @Column(name = "con_hrm_url")
    private String conHrmUrl;

    @Column(name = "con_bus_url")
    private String conBusUrl;

    @Column(name = "con_driver_url")
    private String conDriverUrl;

    @Column(name = "con_web_url")
    private String conWebUrl;

    @Column(name = "con_sms_km", columnDefinition = "TEXT")
    private String conSmsKm;

    @Column(name = "con_sms_send")
    private Boolean conSmsSend;

    @Column(name = "con_json_data", columnDefinition = "LONGTEXT")
    private String conJsonData;

    @Column(name = "con_point_sell")
    private Integer conPointSell;

    @Column(name = "con_point_buy")
    private Integer conPointBuy;

    @Column(name = "con_point_up_level")
    private Integer conPointUpLevel;

    @Column(name = "con_point_apply_at")
    private Integer conPointApplyAt;

    @Column(name = "con_point_expired_at")
    private Integer conPointExpiredAt;

    @Column(name = "con_point_type")
    private Integer conPointType;

    @Column(name = "con_no_check_ip", columnDefinition = "TINYINT")
    private Boolean conNoCheckIp;

    @Column(name = "con_san_check_ping")
    private Integer conSanCheckPing;

    @Column(name = "con_lam_tron_tien_erp")
    private Integer conLamTronTienErp;

    @Column(name = "con_lam_tron_tien_san")
    private Integer conLamTronTienSan;

    @Column(name = "con_so_tien_lam_tron")
    private Integer conSoTienLamTron;
}
