package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.common.CommonUtils;
import com.havaz.transport.api.common.Constant;
import com.havaz.transport.api.form.ChamCongForm;
import com.havaz.transport.api.form.LichTrucCNForm;
import com.havaz.transport.api.form.LichTrucForm;
import com.havaz.transport.api.form.PageCustom;
import com.havaz.transport.api.form.PagingForm;
import com.havaz.transport.api.model.CaPagingDTO;
import com.havaz.transport.api.model.CaTcDTO;
import com.havaz.transport.api.model.ChamCongDTO;
import com.havaz.transport.api.model.DanhSachCaDTO;
import com.havaz.transport.api.model.DanhSachLichDTO;
import com.havaz.transport.api.model.LichTrucDTO;
import com.havaz.transport.api.model.LuuChamCongDTO;
import com.havaz.transport.api.model.TaiXeTcDTO;
import com.havaz.transport.api.model.TaoCaDTO;
import com.havaz.transport.api.model.TaoLichDTO;
import com.havaz.transport.api.model.UpdateChamCongDTO;
import com.havaz.transport.api.model.UpdateLichDTO;
import com.havaz.transport.api.model.XeTcDTO;
import com.havaz.transport.api.repository.QuanLyLichTrucRepositoryCustom;
import com.havaz.transport.api.repository.TcLenhRepositoryCustom;
import com.havaz.transport.api.service.QuanLyLichTrucService;
import com.havaz.transport.api.utils.SecurityUtils;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.core.utils.DateTimeUtils;
import com.havaz.transport.core.utils.Strings;
import com.havaz.transport.dao.entity.TcCaEntity;
import com.havaz.transport.dao.entity.TcChamCongEntity;
import com.havaz.transport.dao.entity.TcLenhEntity;
import com.havaz.transport.dao.entity.TcLichTrucEntity;
import com.havaz.transport.dao.entity.XeEntity;
import com.havaz.transport.dao.repository.TcCaRepository;
import com.havaz.transport.dao.repository.TcChamCongRepository;
import com.havaz.transport.dao.repository.TcLenhRepository;
import com.havaz.transport.dao.repository.TcLichTrucRepository;
import com.havaz.transport.dao.repository.XeRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class QuanLyLichTrucServiceImpl implements QuanLyLichTrucService {

    private static final Logger log = LoggerFactory.getLogger(QuanLyLichTrucServiceImpl.class);

    @Autowired
    private TcCaRepository tcCaRepository;

    @Autowired
    private TcLichTrucRepository tcLichTrucRepository;

    @Autowired
    private XeRepository xeRepository;

    @Autowired
    private QuanLyLichTrucRepositoryCustom quanLyLichTrucRepositoryCustom;

    @Autowired
    private TcChamCongRepository tcChamCongRepository;

    @Autowired
    private TcLenhRepository tcLenhRepository;

    @Autowired
    private TcLenhRepositoryCustom tcLenhRepositoryCustom;

    //API QUẢN LÝ CA TRỰC

    //API1 - Tạo ca
    @Override
    @Transactional
    public void taoMoiCaTruc(TaoCaDTO taoCaDTO) {
        try {
            log.info("Tạo mới ca trực tên ca: {}, mã ca {}", taoCaDTO.getTenCa(), taoCaDTO.getMaCa());
            List<TcCaEntity> tcCaEntities = tcCaRepository.checkCaUniqueByCode(taoCaDTO.getMaCa());
            if (!tcCaEntities.isEmpty()) {
                throw new TransportException("Mã ca đã tồn tại hoặc giờ bắt đầu và giờ kết thúc đã tồn tại.");
            }
//            Bỏ check vì thực tế có ca liên ngày
//            if (taoCaDTO.getGioBatDau() > taoCaDTO.getGioKetThuc()) {
//                throw new TransportException("Giờ bắt đầu phải nhỏ hơn giờ kết thúc.");
//            }
            TcCaEntity tcCaEntity = new TcCaEntity();
            tcCaEntity.setTenCa(taoCaDTO.getTenCa());
            tcCaEntity.setMaCa(taoCaDTO.getMaCa());
            tcCaEntity.setGioBatDau(taoCaDTO.getGioBatDau());
            tcCaEntity.setGioKetThuc(taoCaDTO.getGioKetThuc());
            //Set hours_in_ca
            double startTime = taoCaDTO.getGioBatDau() == null ? 0 : taoCaDTO.getGioBatDau();
            double endTime = taoCaDTO.getGioKetThuc()== null ? 0 : taoCaDTO.getGioKetThuc();
            if (startTime > endTime) {
                tcCaEntity.setHousInCa(endTime + 24 - startTime);
            } else {
                tcCaEntity.setHousInCa(endTime - startTime);
            }
            tcCaEntity.setTrangThai(taoCaDTO.getTrangThai());
            tcCaEntity.setGhiChu(taoCaDTO.getGhiChu());

            tcCaRepository.save(tcCaEntity);
        } catch (Exception e) {
            log.info("Lỗi tạo mới ca trực: {}", e.getMessage());
            throw new TransportException("Lỗi tạo mới ca trực", e);
        }
    }

    @Override
    public Boolean checkCaUnique(String maCa, Float gioBatDau, Float gioKetThuc) {
        List<TcCaEntity> tcCaEntities = tcCaRepository.checkCaUnique(maCa, gioBatDau, gioKetThuc);
        return tcCaEntities.isEmpty();
    }

    // API2 -Danh sách ca
    @Override
    public PageCustom<DanhSachCaDTO> getDanhSachCaTruc(PagingForm pagingForm) {
        String sortBy = "tcCaId";
        String sortType = Constant.DESC;
        if (!StringUtils.isEmpty(pagingForm.getSortBy())) {
            try {
                sortBy = pagingForm.getSortBy();
            } catch (Exception e) {
                log.error("Field not existing !");
            }
        }
        if (!StringUtils.isEmpty(pagingForm.getSortType())) {
            if (Constant.ASC.equalsIgnoreCase(pagingForm.getSortType()) ||
                    Constant.DESC.equalsIgnoreCase(pagingForm.getSortType())) {
                sortType = pagingForm.getSortType();
            }
        }
        pagingForm.setSortBy(sortBy);
        pagingForm.setSortType(sortType);

        Pageable pageable = CommonUtils.convertPagingFormToPageable(pagingForm);
        Page<TcCaEntity> tcCaEntities = tcCaRepository.findAll(pageable);

        Page<DanhSachCaDTO> dtoPage = tcCaEntities.map(e -> {
            DanhSachCaDTO danhSachCaDTO = new DanhSachCaDTO();
            BeanUtils.copyProperties(e, danhSachCaDTO);
            return danhSachCaDTO;
        });
        return CommonUtils.convertPageImplToPageCustom(dtoPage, pagingForm.getSortBy(), pagingForm.getSortType());
    }

    // API2' -Danh sách ca không phân trang
    @Override
    public List<DanhSachCaDTO> getAllCaTruc() {
        List<TcCaEntity> tcCaEntities = tcCaRepository.findAllByOrderByTcCaIdDesc();
        List<DanhSachCaDTO> danhSachCaDTOS = new ArrayList<>();

        for (TcCaEntity tcCaEntity : tcCaEntities) {
            DanhSachCaDTO danhSachCaDTO = new DanhSachCaDTO();
            BeanUtils.copyProperties(tcCaEntity, danhSachCaDTO);
            danhSachCaDTOS.add(danhSachCaDTO);
        }
        return danhSachCaDTOS;
    }

    // API3-Danh sách ca active
    @Override
    public List<CaTcDTO> getActiveCa() {
        List<CaTcDTO> caTcDTOS = new ArrayList<>();
        List<TcCaEntity> tcCaEntities = tcCaRepository.findByTrangThai(true);
        if (tcCaEntities != null) {
            for (TcCaEntity tcCaEntity : tcCaEntities) {
                CaTcDTO caTcDTO = new CaTcDTO();
                BeanUtils.copyProperties(tcCaEntity, caTcDTO);
                caTcDTOS.add(caTcDTO);
            }
        }
        return caTcDTOS;
    }

    // API4- Thông tin chi tiết ca
    @Override
    public CaTcDTO getCaTruc(int tcCaId) {
        log.debug("Start lấy thông tin chi tiết ca trực caId: {}", tcCaId);
        CaTcDTO caTcDTO = new CaTcDTO();
        Optional<TcCaEntity> tcCaEntityOptional = tcCaRepository.findById(tcCaId);
        if (tcCaEntityOptional.isPresent()) {
            TcCaEntity tcCaEntity = tcCaEntityOptional.get();
            BeanUtils.copyProperties(tcCaEntity, caTcDTO);
            log.debug("Kết thúc lấy thông tin ca trực caId: {}", tcCaId);
        }
        return caTcDTO;
    }

    //API5-Cập nhật ca
    @Override
    @Transactional
    public void updateCaTruc(CaTcDTO caTcDTO) {
        log.debug("Start cập nhật ca trực caId: {}", caTcDTO.getTcCaId());
        Optional<TcCaEntity> tcCaEntityOptional = tcCaRepository.findById(caTcDTO.getTcCaId());
        if (tcCaEntityOptional.isPresent()) {
            TcCaEntity tcCaEntity = tcCaEntityOptional.get();
            List<TcCaEntity> tcCaEntities = tcCaRepository.checkCaUnique(caTcDTO.getMaCa(), caTcDTO.getGioBatDau(), caTcDTO.getGioKetThuc());
            if (tcCaEntities != null && tcCaEntities.size() > Constant.CONFIGURATION_NO_1) {
                throw new TransportException("Mã ca đã tồn tại hoặc giờ bắt đầu và giờ kết thúc đã tồn tại.");
            }
            BeanUtils.copyProperties(caTcDTO, tcCaEntity);
            //Set hours_in_ca
            double startTime = caTcDTO.getGioBatDau() == null ? 0 : caTcDTO.getGioBatDau();
            double endTime = caTcDTO.getGioKetThuc()== null ? 0 : caTcDTO.getGioKetThuc();
            if (startTime > endTime) {
                tcCaEntity.setHousInCa(endTime + 24 - startTime);
            } else {
                tcCaEntity.setHousInCa(endTime - startTime);
            }
            //Save to DB
            tcCaRepository.save(tcCaEntity);
            log.info("Kết thúc cập nhật ca trực caId: {}", caTcDTO.getTcCaId());
        } else {
            throw new TransportException("Ca không tồn tại.");
        }
    }

    //API6-Đổi trạng thái ca
    @Override
    @Transactional
    public CaTcDTO changeStatusCaTruc(int tcCaId, boolean active) {
        CaTcDTO caTcDTO = new CaTcDTO();
        Optional<TcCaEntity> tcCaEntityOptional = tcCaRepository.findById(tcCaId);
        if (tcCaEntityOptional.isPresent()) {
            TcCaEntity tcCaEntity = tcCaEntityOptional.get();
            tcCaEntity.setTrangThai(active);
            tcCaRepository.save(tcCaEntity);
            BeanUtils.copyProperties(tcCaEntity, caTcDTO);
        }
        return caTcDTO;
    }

    // API7-Tìm kiếm ca
    @Override
    public PageCustom<DanhSachCaDTO> findCaTruc(CaPagingDTO caPagingDTO) {
        String sortBy = "tcCaId";
        String sortType = Constant.DESC;
        if (!StringUtils.isEmpty(caPagingDTO.getSortBy())) {
            try {
                sortBy = caPagingDTO.getSortBy();
            } catch (Exception e) {
                log.error("Field not existing !");
            }
        }
        if (!StringUtils.isEmpty(caPagingDTO.getSortType())) {
            if (Constant.ASC.equalsIgnoreCase(caPagingDTO.getSortType()) ||
                    Constant.DESC.equalsIgnoreCase(caPagingDTO.getSortType())) {
                sortType = caPagingDTO.getSortType();
            }
        }
        caPagingDTO.setSortBy(sortBy);
        caPagingDTO.setSortType(sortType);
//        setOrder(caPagingDTO,sortBy,sortType);

        Page<TcCaEntity> tcCaEntities = quanLyLichTrucRepositoryCustom.findCaTruc(caPagingDTO);
        Page<DanhSachCaDTO> dtoPage = tcCaEntities.map(e -> {
            DanhSachCaDTO dto = new DanhSachCaDTO();
            BeanUtils.copyProperties(e, dto);
            return dto;
        });
        return CommonUtils.convertPageImplToPageCustom(dtoPage, caPagingDTO.getSortBy(), caPagingDTO.getSortType());
    }

    // API QUẢN LÝ LỊCH TRỰC
    // API8-Danh sách tài xế xe trung chuyển
    @Override
    public List<TaiXeTcDTO> getTaiXeTrungChuyen() {
        return quanLyLichTrucRepositoryCustom.getTaiXeTrungChuyen();
    }

    @Override
    public List<TaiXeTcDTO> getTaiXeTrungChuyen(List<Integer> chiNhanhId) {
        return quanLyLichTrucRepositoryCustom.getTaiXeTrungChuyenByChiNhanh(chiNhanhId);
    }

    @Override
    public List<TaiXeTcDTO> getTaiXeTrungChuyenTheoVung(int vungId) {
        return quanLyLichTrucRepositoryCustom.getTaiXeTrungChuyenByVung(vungId);
    }

    //API9- Danh sách bks xe trung chuyển
    @Override
    public List<XeTcDTO> getDanhSachXeTrungChuyen(int xeTrungTam, boolean xeActive) {
        List<XeTcDTO> xeTcDTOS = new ArrayList<>();
        List<XeEntity> xeEntities = xeRepository.getDanhSachXeTrungChuyen(xeTrungTam, xeActive);
        for (XeEntity xeEntity : xeEntities) {
            XeTcDTO xeTcDTO = new XeTcDTO();
            xeTcDTO.setXeId(xeEntity.getXeId());
            xeTcDTO.setSdtXe(xeEntity.getXeSoDienThoai());
            xeTcDTO.setBks(xeEntity.getXeBienKiemSoat());
            xeTcDTO.setSeats(xeEntity.getXeSoCho());
            xeTcDTOS.add(xeTcDTO);
        }
        return xeTcDTOS;
    }

    //API10 - Tạo lịch trực
    @Override
    @Transactional
    public void taoMoiLichTruc(List<TaoLichDTO> taoLichDTOS) {
        // Validate thông tin phân lịch cho tài xế
        for (int i = 0; i < taoLichDTOS.size() - 1; i++) {
            if (taoLichDTOS.get(i).getNgayTruc() == taoLichDTOS.get(i + 1).getNgayTruc()) {
                throw new TransportException("Ngày trực " + taoLichDTOS.get(i).getNgayTruc() + " bị trùng nhau");
            }
        }

        for (TaoLichDTO taoLichDTO : taoLichDTOS) {
            if (taoLichDTO.getNgayTruc().isBefore(LocalDate.now())) {
                throw new TransportException("Không thể phân lịch cho ngày trực " + taoLichDTO.getNgayTruc() + " do nhỏ hơn ngày hiện tại");
            }
        }

        for (TaoLichDTO taoLichDTO : taoLichDTOS) {
            String maCa = Strings.EMPTY;
            String bks = Strings.EMPTY;
            Optional<TcCaEntity> tcCaEntityOpt = tcCaRepository.findById(taoLichDTO.getTcCaId());
            Optional<XeEntity> xeEntityOpt =xeRepository.findById(taoLichDTO.getXeId());
            if(tcCaEntityOpt.isPresent()){
                TcCaEntity tcCaEntity =tcCaEntityOpt.get();
                maCa = tcCaEntity.getMaCa();
            }
            if (xeEntityOpt.isPresent()) {
                XeEntity xeEntity = xeEntityOpt.get();
                bks = xeEntity.getXeBienKiemSoat();
            }
            log.debug("Tạo mới lịch trực cho tài xế: {}", taoLichDTO.getTaiXeId());
            LocalDateTime currentTime = LocalDateTime.now();
            List<TcLichTrucEntity> tcLichTrucEntities =
                    tcLichTrucRepository.findByTcCaIdAndXeIdAndNgayTruc(taoLichDTO.getTcCaId(),
                                                                        taoLichDTO.getXeId(),
                                                                        taoLichDTO.getNgayTruc());

            List<TcLichTrucEntity> lichTrucEntities = tcLichTrucRepository.findByTaiXeIdAndNgayTruc(taoLichDTO.getTaiXeId(), taoLichDTO.getNgayTruc());
            if (lichTrucEntities != null && lichTrucEntities.size() > Constant.ZERO) {
                for (TcLichTrucEntity lichTrucEntity : lichTrucEntities) {
                    //Kiểm tra điều kiện một ca chi gắn với một xe trong cùng ngày
                    if (tcLichTrucEntities.size() > 1) {
                        throw new TransportException("Đã gắn xe: " + bks + " cho ca trực: " + maCa + " của ngày: " + taoLichDTO.getNgayTruc());
                    } else {
                        lichTrucEntity.setLastUpdatedDate(currentTime);
                        createLichTrucEntity(taoLichDTO, lichTrucEntity);
                    }
                }
            } else {
                if (!tcLichTrucEntities.isEmpty()) {
                    throw new TransportException("Đã gắn xe: " + bks + " cho ca trực: " + maCa + " của ngày: " + taoLichDTO.getNgayTruc());
                } else {
                    TcLichTrucEntity tcLichTrucEntity = new TcLichTrucEntity();
                    tcLichTrucEntity.setCreatedDate(currentTime);
                    tcLichTrucEntity.setLastUpdatedDate(currentTime);
                    createLichTrucEntity(taoLichDTO, tcLichTrucEntity);
                }
            }
        }
    }

    private void createLichTrucEntity(TaoLichDTO taolich, TcLichTrucEntity lichtruc) {
        lichtruc.setTaiXeId(taolich.getTaiXeId());
        lichtruc.setTcCaId(taolich.getTcCaId());
        lichtruc.setXeId(taolich.getXeId());
        lichtruc.setNgayTruc(taolich.getNgayTruc());
        lichtruc.setGhiChu(taolich.getGhiChu() != null ? taolich.getGhiChu() : StringUtils.EMPTY);
        //Save DB
        tcLichTrucRepository.save(lichtruc);
        //TODO cập nhật lệnh tương ứng
        int taiXeId = lichtruc.getTaiXeId();
        int xeId = lichtruc.getXeId();
        LocalDate lDate = lichtruc.getLastUpdatedDate().toLocalDate();
        List<Object> lenhIds = tcLenhRepositoryCustom.findLenhIdsByLaiXeIdAndLastUpdatedDate(taiXeId, lDate);
        lenhIds.forEach(lenhId -> {
            if (lenhId instanceof Integer) {
                Optional<TcLenhEntity> lenh = tcLenhRepository.findById(Integer.parseInt(lenhId.toString()));
                if (lenh.isPresent()) {
                    TcLenhEntity lenhEntity = lenh.get();
                    lenhEntity.setXeId(xeId);
                    log.info("Đổi xe do đổi lịch: {}", xeId);
                    tcLenhRepository.save(lenhEntity);
                }
            }
        });
    }

    //API11-Sửa lịch trực
    @Override
    @Transactional
    public void updateLichTruc(List<UpdateLichDTO> updateLichDTOS) {
        // Validate thông tin phân lịch cho tài xế
        for (int i = 0; i < updateLichDTOS.size() - 1; i++) {
            if (updateLichDTOS.get(i).getNgayTruc() == (updateLichDTOS.get(i + 1).getNgayTruc())) {
                throw new TransportException("Ngày trực " + updateLichDTOS.get(i).getNgayTruc() + " bị trùng nhau");
            }
        }
        for (UpdateLichDTO updateLichDTO : updateLichDTOS) {
            String maCa = Strings.EMPTY;
            String bks = Strings.EMPTY;
            Optional<TcCaEntity> tcCaEntityOptional = tcCaRepository.findById(updateLichDTO.getTcCaId());
            Optional<XeEntity> xeEntityOptional =xeRepository.findById(updateLichDTO.getXeId());
            if(tcCaEntityOptional.isPresent()){
                TcCaEntity tcCaEntity =tcCaEntityOptional.get();
                maCa = tcCaEntity.getMaCa();
            }
            if(xeEntityOptional.isPresent()){
                XeEntity xeEntity = xeEntityOptional.get();
                bks = xeEntity.getXeBienKiemSoat();
            }

            List<TcLichTrucEntity> tcLichTrucEntities = tcLichTrucRepository.findByTcCaIdAndXeIdAndNgayTruc(updateLichDTO.getTcCaId(), updateLichDTO.getXeId(), updateLichDTO.getNgayTruc());
            log.debug("Bắt đầu chỉnh sửa lịch trực cho tài xế: {}", updateLichDTO.getTaiXeId());
            Optional<TcLichTrucEntity> lichTrucEntityOptional = tcLichTrucRepository.findById(updateLichDTO.getTcLichId());

            if (lichTrucEntityOptional.isPresent()) {
                TcLichTrucEntity tcLichTrucEntity = lichTrucEntityOptional.get();
                if (!tcLichTrucEntities.isEmpty()) {
                    throw new TransportException("Đã gắn xe: " + bks + " cho ca trực: " + maCa + " của ngày: " + updateLichDTO.getNgayTruc());
                } else {
                    BeanUtils.copyProperties(updateLichDTO, tcLichTrucEntity);
                    //Save DB
                    tcLichTrucRepository.save(tcLichTrucEntity);
                    log.debug("Đã sửa lịch trực cho tài xế: {}", updateLichDTO.getTaiXeId());
                    //TODO cập nhật lệnh tương ứng
                    int taiXeId = tcLichTrucEntity.getTaiXeId();
                    int xeId = tcLichTrucEntity.getXeId();
                    LocalDate lDate = tcLichTrucEntity.getLastUpdatedDate().toLocalDate();
                    List<Object> lenhIds = tcLenhRepositoryCustom.findLenhIdsByLaiXeIdAndLastUpdatedDate(taiXeId, lDate);
                    lenhIds.forEach(lenhId -> {
                        if (lenhId instanceof Integer) {
                            Optional<TcLenhEntity> lenh = tcLenhRepository.findById(Integer.parseInt(lenhId.toString()));
                            if (lenh.isPresent()) {
                                TcLenhEntity lenhEntity = lenh.get();
                                lenhEntity.setXeId(xeId);
                                tcLenhRepository.save(lenhEntity);
                            }
                        }
                    });
                }
            } else {
                log.info("Không tồn tại lịch trực: " + updateLichDTO.getTcLichId() + " cho tài xế: " + updateLichDTO.getTaiXeId());
            }
        }
    }

    // API 12- Danh sách lịch trực
    @Override
    public PageCustom<DanhSachLichDTO> getLichTrucByNgayTruc(LichTrucForm lichTrucForm) {
        List<DanhSachLichDTO> danhSachLichDTOS = new ArrayList<>();
        LocalDate startDate = lichTrucForm.getStartDate();
        LocalDate endDate = lichTrucForm.getEndDate();
        int pageNumber = lichTrucForm.getPage();
        int pageSize = lichTrucForm.getSize();

        //Validate điều kiện order
        String sortBy = "tcLichId";
        String sortType = Constant.ASC;
        if (!StringUtils.isEmpty(lichTrucForm.getSortBy())) {
            sortBy = lichTrucForm.getSortBy();
        }
        if (Constant.ASC.equalsIgnoreCase(lichTrucForm.getSortType())
                || Constant.DESC.equalsIgnoreCase(lichTrucForm.getSortType())) {
            sortType = lichTrucForm.getSortType();
        }

        lichTrucForm.setSortBy(sortBy);
        lichTrucForm.setSortType(sortType);

        //Set thông tin phân trang
        Pageable pageableTaixeId = PageRequest.of((pageNumber > Constant.ZERO ? pageNumber : Constant.CONFIGURATION_NO_1) - Constant.CONFIGURATION_NO_1,
                pageSize > Constant.ZERO ? pageSize : Constant.CONFIGURATION_NO_10);

        Page<Integer> taixeIds = tcLichTrucRepository.getListTaiXeIdByNgayTruc(startDate, endDate, pageableTaixeId);
        for (Integer taixeId : taixeIds) {
            DanhSachLichDTO danhSachLichDTO = new DanhSachLichDTO();
            danhSachLichDTO.setTaiXeId(taixeId);
            List<LichTrucDTO> lichTrucDTOS = quanLyLichTrucRepositoryCustom.getLichTrucByNgayTruc(taixeId, startDate, endDate, sortBy, sortType);
            if (!lichTrucDTOS.isEmpty()) {
                // Set lịch trực cho một lái xe từ ngày startDate đến ngày endDate
                danhSachLichDTO.setLichTrucDTOS(lichTrucDTOS);
                danhSachLichDTOS.add(danhSachLichDTO);
            }
        }
        Page<DanhSachLichDTO> page = new PageImpl<>(danhSachLichDTOS, pageableTaixeId,
                                                    taixeIds.getTotalElements());
        return CommonUtils.convertPageImplToPageCustom(page, sortBy, sortType);
    }

    // API 12B- Danh sách lịch trực theo chi nhánh
    @Override
    public PageCustom<DanhSachLichDTO> getLichTrucByNgayTrucAndChiNhanh(LichTrucCNForm lichTrucCNForm) {
        List<DanhSachLichDTO> danhSachLichDTOS = new ArrayList<>();
        LocalDate startDate = lichTrucCNForm.getStartDate();
        LocalDate endDate = lichTrucCNForm.getEndDate();
        int pageNumber = lichTrucCNForm.getPage();
        int pageSize = lichTrucCNForm.getSize();

        //Validate điều kiện order
        String sortBy = "tcLichId";
        String sortType = Constant.ASC;
        if (!StringUtils.isEmpty(lichTrucCNForm.getSortBy())) {
            sortBy = lichTrucCNForm.getSortBy();
        }
        if (Constant.ASC.equalsIgnoreCase(lichTrucCNForm.getSortType())
                || Constant.DESC.equalsIgnoreCase(lichTrucCNForm.getSortType())) {
            sortType = lichTrucCNForm.getSortType();
        }
        lichTrucCNForm.setSortBy(sortBy);
        lichTrucCNForm.setSortType(sortType);

        //Set thông tin phân trang
        Pageable pageableTaixeId = PageRequest.of((pageNumber > Constant.ZERO ? pageNumber : Constant.CONFIGURATION_NO_1) - Constant.CONFIGURATION_NO_1,
                pageSize > Constant.ZERO ? pageSize : Constant.CONFIGURATION_NO_10);
        Page<Integer> taixeIds;
        if (CollectionUtils.isEmpty(lichTrucCNForm.getChiNhanh())) {
             taixeIds = tcLichTrucRepository.getListTaiXeIdByNgayTruc(startDate, endDate, pageableTaixeId);
        } else {
            taixeIds = tcLichTrucRepository.getListTaiXeIdByNgayTrucAndChiNhanh(startDate, endDate,
                                                                                lichTrucCNForm.getChiNhanh(),
                                                                                pageableTaixeId);
        }
        for (Integer taixeId : taixeIds) {
            log.debug("Lấy danh sách phân lịch tài xế xe trung chuyển có taixeId={}", taixeId);
            DanhSachLichDTO danhSachLichDTO = new DanhSachLichDTO();
            danhSachLichDTO.setTaiXeId(taixeId);
            List<LichTrucDTO> lichTrucDTOS = quanLyLichTrucRepositoryCustom.getLichTrucByNgayTruc(taixeId, startDate, endDate, sortBy, sortType);
            if (!lichTrucDTOS.isEmpty()) {
                // Set lịch trực cho một lái xe từ ngày startDate đến ngày endDate
                danhSachLichDTO.setLichTrucDTOS(lichTrucDTOS);
                danhSachLichDTOS.add(danhSachLichDTO);
            }
        }
        Page<DanhSachLichDTO> page = new PageImpl<>(danhSachLichDTOS, pageableTaixeId,
                                                    taixeIds.getTotalElements());
        return CommonUtils.convertPageImplToPageCustom(page, sortBy, sortType);
    }

    //API13-Lấy dữ liệu chấm công
    @Override
    @Transactional
    public List<ChamCongDTO> getDuLieuChamCong(ChamCongForm chamCongForm) {
        List<Object[]> objects ;
        List<TcLichTrucEntity> tcLichTrucEntities;
        //Kiểm tra có chọn filter theo chi nhánh hay không (trường có tài xế không thuộc bất kỳ chi nhánh nào)
        if (CollectionUtils.isEmpty(chamCongForm.getChiNhanh())) {
            objects = tcChamCongRepository.getDuLieuChamCong(chamCongForm.getNgayChamCong());
        } else {
            objects = tcChamCongRepository.getDuLieuChamCongChiNhanh(chamCongForm.getNgayChamCong(),
                                                                     chamCongForm.getChiNhanh());
        }
        List<ChamCongDTO> chamCongDTOS = new ArrayList<>();

        //Kiểm tra đã có dữ liệu chấm công chưa, nếu chưa có sẽ lấy về từ lịch trực
        if (CollectionUtils.isEmpty(objects)) {
            if (CollectionUtils.isEmpty(chamCongForm.getChiNhanh())) {
                tcLichTrucEntities = tcLichTrucRepository.findByNgayTruc(chamCongForm.getNgayChamCong());
            } else {
                tcLichTrucEntities = tcLichTrucRepository.findByNgayTrucAndChiNhanh(chamCongForm.getNgayChamCong(),
                                                                                    chamCongForm.getChiNhanh());
            }
            if (!tcLichTrucEntities.isEmpty()) {
                List<TcChamCongEntity> tcChamCongEntities = new ArrayList<>();
                for (TcLichTrucEntity tcLichTrucEntity : tcLichTrucEntities) {
                    TcChamCongEntity tcChamCongEntity = new TcChamCongEntity();
                    BeanUtils.copyProperties(tcLichTrucEntity, tcChamCongEntity);
                    tcChamCongEntity.setNgayChamCong(chamCongForm.getNgayChamCong());
                    tcChamCongEntity.setCreatedBy(String.valueOf(SecurityUtils.getCurrentUserLogin()));
                    tcChamCongEntities.add(tcChamCongEntity);
                }

                //Save dữ liệu chấm công to DB
                tcChamCongRepository.saveAll(tcChamCongEntities);
                List<Object[]> duLieuChamCong;
                if (CollectionUtils.isEmpty(chamCongForm.getChiNhanh())) {
                    duLieuChamCong = tcChamCongRepository.getDuLieuChamCong(chamCongForm.getNgayChamCong());
                } else {
                    duLieuChamCong = tcChamCongRepository.getDuLieuChamCongChiNhanh(chamCongForm.getNgayChamCong(),
                                                                                    chamCongForm.getChiNhanh());
                }
                chamCongDTOS = convertObjectToChamCongDTO(duLieuChamCong);
            } else {
                throw new TransportException("Không có dữ liệu chấm công ngày:" + chamCongForm.getNgayChamCong());
            }

        } else {
            chamCongDTOS = convertObjectToChamCongDTO(objects);
        }
        return chamCongDTOS;
    }

    private List<ChamCongDTO> convertObjectToChamCongDTO(List<Object[]> objects) {
        List<ChamCongDTO> chamCongDTOS = new ArrayList<>();
        for (Object[] obj : objects) {
            ChamCongDTO chamCongDTO = new ChamCongDTO();
            chamCongDTO.setTaiXeId(obj[0] != null ? Integer.parseInt(obj[0].toString()) : 0);
            chamCongDTO.setTaiXeTen(obj[1] != null ? String.valueOf(obj[1]) : Strings.EMPTY);
            chamCongDTO.setXeId(obj[2] != null ? Integer.parseInt(obj[2].toString()) : 0);
            chamCongDTO.setBks(obj[3] != null ? String.valueOf(obj[3]) : Strings.EMPTY);
            chamCongDTO.setCaId(obj[4] != null ? Integer.parseInt(obj[4].toString()) : 0);
            chamCongDTO.setMaCa(obj[5] != null ? String.valueOf(obj[5]) : Strings.EMPTY);
            chamCongDTO.setNgayChamCong(obj[6] != null ? DateTimeUtils
                    .convertStringToLocalDate(obj[6].toString(), DateTimeUtils.DATE_YYYY_MM_DD) : LocalDate.now());
            chamCongDTO.setGioThucTe(obj[7] != null ? Double.parseDouble(obj[7].toString()) : 0);
            chamCongDTO.setKhachPhatSinh(obj[8] != null ? Integer.parseInt(obj[8].toString()) : 0);
            chamCongDTO.setGhiChu(obj[9] != null ? String.valueOf(obj[9]) : Strings.EMPTY);
            chamCongDTO.setTcCongId(obj[10] != null ? Integer.parseInt(obj[10].toString()) : 0);

            // Add lịch trực của một lái xe vào danh sách phân lịch
            chamCongDTOS.add(chamCongDTO);
        }
        return chamCongDTOS;
    }

    //API14- Lưu dữ liệu chấm công
    @Override
    @Transactional
    public void saveDuLieuChamCong(LuuChamCongDTO luuChamCongDTO) {
        if (luuChamCongDTO.getNgayChamCong().isAfter(LocalDate.now())) {
            throw new TransportException("Không thể chấm công cho ngày tương lai");
        }
        List<UpdateChamCongDTO> updateChamCongDTOS = luuChamCongDTO.getUpdateChamCongDTOS();
        for (UpdateChamCongDTO updateChamCongDTO : updateChamCongDTOS) {
            if (updateChamCongDTO.getTcCongId() != Constant.ZERO) {
                Optional<TcChamCongEntity> tcChamCong = tcChamCongRepository.findById(updateChamCongDTO.getTcCongId());
                if (tcChamCong.isPresent()) {
                    TcChamCongEntity tcChamCongEntity = tcChamCong.get();
                    convertUpdateChamCongDTOtoTcChamCongEntity(updateChamCongDTO, tcChamCongEntity);
                } else {
                    log.info("Không tìm thấy dữ liệu chấm công có tcCongId: {}", updateChamCongDTO.getTcCongId());
                }
            } else {
                TcChamCongEntity tcChamCongEntity = new TcChamCongEntity();
                tcChamCongEntity.setNgayChamCong(luuChamCongDTO.getNgayChamCong());
                tcChamCongEntity.setCreatedBy(String.valueOf(SecurityUtils.getCurrentUserLogin()));
                convertUpdateChamCongDTOtoTcChamCongEntity(updateChamCongDTO, tcChamCongEntity);
            }
        }
    }

    private void convertUpdateChamCongDTOtoTcChamCongEntity(UpdateChamCongDTO updateChamCongDTO,
                                                            TcChamCongEntity tcChamCongEntity) {

        tcChamCongEntity.setGhiChu(updateChamCongDTO.getGhiChu());
        tcChamCongEntity.setGioThucTe(updateChamCongDTO.getGioThucTe());
        tcChamCongEntity.setKhachPhatSinh(updateChamCongDTO.getKhachPhatSinh());

        tcChamCongEntity.setTaiXeId(updateChamCongDTO.getTaiXeId());
        tcChamCongEntity.setTcCaId(updateChamCongDTO.getCaId());
        tcChamCongEntity.setXeId(updateChamCongDTO.getXeId());

        LocalDateTime currentTime = LocalDateTime.now();
        if (tcChamCongEntity.getCreatedDate() == null) {
            tcChamCongEntity.setCreatedDate(currentTime);
        }

        tcChamCongEntity.setLastUpdatedDate(currentTime);
        tcChamCongEntity.setLastUpdatedBy(String.valueOf(SecurityUtils.getCurrentUserLogin()));
        tcChamCongRepository.save(tcChamCongEntity);

        //TODO cập nhật thông tin các lệnh tương ứng
        int taiXeId = tcChamCongEntity.getTaiXeId();
        int xeId = tcChamCongEntity.getXeId();
        LocalDate lDate = tcChamCongEntity.getLastUpdatedDate().toLocalDate();
        List<Object> lenhIds = tcLenhRepositoryCustom.findChamCongByLaiXeIdAndLastUpdatedDate(taiXeId, lDate);
        lenhIds.forEach(lenhId -> {
            if (lenhId instanceof Integer) {
                Optional<TcLenhEntity> lenh = tcLenhRepository.findById(Integer.parseInt(lenhId.toString()));
                if (lenh.isPresent()) {
                    TcLenhEntity lenhEntity = lenh.get();
                    log.debug("START: CẬP NHẬT LỆNH: {}", lenhEntity.getTcLenhId());
                    lenhEntity.setXeId(xeId);
                    log.debug("Đổi xe do đổi lịch: {}", xeId);
                    tcLenhRepository.save(lenhEntity);
                    log.debug("END: CẬP NHẬT LỆNH: {}", lenhEntity.getTcLenhId());
                }
            }
        });
    }

    @Override
    @Transactional
    public void xoaCaTruc(int id) {
        log.info("Delete ca truc va cham cong voi id: {}", id);
        TcLichTrucEntity tcLichTrucEntity = tcLichTrucRepository.findById(id).orElse(null);
        if (tcLichTrucEntity == null) {
            throw new TransportException("Không tìm thấy lịch trực");
        }
        if (tcLichTrucEntity.getNgayTruc().isBefore(LocalDate.now())) {
            throw new TransportException("Không thể xóa phân lịch của ngày nhỏ hơn hiện tại");
        }
        //Xóa phân lịch
        tcLichTrucRepository.delete(tcLichTrucEntity);
        tcLichTrucRepository.flush();
        //Xóa trong bảng chấm công
        tcChamCongRepository.deleteByTaiXeAndNgayTruc(tcLichTrucEntity.getTaiXeId(),
                                                      tcLichTrucEntity.getNgayTruc());

    }
}
