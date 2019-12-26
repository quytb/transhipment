package com.havaz.transport.dao.entity;

import com.havaz.transport.core.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "admin_lv2_user")
public class AdminLv2UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "adm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer admId;

    @Column(name = "adm_ctv", columnDefinition = "TINYINT")
    private Integer admCtv;

    @Column(name = "adm_ma")
    private String admMa;

    @Column(name = "adm_ma_text")
    private String admMaText;

    @Column(name = "adm_ma_md5")
    private String admMaMd5;

    @Column(name = "adm_loginname")
    private String admLoginname;

    @Column(name = "adm_loginname_md5")
    private String admLoginnameMd5;

    @Column(name = "adm_phong_ban_id")
    private Integer admPhongBanId;

    @Column(name = "adm_phong_ban_2_id")
    private Integer admPhongBan2Id;

    @Column(name = "adm_group_id")
    private String admGroupId;

    @Column(name = "adm_nhom_dai_ly")
    private Integer admNhomDaiLy;

    @Column(name = "adm_loai_dai_ly", columnDefinition = "TINYINT")
    private Integer admLoaiDaiLy;

    @Column(name = "adm_password")
    private String admPassword;

    @Column(name = "remember_token")
    private String rememberToken;

    @Column(name = "adm_name")
    private String admName;

    @Column(name = "adm_name_ko_dau")
    private String admNameKoDau;

    @Column(name = "adm_avatar")
    private String admAvatar;

    @Column(name = "adm_gioi_tinh", columnDefinition = "TINYINT")
    private Integer admGioiTinh;

    @Column(name = "adm_ben_ab", columnDefinition = "BIT")
    private Integer admBenAb;

    @Column(name = "adm_dan_toc")
    private Integer admDanToc;

    @Column(name = "adm_ton_giao")
    private Integer admTonGiao;

    @Column(name = "adm_cap")
    private String admCap;

    @Column(name = "adm_ho_khau_thuong_tru")
    private String admHoKhauThuongTru;

    @Column(name = "adm_hash")
    private String admHash;

    @Column(name = "adm_email")
    private String admEmail;

    @Column(name = "adm_email_2")
    private String admEmail2;

    @Column(name = "adm_phone_2")
    private String admPhone2;

    @Column(name = "adm_ngay_sinh")
    private Integer admNgaySinh;

    @Column(name = "adm_address")
    private String admAddress;

    @Column(name = "adm_noi_lam_viec")
    private Integer admNoiLamViec;

    @Column(name = "adm_phone")
    private String admPhone;

    @Column(name = "adm_date")
    private Integer admDate;

    @Column(name = "adm_isadmin")
    private Boolean admIsadmin;

    @Column(name = "adm_active")
    private Boolean admActive;

    @Column(name = "adm_minus", columnDefinition = "TINYINT")
    private Integer admMinus;

    @Column(name = "adm_delete", columnDefinition = "TINYINT")
    private Boolean admDelete;

    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "adm_chuc_danh")
    private String admChucDanh;

    @Column(name = "adm_chuc_danh_id")
    private Integer admChucDanhId;

    @Column(name = "adm_chuc_danh_2")
    private Integer admChucDanh2;

    @Column(name = "adm_loai_hop_dong", columnDefinition = "TINYINT")
    private Integer admLoaiHopDong;

    @Column(name = "adm_han_hop_dong")
    private Integer admHanHopDong;

    @Column(name = "adm_ngay_vao_cong_ty")
    private Integer admNgayVaoCongTy;

    @Column(name = "adm_status")
    private Integer admStatus;

    @Column(name = "adm_token")
    private String admToken;

    @Column(name = "adm_ngay_sinh_ngay", columnDefinition = "TINYINT")
    private Integer admNgaySinhNgay;

    @Column(name = "adm_ngay_sinh_thang", columnDefinition = "TINYINT")
    private Integer admNgaySinhThang;

    @Column(name = "adm_noi_sinh")
    private String admNoiSinh;

    @Column(name = "adm_date_join_bhxh")
    private Integer admDateJoinBhxh;

    @Column(name = "adm_date_bao_giam")
    private Integer admDateBaoGiam;

    @Column(name = "adm_phb_id_old")
    private Integer admPhbIdOld;

    @Column(name = "adm_date_bao_tang")
    private Integer admDateBaoTang;

    @Column(name = "adm_type")
    private Integer admType;

    @Column(name = "adm_dai_ly_on_off", columnDefinition = "TINYINT")
    private Integer admDaiLyOnOff;

    @Column(name = "adm_last_login")
    private Integer admLastLogin;

    @Column(name = "adm_create_by")
    private Integer admCreateBy;

    @Column(name = "adm_create_time")
    private Integer admCreateTime;

    @Column(name = "adm_no_check_ip", columnDefinition = "TINYINT")
    private Integer admNoCheckIp;

    @Column(name = "adm_app_token", columnDefinition = "TEXT")
    private String admAppToken;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tc_users_roles",
               joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "adm_id",
                                         insertable = false, updatable = false),
               inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id",
                                                insertable = false, updatable = false))
    private Set<TcRoleEntity> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tc_users_permissions",
               joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "adm_id",
                                         insertable = false, updatable = false),
               inverseJoinColumns = @JoinColumn(name = "permission_id",
                                                referencedColumnName = "id", insertable = false,
                                                updatable = false))
    private Set<TcPermissionEntity> permissions = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<AdminLv2RoleEntity> admRoles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "admin_lv2_user_group_id",
               joinColumns = @JoinColumn(name = "admg_admin_id", referencedColumnName = "adm_id",
                                         insertable = false, updatable = false),
               inverseJoinColumns = @JoinColumn(name = "admg_group_id", insertable = false,
                                                updatable = false))
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<AdminLv2GroupEntity> groups = new HashSet<>();

    public boolean authenticate(String password) {
        return CommonUtils.hashMD5(password + this.admHash).equals(this.admPassword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("admId", admId)
                .append("admMa", admMa)
                .append("admLoginname", admLoginname)
                .append("admMaText", admMaText)
                .append("admName", admName)
                .append("admNameKoDau", admNameKoDau)
                .append("admGioiTinh", admGioiTinh)
                .append("admDanToc", admDanToc)
                .append("admTonGiao", admTonGiao)
                .append("admDelete", admDelete)
                .append("adminId", adminId)
                .toString();
    }
}
