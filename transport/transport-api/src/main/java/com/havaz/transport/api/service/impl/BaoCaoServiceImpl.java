package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.form.BaoCaoForm;
import com.havaz.transport.api.form.BaoCaoLenhForm;
import com.havaz.transport.api.model.BaoCaoChamCongDTO;
import com.havaz.transport.api.model.BaoCaoLenhDTO;
import com.havaz.transport.api.model.BaoCaoTaiXeDTO;
import com.havaz.transport.api.model.NgayCongDTO;
import com.havaz.transport.api.repository.TcChamCongRepositoryCustom;
import com.havaz.transport.api.repository.TcLenhRepositoryCustom;
import com.havaz.transport.api.service.BaoCaoService;
import com.havaz.transport.api.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BaoCaoServiceImpl implements BaoCaoService {

    @Autowired
    private TcChamCongRepositoryCustom tcChamCongRepositoryCustom;

    @Autowired
    private TcLenhRepositoryCustom tcLenhRepositoryCustom;

    @Override
    public List<BaoCaoTaiXeDTO> getAll(BaoCaoForm requestBaoCaoForm) {
        int thang = DateTimeUtils.convertThangNam(requestBaoCaoForm.getThang())[0];
        int nam = DateTimeUtils.convertThangNam(requestBaoCaoForm.getThang())[1];
        int chiNhanh = requestBaoCaoForm.getChiNhanh();
        List<BaoCaoTaiXeDTO> baoCaoTaiXeDTOS = new ArrayList<>();
        List<BaoCaoChamCongDTO> baoCaoChamCongDTOS = tcChamCongRepositoryCustom
                .getDuLieuBaoCaoChamCong(thang, nam, chiNhanh);

        Map<Integer, List<BaoCaoChamCongDTO>> groupByTaiXeIdMap =
                baoCaoChamCongDTOS
                        .stream().collect(Collectors.groupingBy(BaoCaoChamCongDTO::getTaiXeId));

        groupByTaiXeIdMap.forEach((key, value) -> {
            BaoCaoTaiXeDTO baoCaoTaiXeDTO = new BaoCaoTaiXeDTO();
            baoCaoTaiXeDTO.setTaiXeId(key);
            baoCaoTaiXeDTO.setTaiXeTen(value.get(0).getTaiXeTen());
            List<NgayCongDTO> ngayCongDTOS = new ArrayList<>();
            double tongCong = 0;
            for (BaoCaoChamCongDTO baoCaoChamCongDTO : value) {
                NgayCongDTO ngayCongDTO = new NgayCongDTO();
                ngayCongDTO.setNgay(baoCaoChamCongDTO.getNgayChamCong());
                ngayCongDTO.setCong(baoCaoChamCongDTO.getCong());
                tongCong += baoCaoChamCongDTO.getCong();
                ngayCongDTOS.add(ngayCongDTO);
            }
            baoCaoTaiXeDTO.setTongCong(tongCong);
            baoCaoTaiXeDTO.setDuLieuThang(ngayCongDTOS);
            baoCaoTaiXeDTOS.add(baoCaoTaiXeDTO);
        });

        return baoCaoTaiXeDTOS;
    }

    @Override
    public List<BaoCaoLenhDTO> getAll(BaoCaoLenhForm baoCaoLenhForm) {
        LocalDate currentDate = LocalDate.now();
        if (baoCaoLenhForm.getFromDate() == null) {
            baoCaoLenhForm.setFromDate(currentDate);
        }

        if (baoCaoLenhForm.getEndDate() == null) {
            baoCaoLenhForm.setEndDate(currentDate);
        }
        return tcLenhRepositoryCustom.getReports(baoCaoLenhForm);
    }
}
