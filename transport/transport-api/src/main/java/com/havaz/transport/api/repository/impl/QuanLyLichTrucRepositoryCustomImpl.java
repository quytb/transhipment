package com.havaz.transport.api.repository.impl;

import com.havaz.transport.api.common.CommonUtils;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.form.CustomerRankData;
import com.havaz.transport.api.model.CaPagingDTO;
import com.havaz.transport.api.model.DieuDoKhachTcDonDTO;
import com.havaz.transport.api.model.DieuDoKhachTcTraDTO;
import com.havaz.transport.api.model.LichTrucDTO;
import com.havaz.transport.api.model.TaiXeTcDTO;
import com.havaz.transport.api.model.TimKhachDTO;
import com.havaz.transport.api.repository.QuanLyLichTrucRepositoryCustom;
import com.havaz.transport.api.service.CommonService;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.Strings;
import com.havaz.transport.dao.entity.TcCaEntity;
import com.havaz.transport.dao.repository.AbstractRepository;
import com.havaz.transport.dao.repository.NotTuyenRepository;
import com.havaz.transport.dao.repository.TuyenRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Repository
public class QuanLyLichTrucRepositoryCustomImpl extends AbstractRepository implements QuanLyLichTrucRepositoryCustom {

    private static final Logger LOG = LoggerFactory.getLogger(QuanLyLichTrucRepositoryCustom.class);

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private TuyenRepository tuyenRepository;

    @Autowired
    private NotTuyenRepository notTuyenRepository;

    @Autowired
    CommonService commonService;

    @Override
    public List<TaiXeTcDTO> getTaiXeTrungChuyen() {
        String sql = "SELECT adm_id AS taiXeId, adm_name AS taiXeTen " +
                " FROM  admin_lv2_user " +
                "   INNER JOIN admin_lv2_user_group_id " +
                "     ON admg_admin_id = adm_id " +
                " WHERE adm_active = 1 AND admg_group_id = 2 " +
                " ORDER BY adm_name";
        Session session = getCurrentSession();
        NativeQuery<TaiXeTcDTO> query = session.createNativeQuery(sql);
        return query.setResultTransformer(Transformers.aliasToBean(TaiXeTcDTO.class)).list();
    }

    @Override
    public List<TaiXeTcDTO> getTaiXeTrungChuyenByChiNhanh(List<Integer> chiNhanhId) {
        String sql = "SELECT adm_id AS taiXeId, adm_name AS taiXeTen " +
                " FROM  admin_lv2_user " +
                "   INNER JOIN admin_lv2_user_group_id " +
                "   ON admg_admin_id = adm_id " +
                " WHERE adm_active = 1 AND admg_group_id = 2 " +
                "   AND adm_noi_lam_viec IN :chiNhanhId " +
                " ORDER BY adm_name";
        Session session = getCurrentSession();
        NativeQuery query = session.createNativeQuery(sql);
        query.setParameter("chiNhanhId", chiNhanhId);
        return query.setResultTransformer(Transformers.aliasToBean(TaiXeTcDTO.class)).list();
    }

    @Override
    public List<TaiXeTcDTO> getTaiXeTrungChuyenByVung(int vungId) {
        String sql = "SELECT " +
                "     ctv.tc_ctv_id AS taiXeId," +
                "     AD.adm_name   AS taiXeTen " +
                " FROM tc_vtc_ctv ctv " +
                "   LEFT JOIN admin_lv2_user AD ON AD.adm_id = ctv.tc_ctv_id " +
                "   WHERE ctv.tc_vtt_id = :vungId";
        Session session = getCurrentSession();
        NativeQuery<TaiXeTcDTO> query = session.createNativeQuery(sql);
        query.setParameter("vungId", vungId);
        return query.setResultTransformer(Transformers.aliasToBean(TaiXeTcDTO.class)).list();
    }

    @Override
    public Page<TcCaEntity> findCaTruc(CaPagingDTO caPagingDTO) {

        StringBuilder jql = new StringBuilder("SELECT c FROM TcCaEntity c");
        boolean isTenCa = StringUtils.isEmpty(caPagingDTO.getTenCa());
        boolean isMaCa = StringUtils.isEmpty(caPagingDTO.getMaCa());
        boolean isGioBatDau = (caPagingDTO.getGioBatDau() == null);
        boolean isGioKetThuc = (caPagingDTO.getGioKetThuc() == null);
        boolean isTrangThai = (caPagingDTO.getTrangThai() == -1);
        Pageable pageable = CommonUtils.convertPagingFormToPageable(caPagingDTO);

        if (!isGioBatDau || !isGioKetThuc || !isMaCa || !isTenCa || !isTrangThai) {
            jql.append(" WHERE ");
        }
        if (!isTenCa) {
            jql.append("c.tenCa like :tenCa");
            if (!isMaCa) {
                jql.append(" AND c.maCa like :maCa");
            }
            if (!isGioBatDau) {
                jql.append(" AND c.gioBatDau =:gioBatDau");
            }
            if (!isGioKetThuc) {
                jql.append(" AND c.gioKetThuc =:gioKetThuc");
            }
            if (!isTrangThai) {
                jql.append(" AND c.trangThai =:trangThai");
            }
        } else if (!isMaCa) {
            jql.append("c.maCa like :maCa");

            if (!isGioBatDau) {
                jql.append(" AND c.gioBatDau =:gioBatDau");
            }
            if (!isGioKetThuc) {
                jql.append(" AND c.gioKetThuc =:gioKetThuc");
            }
            if (!isTrangThai) {
                jql.append(" AND c.trangThai =:trangThai");
            }
        } else if (!isGioBatDau) {
            jql.append(" c.gioBatDau =:gioBatDau");

            if (!isGioKetThuc) {
                jql.append(" AND c.gioKetThuc =:gioKetThuc");
            }
            if (!isTrangThai) {
                jql.append(" AND c.trangThai =:trangThai");
            }
        } else if (!isGioKetThuc) {
            jql.append("c.gioKetThuc =:gioKetThuc");
            if (!isTrangThai) {
                jql.append(" AND c.trangThai =:trangThai");
            }
        } else if (!isTrangThai) {
            jql.append("c.trangThai =:trangThai");
        }

        if (!StringUtils.isEmpty(caPagingDTO.getSortBy()) && !StringUtils.isEmpty(caPagingDTO.getSortType())) {
            jql.append(" ORDER BY ").append(caPagingDTO.getSortBy()).append(Strings.SPACE)
                    .append(caPagingDTO.getSortType());
        }

        Query query = getEntityManager().createQuery(String.valueOf(jql));

        if (!isTenCa) {
            query.setParameter("tenCa", "%" + caPagingDTO.getTenCa() + "%");

        }
        if (!isMaCa) {
            query.setParameter("maCa", "%" + caPagingDTO.getMaCa() + "%");
        }
        if (!isGioBatDau) {
            query.setParameter("gioBatDau", caPagingDTO.getGioBatDau());
        }
        if (!isGioKetThuc) {
            query.setParameter("gioKetThuc", caPagingDTO.getGioKetThuc());
        }
        if (!isTrangThai) {
            query.setParameter("trangThai", caPagingDTO.getTrangThai() == 1);
        }
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        //Lấy tổng số bản ghi tìm thấy
        List<TcCaEntity> listCa = query.getResultList();

        // Lấy số bản ghi theo phân trang
        List<TcCaEntity> tcCaEntities = query.setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList();

        return new PageImpl<>(tcCaEntities, pageable, listCa.size());
    }

    @Override
    public List<LichTrucDTO> getLichTrucByNgayTruc(int taiXeId, LocalDate startDate, LocalDate endDate, String sortBy, String sortType) {
        String sql = "SELECT " +
                "       e.tc_lich_id        AS tcLichId, " +
                "       e.tai_xe_id         AS taiXeId, " +
                "       a.adm_name          AS taiXeTen, " +
                "       x.xe_bien_kiem_soat AS bks, " +
                "       e.ngay_truc         AS ngayTruc, " +
                "       c.ma_ca             AS maCa, " +
                "       e.tc_ca_id          AS tcCaId, " +
                "       e.xe_id             AS xeId, " +
                "       e.ghi_chu           AS ghiChu " +
                " FROM tc_lich_truc e " +
                "         LEFT JOIN admin_lv2_user a ON e.tai_xe_id = a.adm_id " +
                "         LEFT JOIN xe x ON e.xe_id = x.xe_id " +
                "         LEFT JOIN tc_ca c ON e.tc_ca_id = c.tc_ca_id " +
                " WHERE e.tai_xe_id = :taiXeId " +
                "   AND e.ngay_truc BETWEEN :startDate AND :endDate ";

        StringBuilder sb = new StringBuilder();

        if (!StringUtils.isEmpty(sortBy) && !StringUtils.isEmpty(sortType)) {
            sb.append(" ORDER BY ").append(sortBy).append(Strings.SPACE).append(sortType);
        }

        sql += sb.toString();

        Session session = getCurrentSession();
        NativeQuery<LichTrucDTO> query = session.createNativeQuery(sql);
        query.setParameter("taiXeId", taiXeId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.setResultTransformer(Transformers.aliasToBean(LichTrucDTO.class)).list();
    }

    @Override
    public Page<DieuDoKhachTcDonDTO> getDanhSachKhachTrungChuyenDon(TimKhachDTO timKhachDTO) {

        StringBuilder jql = new StringBuilder("SELECT t.tuyTen, " +
                "d.didGioDieuHanh, " +
                "e.bvvTenKhachHangDi, " +
                "e.bvvPhoneDi, " +
                "e.bvvDiemDonKhach, " +
                "count(e.bvvDiemDonKhach)," +
                "(SELECT a.admName FROM AdminLv2UserEntity a WHERE a.admId = e.laiXeIdDon), " +
                "e.thuTuDon AS thuTuDon, " +
                "e.tcTrangThaiDon, " +
                "e.lyDoTuChoiDon, " +
                "GROUP_CONCAT(e.bvvId ), " +
                "n.notMa, " +
                "e.bvvGhiChu, " +
                "vtt.tcVttName, " +
                "e.tcHubDiemDon, " +
                "bex.ten, " +
                "e.didId, " +
                "e.tcTimeToHubDon, " +
                "e.version " +
                "FROM TcVeEntity e " +
                "LEFT JOIN DieuDoTempEntity d ON e.didId = d.didId " +
                "LEFT JOIN NotTuyenEntity n ON n.notId=d.didNotId " +
                "LEFT JOIN TuyenEntity t ON t.tuyId = n.notTuyId " +
                "LEFT JOIN TcVungTrungChuyenEntity vtt ON e.vungTCDon = vtt.tcVttId " +
                "LEFT JOIN BenXeEntity bex ON e.tcHubDiemDon = bex.id " +
                "WHERE n.notId IN (:notIds) AND e.isTcDon = 1 AND e.tcTrangThaiDon IN (:trangThaiDon) ");
        if (timKhachDTO.getDiemKDs() != null && timKhachDTO.getDiemKDs().size() > 0) {
            jql.append(" AND e.tcBvvBexIdA IN (:diemKDs) ");
        } else if (timKhachDTO.getHubId() != null && timKhachDTO.getHubId() > 0) {
            jql.append(" AND e.tcHubDiemDon =:hubId ");
        }
        List<Integer> notIds = timKhachDTO.getNotIds();
        List<Integer> tuyIds = timKhachDTO.getTuyIds();
        List<Integer> trangThaiDon = timKhachDTO.getTrangThaiTC();
        int khoangThoiGian = CommonUtils.getSeconds(timKhachDTO.getKhoangThoiGian());
        int loaiGio = timKhachDTO.getLoaiGio();
        LocalDate ngayXuatBen = LocalDate.now();
        List<Object[]> totalElements = new ArrayList<>();
        List<Object[]> objects = new ArrayList<>();

        Pageable pageable = CommonUtils.convertPagingFormToPageable(timKhachDTO);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = timKhachDTO.getSortBy();
        String sortType = timKhachDTO.getSortType();

        if (timKhachDTO.getNgayXuatBen() != null) {
            ngayXuatBen = timKhachDTO.getNgayXuatBen();
        }

        // Tim kiem theo vung TC
        if (timKhachDTO.getVungTC() != null && timKhachDTO.getVungTC() > 0) {
            jql.append("AND e.vungTCDon = " + timKhachDTO.getVungTC() + " ");
        }

        // Tim kiem theo hub diem don khach
        if (timKhachDTO.getHubId() != null && timKhachDTO.getHubId() > 0) {
            jql.append("AND e.tcHubDiemDon = " + timKhachDTO.getHubId() + " ");
        }

        //Search By BvvId
        if (!CollectionUtils.isEmpty(timKhachDTO.getBvvIds())) {
            jql.append(" AND e.bvvId in (:bvvIds) ");
        }

        if (notIds == null || notIds.isEmpty()) {
            if (tuyIds == null || tuyIds.isEmpty()) {
                tuyIds = tuyenRepository.getTuyId();
            }
            if (loaiGio == 0) {
                notIds = notTuyenRepository.getNotId(tuyIds);
            } else if (loaiGio == 1) {
                notIds = notTuyenRepository.getNotIdByGioA(tuyIds);
            } else {
                notIds = notTuyenRepository.getNotIdByGioB(tuyIds);
            }

            if (khoangThoiGian == Constant.ZERO) {
                jql.append("AND DATE_FORMAT(FROM_UNIXTIME(d.didTimeXuatBen),'%Y-%m-%d') =:ngayXuatBen ");
                jql.append("GROUP BY e.bvvDiemDonKhach, e.bvvPhoneDi, d.didId, e.tcTrangThaiDon ORDER BY  e.tcTrangThaiDon, " + sortBy + " " + sortType);
                Query query = getEntityManager().createQuery(String.valueOf(jql));
                query.setParameter("ngayXuatBen", ngayXuatBen.toString());
                query.setParameter("notIds", notIds);
                query.setParameter("trangThaiDon", trangThaiDon);
                if (!CollectionUtils.isEmpty(timKhachDTO.getBvvIds())) {
                    query.setParameter("bvvIds", timKhachDTO.getBvvIds());
                }
                if (timKhachDTO.getDiemKDs() != null && timKhachDTO.getDiemKDs().size() > 0) {
                    query.setParameter("diemKDs", timKhachDTO.getDiemKDs());
                } else if (timKhachDTO.getHubId() != null && timKhachDTO.getHubId() > 0) {
                    query.setParameter("hubId", timKhachDTO.getHubId());
                }
                totalElements = query.getResultList();
                objects = query.setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList();

            } else {
                jql.append("AND DATE_FORMAT(FROM_UNIXTIME(d.didTimeXuatBen),'%Y-%m-%d') =:ngayXuatBen ");
                jql.append("AND d.didGioDieuHanh BETWEEN CURRENT_TIME AND ADDTIME(CURRENT_TIME ,SEC_TO_TIME(:khoangThoiGian))");
//                jql.append("AND STR_TO_DATE(d.didGioXuatBenThat,'%H:%i') BETWEEN CURRENT_TIME AND ADDTIME(CURRENT_TIME ,SEC_TO_TIME(:khoangThoiGian))");
                jql.append("GROUP BY e.bvvDiemDonKhach, e.bvvPhoneDi, d.didId, e.tcTrangThaiDon ORDER BY e.tcTrangThaiDon," + sortBy + " " + sortType);
                Query query = getEntityManager().createQuery(String.valueOf(jql));
                query.setParameter("notIds", notIds);
                query.setParameter("ngayXuatBen", ngayXuatBen.toString());
                query.setParameter("khoangThoiGian", khoangThoiGian);
                query.setParameter("trangThaiDon", trangThaiDon);
                if (!CollectionUtils.isEmpty(timKhachDTO.getBvvIds())) {
                    query.setParameter("bvvIds", timKhachDTO.getBvvIds());
                }
                if (timKhachDTO.getDiemKDs() != null && timKhachDTO.getDiemKDs().size() > 0) {
                    query.setParameter("diemKDs", timKhachDTO.getDiemKDs());
                } else if (timKhachDTO.getHubId() != null && timKhachDTO.getHubId() > 0) {
                    query.setParameter("hubId", timKhachDTO.getHubId());
                }
                totalElements = query.getResultList();
                objects = query.setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList();
            }

        } else {
            jql.append("AND DATE_FORMAT(FROM_UNIXTIME(d.didTimeXuatBen),'%Y-%m-%d') =:ngayXuatBen ");
            jql.append("GROUP BY e.bvvDiemDonKhach, e.bvvPhoneDi, d.didId, e.tcTrangThaiDon ORDER BY e.tcTrangThaiDon, " + sortBy + " " + sortType);

            Query query = getEntityManager().createQuery(String.valueOf(jql));
            query.setParameter("notIds", notIds);
            query.setParameter("ngayXuatBen", ngayXuatBen.toString());
            query.setParameter("trangThaiDon", trangThaiDon);
            if (!CollectionUtils.isEmpty(timKhachDTO.getBvvIds())) {
                query.setParameter("bvvIds", timKhachDTO.getBvvIds());
            }
            if (timKhachDTO.getDiemKDs() != null && timKhachDTO.getDiemKDs().size() > 0) {
                query.setParameter("diemKDs", timKhachDTO.getDiemKDs());
            } else if (timKhachDTO.getHubId() != null && timKhachDTO.getHubId() > 0) {
                query.setParameter("hubId", timKhachDTO.getHubId());
            }
            totalElements = query.getResultList();
            objects = query.setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList();
        }

        List<DieuDoKhachTcDonDTO> dieuDoKhachTCDonDTOS = new ArrayList<>();
        List<String> listPhone = new ArrayList<>();
        if (objects != null && objects.size() > 0) {
            for (Object[] obj : objects) {
                DieuDoKhachTcDonDTO dieuDoKhachTCDonDTO = new DieuDoKhachTcDonDTO();
                String tenTuyen = obj[0].toString();
                if (obj[11] != null) {
                    String maNot = String.valueOf(obj[11]);
                    if (maNot.charAt(maNot.length() - 1) == 'B') {
                        tenTuyen = Strings.reverseStringByWords(tenTuyen);
                    }
                }
                dieuDoKhachTCDonDTO.setTenTuyen(tenTuyen);

                dieuDoKhachTCDonDTO.setGioXuatBen(obj[1] != null ? String.valueOf(obj[1]) : "");
                dieuDoKhachTCDonDTO.setTenKhach(obj[2] != null ? String.valueOf(obj[2]) : "");
                dieuDoKhachTCDonDTO.setSdtKhach(obj[3] != null ? String.valueOf(obj[3]) : "");
                listPhone.add(obj[3] != null ? String.valueOf(obj[3]) : "");
                dieuDoKhachTCDonDTO.setDiemDonKhach(obj[4] != null ? String.valueOf(obj[4]) : "");
                dieuDoKhachTCDonDTO.setSoKhach(obj[5] != null ? Integer.parseInt(String.valueOf(obj[5])) : 0);
                dieuDoKhachTCDonDTO.setTaiXeDon(obj[6] != null ? String.valueOf(obj[6]) : "");
                dieuDoKhachTCDonDTO.setThuTuDon(obj[7] != null ? Integer.parseInt(String.valueOf(obj[7])) : 0);
                dieuDoKhachTCDonDTO.setTrangThaiDon(obj[8] != null ? Integer.parseInt(String.valueOf(obj[8])) : 0);
                dieuDoKhachTCDonDTO.setLyDo(obj[9] != null ? String.valueOf(obj[9]) : "");
                dieuDoKhachTCDonDTO.setVungTCDon(obj[13] != null ? String.valueOf(obj[13]) : "");
                if (obj[10] != null) {
                    List<Integer> bvvIds = CommonUtils.convertListStringToInt(Arrays.asList(String.valueOf(obj[10]).split("\\s*,\\s*")));
                    dieuDoKhachTCDonDTO.setBvvIds(bvvIds);
                }
                dieuDoKhachTCDonDTO.setBvvGhiChu(obj[12] != null ? String.valueOf(obj[12]) : "");
                dieuDoKhachTCDonDTO.setHubDiemDonId(obj[14] != null ? Integer.valueOf(String.valueOf(obj[14])) : 0);
                dieuDoKhachTCDonDTO.setHubDiemDon(obj[15] != null ? String.valueOf(obj[15]) : "");
                dieuDoKhachTCDonDTO.setDidId(obj[16] != null ? Integer.valueOf(String.valueOf(obj[16])) : 0);
                dieuDoKhachTCDonDTO.setTimeToHubDon(obj[17] != null ? Integer.valueOf(String.valueOf(obj[17])) : 0);
                if (obj[18] != null) {
                    List<Integer> versions = CommonUtils.convertListStringToInt(Arrays.asList(String.valueOf(obj[18]).split("\\s*,\\s*")));
                    dieuDoKhachTCDonDTO.setVersion(versions);
                }
                dieuDoKhachTCDonDTOS.add(dieuDoKhachTCDonDTO);
            }
        }
        //goi api ben san de lay rank cua khach hang 1
        dieuDoKhachTCDonDTOS = getRankCustomerHavazTCD(dieuDoKhachTCDonDTOS,listPhone);
        return new PageImpl<>(dieuDoKhachTCDonDTOS, pageable, totalElements.size());
    }

    @Override
    public Page<DieuDoKhachTcTraDTO> getDanhSachKhachTrungChuyenTra(TimKhachDTO timKhachDTO) {
        StringBuilder jql = new StringBuilder("SELECT t.tuyTen, " +
                "d.didGioDieuHanh, " +
                "e.bvvTenKhachHangDi, " +
                "e.bvvPhoneDi, " +
                "e.bvvDiemTraKhach, " +
                "count(e.bvvDiemTraKhach)," +
                "(SELECT a.admName FROM AdminLv2UserEntity a WHERE a.admId = e.laiXeIdTra), " +
                "e.thuTuTra AS thuTuTra, " +
                "e.tcTrangThaiTra, " +
                "e.lyDoTuChoiTra, " +
                "GROUP_CONCAT(e.bvvId ), " +
                "n.notMa, " +
                "e.bvvGhiChu, " +
                "vtt.tcVttName, " +
                "e.tcHubDiemTra, " +
                "bex.ten, " +
                "e.didId, "+
                "e.tcTimeToHubTra, "+
                " ( CASE WHEN rt.totalRunTime IS NOT NULL AND rt.totalRunTime > 0 " +
                "   THEN  rt.totalRunTime  " +
                "   ELSE t.tuyThoiGianChay END ), " +
                " d.didGioDieuHanh, "+
                " ( CASE WHEN rt.totalRunTime IS NOT NULL AND rt.totalRunTime > 0 " +
                "   THEN ( (time_to_sec(d.didGioDieuHanh) + rt.totalRunTime*60)%86400 ) " +
                "   ELSE ( (time_to_sec(d.didGioDieuHanh) + t.tuyThoiGianChay*60)%86400 ) END ) AS gioveben "  +
                "FROM TcVeEntity e " +
                "LEFT JOIN DieuDoTempEntity d ON e.didId = d.didId " +
                "LEFT JOIN NotTuyenEntity n ON n.notId=d.didNotId " +
                "LEFT JOIN TuyenEntity t ON t.tuyId = n.notTuyId " +
                "LEFT JOIN TcVungTrungChuyenEntity vtt ON e.vungTCTra = vtt.tcVttId "+
                "LEFT JOIN BenXeEntity bex ON e.tcHubDiemTra = bex.id "+
                "LEFT JOIN RoutingOptionEntity ro ON ro.rooId = n.notRoutingOptionId "+
                "LEFT JOIN BvLoaiDichVuEntity bldv on bldv.bvlId = d.didLoaiXe " +
                "LEFT JOIN TotalRunTimeViewEntity rt on rt.tuyenId = t.tuyId AND rt.dichVuId = bldv.bvlId " +
                " AND rt.roId = ro.rooId " +
                " AND e.isTcTra = 1 AND e.tcTrangThaiTra IN (:trangThaiTra) "+
                "WHERE n.notId IN (:notIds) AND e.isTcTra = 1 AND e.tcTrangThaiTra IN (:trangThaiTra) ");

        if (timKhachDTO.getDiemKDs() != null && timKhachDTO.getDiemKDs().size() > 0) {
            jql.append(" AND e.tcBvvBexIdB IN (:diemKDs) ");
        } else if (timKhachDTO.getHubId() != null && timKhachDTO.getHubId() > 0) {
            jql.append(" AND e.tcHubDiemTra =:hubId ");
        }
        List<Integer> notIds = timKhachDTO.getNotIds();
        List<Integer> tuyIds = timKhachDTO.getTuyIds();
        List<Integer> trangThaiTra = timKhachDTO.getTrangThaiTC();
        int khoangThoiGian = CommonUtils.getSeconds(timKhachDTO.getKhoangThoiGian());
        int loaiGio = timKhachDTO.getLoaiGio();
        LocalDate ngayXuatBen = LocalDate.now();
        List<Object[]> totalElements = new ArrayList<>();
        List<Object[]> objects = new ArrayList<>();

        Pageable pageable = CommonUtils.convertPagingFormToPageable(timKhachDTO);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        String sortBy = timKhachDTO.getSortBy();

        if (timKhachDTO.getNgayXuatBen() != null) {
            ngayXuatBen = timKhachDTO.getNgayXuatBen();
        }
        String sortType = timKhachDTO.getSortType();

        //Tim kiem theo vung TC
        if (timKhachDTO.getVungTC() > 0)
            jql.append("AND e.vungTCTra = " + timKhachDTO.getVungTC() + " ");

        //search by bvvId
        if (!CollectionUtils.isEmpty(timKhachDTO.getBvvIds())) {
            jql.append(" AND e.bvvId in (:bvvIds) ");
        }
        //Tim kiem theo hub diem tra khach
        if (timKhachDTO.getHubId() > 0)
            jql.append("AND e.tcHubDiemTra = " + timKhachDTO.getHubId() + " ");

        jql.append("AND DATE_FORMAT(FROM_UNIXTIME( CASE WHEN rt.totalRunTime IS NOT NULL AND rt.totalRunTime > 0 " +
                " THEN ( d.didTimeXuatBen + rt.totalRunTime*60)  " +
                " ELSE ( d.didTimeXuatBen + t.tuyThoiGianChay*60) END  ),'%Y-%m-%d') =:ngayXuatBen ");
        if (notIds == null || notIds.size() == 0) {
            if (tuyIds == null || tuyIds.size() == 0) {
                tuyIds = tuyenRepository.getTuyId();
            }
            if (loaiGio == 0) {
                notIds = notTuyenRepository.getNotId(tuyIds);
            } else if (loaiGio == 1) {
                notIds = notTuyenRepository.getNotIdByGioA(tuyIds);
            } else {
                notIds = notTuyenRepository.getNotIdByGioB(tuyIds);
            }
            if (khoangThoiGian == Constant.ZERO) {
                jql.append("GROUP BY e.bvvDiemTraKhach, e.bvvPhoneDi, d.didId, e.tcTrangThaiTra ORDER BY e.tcTrangThaiTra," + sortBy + " " + sortType);

                Query query = getEntityManager().createQuery(String.valueOf(jql));
                query.setParameter("ngayXuatBen", ngayXuatBen.toString());
                query.setParameter("notIds", notIds);
                if (timKhachDTO.getDiemKDs() != null && timKhachDTO.getDiemKDs().size() > 0) {
                    query.setParameter("diemKDs", timKhachDTO.getDiemKDs());
                } else if (timKhachDTO.getHubId() != null && timKhachDTO.getHubId() > 0) {
                    query.setParameter("hubId", timKhachDTO.getHubId());
                }
                query.setParameter("trangThaiTra", trangThaiTra);
                if (!CollectionUtils.isEmpty(timKhachDTO.getBvvIds())) {
                    query.setParameter("bvvIds", timKhachDTO.getBvvIds());
                }
                totalElements = query.getResultList();
                objects = query.setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList();

            } else {
                jql.append(" AND ADDTIME(d.didGioDieuHanh, " +
                        " CASE WHEN rt.totalRunTime IS NOT NULL AND rt.totalRunTime > 0" +
                        " THEN SEC_TO_TIME(rt.totalRunTime*60) " +
                        " ELSE SEC_TO_TIME(t.tuyThoiGianChay*60) END ) " +
                        " BETWEEN CURRENT_TIME AND ADDTIME(CURRENT_TIME ,SEC_TO_TIME(:khoangThoiGian))");
                jql.append("GROUP BY e.bvvDiemTraKhach, e.bvvPhoneDi, d.didId, e.tcTrangThaiTra ORDER BY e.tcTrangThaiTra, " + sortBy + " " + sortType);
                Query query = getEntityManager().createQuery(String.valueOf(jql));
                query.setParameter("ngayXuatBen", ngayXuatBen.toString());
                query.setParameter("khoangThoiGian", khoangThoiGian);
                query.setParameter("notIds", notIds);
                if (timKhachDTO.getDiemKDs() != null && timKhachDTO.getDiemKDs().size() > 0) {
                    query.setParameter("diemKDs", timKhachDTO.getDiemKDs());
                } else if (timKhachDTO.getHubId() != null && timKhachDTO.getHubId() > 0) {
                    query.setParameter("hubId", timKhachDTO.getHubId());
                }
                query.setParameter("trangThaiTra", trangThaiTra);
                if (!CollectionUtils.isEmpty(timKhachDTO.getBvvIds())) {
                    query.setParameter("bvvIds", timKhachDTO.getBvvIds());
                }
                totalElements = query.getResultList();
                objects = query.setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList();
            }

        } else {
            jql.append("GROUP BY e.bvvDiemTraKhach, e.bvvPhoneDi, d.didId, e.tcTrangThaiTra ORDER BY e.tcTrangThaiTra, " + sortBy + " " + sortType);
            Query query = getEntityManager().createQuery(String.valueOf(jql));
            query.setParameter("notIds", notIds);
            if (timKhachDTO.getDiemKDs() != null && timKhachDTO.getDiemKDs().size() > 0) {
                query.setParameter("diemKDs", timKhachDTO.getDiemKDs());
            } else if (timKhachDTO.getHubId() != null && timKhachDTO.getHubId() > 0) {
                query.setParameter("hubId", timKhachDTO.getHubId());
            }
            query.setParameter("ngayXuatBen", ngayXuatBen.toString());
            query.setParameter("trangThaiTra", trangThaiTra);
            if (!CollectionUtils.isEmpty(timKhachDTO.getBvvIds())) {
                query.setParameter("bvvIds", timKhachDTO.getBvvIds());
            }
            totalElements = query.getResultList();
            objects = query.setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList();
        }

        List<DieuDoKhachTcTraDTO> dieuDoKhachTcTraDTOS = new ArrayList<>();
        List<String> listPhone = new ArrayList<>();
        if (objects != null && objects.size() > 0) {
            for (Object[] obj : objects) {
                DieuDoKhachTcTraDTO dieuDoKhachTcTraDTO = new DieuDoKhachTcTraDTO();
                String tenTuyen = obj[0].toString();
                if (obj[11] != null) {
                    String maNot = String.valueOf(obj[11]);
                    if (maNot.charAt(maNot.length() - 1) == 'B') {
                        tenTuyen = Strings.reverseStringByWords(tenTuyen);
                    }
                }
                dieuDoKhachTcTraDTO.setTenTuyen(tenTuyen);

//                dieuDoKhachTcTraDTO.setGioXuatBen(obj[1] != null ? String.valueOf(obj[1]) : "");
                dieuDoKhachTcTraDTO.setTenKhach(obj[2] != null ? String.valueOf(obj[2]) : "");
                dieuDoKhachTcTraDTO.setSdtKhach(obj[3] != null ? String.valueOf(obj[3]) : "");
                listPhone.add(obj[3] != null ? String.valueOf(obj[3]) : "");
                dieuDoKhachTcTraDTO.setDiemTraKhach(obj[4] != null ? String.valueOf(obj[4]) : "");
                dieuDoKhachTcTraDTO.setSoKhach(obj[5] != null ? Integer.parseInt(String.valueOf(obj[5])) : 0);
                dieuDoKhachTcTraDTO.setTaiXeTra(obj[6] != null ? String.valueOf(obj[6]) : "");
                dieuDoKhachTcTraDTO.setThuTuTra(obj[7] != null ? Integer.parseInt(String.valueOf(obj[7])) : 0);
                dieuDoKhachTcTraDTO.setTrangThaiTra(obj[8] != null ? Integer.parseInt(String.valueOf(obj[8])) : 0);
                dieuDoKhachTcTraDTO.setLyDo(obj[9] != null ? String.valueOf(obj[9]) : "");
                dieuDoKhachTcTraDTO.setVungTCTra(obj[13] != null ? String.valueOf(obj[13]) : "");
                if (obj[10] != null) {
                    List<Integer> bvvIds = CommonUtils.convertListStringToInt(Arrays.asList(String.valueOf(obj[10]).split("\\s*,\\s*")));
                    dieuDoKhachTcTraDTO.setBvvIds(bvvIds);
                }
                dieuDoKhachTcTraDTO.setBvvGhiChu(obj[12] != null ? String.valueOf(obj[12]) : "");
                dieuDoKhachTcTraDTO.setHubDiemTraId(obj[14] != null ? Integer.valueOf(String.valueOf(obj[14])) : 0);
                dieuDoKhachTcTraDTO.setHubDiemTra(obj[15] != null ? String.valueOf(obj[15]) : "");
                dieuDoKhachTcTraDTO.setDidId(obj[16] != null ? Integer.valueOf(String.valueOf(obj[16])) : 0);
                dieuDoKhachTcTraDTO.setTimeToHubTra(obj[17] != null ? Integer.valueOf(String.valueOf(obj[17])) : 0);
                dieuDoKhachTcTraDTO.setGioDieuHanh(obj[19] != null ? String.valueOf(obj[19]) : StringUtils.EMPTY);

                String gioVeBen = addMinuteToStringTime(String.valueOf(obj[1]), Integer.parseInt(String.valueOf(obj[18])));
                dieuDoKhachTcTraDTO.setGioXuatBen(gioVeBen);

                dieuDoKhachTcTraDTOS.add(dieuDoKhachTcTraDTO);
            }
        }
        dieuDoKhachTcTraDTOS = getRankCustomerHavazTCT(dieuDoKhachTcTraDTOS,listPhone);
        return new PageImpl<>(dieuDoKhachTcTraDTOS, pageable, totalElements.size());
    }

    private String addMinuteToStringTime(String myTime, int amountMunite) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            java.util.Date d = df.parse(myTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, amountMunite);
            return df.format(cal.getTime());
        } catch (ParseException e) {
            throw new TransportException(e.getMessage());
        }

    }

    private List<DieuDoKhachTcDonDTO> getRankCustomerHavazTCD(List<DieuDoKhachTcDonDTO> lstObject, List<String> lstString){
        if(lstString.size() <= 0)
            return lstObject;
        try{
            List<CustomerRankData> customerRankDatas = commonService.layThongTinRankTuHavaz(lstString);
            lstObject = lstObject.stream().map(obj1 -> {
                customerRankDatas.stream().map(obj2 -> {
                    if(obj2.getPhone().equals(obj1.getSdtKhach())) {
                        obj1.setRank(obj2.getRank());
                        obj1.setIcon(obj2.getIcon());
                    }
                        return obj1;
                }).collect(Collectors.toList());
                return obj1;
            }).collect(Collectors.toList());
            return lstObject;
        }catch (Exception ex){
            ex.printStackTrace();
            LOG.warn("Khong lay duoc danh sach rank tu ben san");
            return lstObject;
        }
    }

    private List<DieuDoKhachTcTraDTO> getRankCustomerHavazTCT(List<DieuDoKhachTcTraDTO> lstObject, List<String> lstString){
        if(lstString.size() <= 0)
            return lstObject;
        try{
            List<CustomerRankData> customerRankDatas = commonService.layThongTinRankTuHavaz(lstString);
            lstObject = lstObject.stream().map(obj1 -> {
                customerRankDatas.stream().map(obj2 -> {
                    if(obj2.getPhone().equals(obj1.getSdtKhach())) {
                        obj1.setRank(obj2.getRank());
                        obj1.setIcon(obj2.getIcon());
                    }
                    return obj1;
                }).collect(Collectors.toList());
                return obj1;
            }).collect(Collectors.toList());
            return lstObject;
        }catch (Exception ex){
            ex.printStackTrace();
            LOG.warn("Khong lay duoc danh sach rank tu ben san");
            return lstObject;
        }
    }
}
