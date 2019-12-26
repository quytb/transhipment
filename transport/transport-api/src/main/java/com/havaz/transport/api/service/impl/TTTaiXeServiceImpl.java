package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.form.TTTaiXeForm;
import com.havaz.transport.api.service.DieuDoService;
import com.havaz.transport.api.service.TTTaiXeService;
import com.havaz.transport.core.exception.TransportException;
import com.havaz.transport.dao.entity.TTTaiXe;
import com.havaz.transport.dao.entity.TTTaiXeLogs;
import com.havaz.transport.dao.repository.TTTaiXeLogsRepository;
import com.havaz.transport.dao.repository.TTTaiXeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
public class TTTaiXeServiceImpl implements TTTaiXeService {

    private static final Logger LOG = LoggerFactory.getLogger(TTTaiXeServiceImpl.class);

    @Autowired
    private TTTaiXeRepository ttTaiXeRepository;

    @Autowired
    private TTTaiXeLogsRepository ttTaiXeLogsRepository;

    @Autowired
    private DieuDoService dieuDoService;

    @Override
    @Transactional
    public TTTaiXe getTTTaiXeByTaiXeId(int taiXeId) {
        TTTaiXe taiXeExcist;
        LocalDateTime currentTime = LocalDateTime.now();
        try {
            taiXeExcist = ttTaiXeRepository.findTopByTaiXeIdOrderByLastUpdatedAtDesc(taiXeId);
            if(taiXeExcist == null){
                taiXeExcist = new TTTaiXe();
                taiXeExcist.setTaiXeId(taiXeId);
                taiXeExcist.setTrangThai(1);
                taiXeExcist.setLastUpdatedAt(currentTime);
                ttTaiXeRepository.save(taiXeExcist);
            }
        }catch (Exception ex){
            LOG.error(ex.getMessage());
            throw new TransportException("Không tìm thấy trạng thái của tài xế");
        }
        return taiXeExcist;
    }

    @Override
    public TTTaiXe getTTTaiXeByTaiXeIdForDieuHanh(int taiXeId, LocalDate date) {
        TTTaiXe taiXeExcist;
        //check trong phần phân lịch hoặc chấm công hoặc phân lịch.
        int idXe = dieuDoService.getIdXeTuChamCongHoacPhanLich(taiXeId, date);
        if(idXe == 0)
            throw new TransportException("Lái xe chưa có dữ liệu trong phần chấm công hoặc phân lịch cho ngày "+date);

        //nếu đã có thì xem trạng thái của lái xe đó có sẵn sàng không để cảnh báo cho người điều độ
        try {
            taiXeExcist = ttTaiXeRepository.findTopByTaiXeIdOrderByLastUpdatedAtDesc(taiXeId);
        }catch (Exception ex){
            LOG.error(ex.getMessage());
            throw new TransportException("Không tìm thấy trạng thái của tài xế");
        }
        return taiXeExcist;
    }

    @Override
    @Transactional
    public boolean updateTrangThaiTaiXe(TTTaiXeForm ttTaiXeForm) {
        LOG.info("Bắt đầu lưu trạng thái cho lái xe: "+ttTaiXeForm.getTaiXeId());
        //check tai xe nay da co trong bang trang thai chua
        //Neu chua thi tao moi va set trang thai moi
        LocalDateTime currentTime = LocalDateTime.now();
        TTTaiXe taiXeExcist = ttTaiXeRepository.findTopByTaiXeIdOrderByLastUpdatedAtDesc(ttTaiXeForm.getTaiXeId());
        if (taiXeExcist != null) {
            //Neu cap nhat trang thai moi = voi trang thai cu => ko cho cap nhat
            if (taiXeExcist.getTrangThai() == ttTaiXeForm.getTrangThai()) {
                return true;
            }

            saveTTTaiXeLogs(taiXeExcist);
            taiXeExcist.setTrangThai(ttTaiXeForm.getTrangThai());
            taiXeExcist.setLastUpdatedAt(currentTime);
            ttTaiXeRepository.save(taiXeExcist);
        } else {
            TTTaiXe ttTaiXeNew = new TTTaiXe();
            ttTaiXeNew.setTaiXeId(ttTaiXeForm.getTaiXeId());
            ttTaiXeNew.setTrangThai(ttTaiXeForm.getTrangThai());
            ttTaiXeNew.setLastUpdatedAt(currentTime);
            ttTaiXeRepository.save(ttTaiXeNew);
        }
        LOG.info("Lưu trạng thái cho lái xe: {} là {} thành công", ttTaiXeForm.getTaiXeId(), ttTaiXeForm.getTrangThai());
        return true;
    }

    @Override
    public boolean checkTTTaiXeSanSang(int taiXe) {
        try {
            TTTaiXe taiXeExcist = ttTaiXeRepository.findTopByTaiXeIdOrderByLastUpdatedAtDesc(taiXe);
            if (taiXeExcist != null) {
                if (taiXeExcist.getTrangThai() == 1) {
                    return true;
                }
            }
        } catch (Exception ex) {
            LOG.warn(ex.getMessage(), ex);
            return false;
        }
        return false;
    }

    private void saveTTTaiXeLogs(TTTaiXe ttTaiXe){
        LocalDateTime currentTime = LocalDateTime.now();
        // long duration = CommonUtils.compareTwoLocalDateTimes(LocalDateTime,ttTaiXe.getLastUpdatedAt());
        long duration = ChronoUnit.HOURS.between(currentTime, ttTaiXe.getLastUpdatedAt());
        TTTaiXeLogs ttTaiXeLogs = new TTTaiXeLogs();
        ttTaiXeLogs.setTtTaiXeId(ttTaiXe.getId());
        ttTaiXeLogs.setDuration((int) duration);
        ttTaiXeLogs.setLastUpdatedAt(currentTime);
        ttTaiXeLogsRepository.save(ttTaiXeLogs);
    }
}
