package com.havaz.transport.api.service.impl;

import com.havaz.transport.api.model.TcMonitorTransferTicketDTO;
import com.havaz.transport.api.repository.TcMonitorTransferVeRepositoryCustom;
import com.havaz.transport.api.service.TcMonitorTransferTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TcMonitorTransferTicketServiceImpl implements TcMonitorTransferTicketService {

    private static final Logger log = LoggerFactory.getLogger(TcMonitorTransferTicketServiceImpl.class);

    @Value("${havaz.url-monitor-transfer-ticket}")
    private String url;

    @Value("${havaz.url-erp-token}")
    private String token;

    @Autowired
    private TcMonitorTransferVeRepositoryCustom tcMonitorTransferVeRepositoryCustom;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<TcMonitorTransferTicketDTO> findByNgayAndTrangThai(LocalDate ngayKiemTra, int trangThai) {
        return tcMonitorTransferVeRepositoryCustom.findByNgayAndTrangThai(ngayKiemTra, trangThai);
    }

    @Override
    public void updateTransferTicket(String didId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "cron_bvv_tt_update_trip.php")
                .queryParam("did_id", didId)
                .queryParam("token", token);
        log.debug(builder.toUriString());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
    }
}
