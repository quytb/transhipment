package com.havaz.transport.batch.service.impl;

import com.havaz.transport.batch.model.MonitorTcvDTO;
import com.havaz.transport.batch.model.MonitorTcvTraDTO;
import com.havaz.transport.batch.repository.MonitoringTransferTicketRepositoryCustom;
import com.havaz.transport.batch.service.MonitorTransferTcvService;
import com.havaz.transport.core.utils.CommonUtils;
import com.havaz.transport.dao.entity.BanVeVeEntity;
import com.havaz.transport.dao.entity.TcMonitorTransferVeEntity;
import com.havaz.transport.dao.repository.BvvRepository;
import com.havaz.transport.dao.repository.TcMonitorTransferVeRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class MonitorTransferTcvServiceImpl implements MonitorTransferTcvService {

    private static final Logger log = LoggerFactory.getLogger(MonitorTransferTcvServiceImpl.class);

    @Value("${havaz.sms.hotlines}")
    private List<String> hotlines;

    @Value("${havaz.url-monitor-transfer-ticket}")
    private String url;

    @Value("${havaz.url-erp-token}")
    private String token;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MonitoringTransferTicketRepositoryCustom monitoringTransferTicketRepositoryCustom;

    @Autowired
    private TcMonitorTransferVeRepository tcMonitorTransferVeRepository;

    @Autowired
    private BvvRepository bvvRepository;

    private final String smsMessage;

    private static final int NUMBER_MINUTE_TO_CALL_API = 1;

    @Autowired
    public MonitorTransferTcvServiceImpl(Environment environment) {
        Assert.notNull(environment, "environment cannot be null");
        String[] activeProfiles = environment.getActiveProfiles();
        this.smsMessage = String.join(StringUtils.EMPTY, activeProfiles) + ": CO LOI DONG BO VE, CAN KIEM TRA!";
    }

    @Override
    public List<MonitorTcvDTO> getReport(boolean isPickup) {
        return monitoringTransferTicketRepositoryCustom.calculateData(isPickup);
    }

    @Override
    public MonitorTcvTraDTO getReportTra(int didId) {
        return monitoringTransferTicketRepositoryCustom.calculateDataTra(didId);
    }

    @Override
    @Transactional
    public void insertMonitorRecord() {


        List<MonitorTcvDTO> monitorTcvDTOList = this.getReport(true);
        List<MonitorTcvDTO> monitorTcvTraDTOS = this.getReport(false);
        Set<Integer> didIds = new HashSet<>();
        executeTrackingMonitoringTicket(monitorTcvDTOList, monitorTcvTraDTOS, didIds);
        // call API reTransferTicket
        didIds.forEach(didId -> transferTicketByDidId(didId));
    }

    private TcMonitorTransferVeEntity initMonitorEntity(MonitorTcvTraDTO monitorTcvTraDTO, MonitorTcvDTO monitor) {
        TcMonitorTransferVeEntity e = new TcMonitorTransferVeEntity();
        e.setMonitorTrip(monitor.getMonitorTrip());
        int erpCountDon = monitor.getMonitorCountVeErp() ==  null ? 0 :Integer.valueOf(monitor.getMonitorCountVeErp());
        int tcCountDon = monitor.getMonitorCountVeTc() ==  null ? 0 :Integer.valueOf(monitor.getMonitorCountVeTc());
        int erpCountTra = monitorTcvTraDTO.getMonitorCountVeErpTra() ==  null ? 0 : Integer.valueOf(monitorTcvTraDTO.getMonitorCountVeErpTra());
        int tcCountTra = monitorTcvTraDTO.getMonitorCountVeErpTra() == null ? 0 : Integer.valueOf(monitorTcvTraDTO.getMonitorCountVeTcTra());

        e.setMonitorCountVeErpDon(erpCountDon);
        e.setMonitorCountVeTcDon(tcCountDon);
        e.setMonitorCountVeErpTra(erpCountTra);
        e.setMonitorCountVeTcTra(tcCountTra);
        e.setMonitorTuyen(monitor.getMonitorTuyen());
        e.setMonitorBks(monitor.getMonitorBks());
        e.setMonitorGxb(monitor.getMonitorGxb());
        if (monitor.getMonitorChieu() == null || "1".equals(monitor.getMonitorChieu())) {
            e.setMonitorChieu("CHIỀU A");
        } else {
            e.setMonitorChieu("CHIỀU B");
        }
        //when lost Ticket
        if (erpCountDon > tcCountDon || erpCountTra > tcCountTra) {
            e.setStatus(0);
        } else {
            e.setStatus(1);
        }

        return e;

    }

    private void executeTrackingMonitoringTicket(List<MonitorTcvDTO> monitorTcvDTOS,
                                                 List<MonitorTcvDTO> monitorTcvTraDTOS,
                                                 Set<Integer> didIds) {
        List<TcMonitorTransferVeEntity> list = new ArrayList<>();
        AtomicBoolean isLostTicket = new AtomicBoolean(false);
        monitorTcvDTOS.forEach(monitor -> {
            MonitorTcvTraDTO monitorTcvTraDTO = this.getReportTra(Integer.valueOf(monitor.getMonitorTrip()));
            removeTripHasBeenMonitor(monitor, monitorTcvTraDTOS);
            TcMonitorTransferVeEntity e = initMonitorEntity(monitorTcvTraDTO, monitor);
            Integer checkStatus = 0;
            //when ticket is lost
            if (checkStatus == e.getStatus()) {
                setListMissedTicket(monitor, monitorTcvTraDTO);
                initListTripIdToCallApiSyncTicket(monitor.getMissedTickets(), didIds);
                initListTripIdToCallApiSyncTicket(monitorTcvTraDTO.getMissedTickets(), didIds);
                log.info(StringUtils.join("Monitoring Ticket From Erp PickUp BvvIds: ", monitor.getErpBvvIds()));
                log.info(StringUtils.join("Monitoring Ticket From TC PickUp BvvIds: ", monitor.getTcBvvIds()));
                log.info(StringUtils.join("Monitoring Ticket From Erp Drop Off BvvIds : ", monitorTcvTraDTO.getErpBvvIds()));
                log.info(StringUtils.join("Monitoring Ticket From TC Drop Off BvvIds: ", monitorTcvTraDTO.getTcBvvIds()));
                isLostTicket.set(true);
            }
            list.add(e);
        });
        monitorTcvTraDTOS.forEach(monitorTcvTraDTO -> {
            int erpCount = monitorTcvTraDTO.getMonitorCountVeErp() == null ? 0 :
                    Integer.valueOf(monitorTcvTraDTO.getMonitorCountVeErp());
            int tcCount = monitorTcvTraDTO.getMonitorCountVeTc() == null ? 0 :
                    Integer.valueOf(monitorTcvTraDTO.getMonitorCountVeTc());
            TcMonitorTransferVeEntity e = new TcMonitorTransferVeEntity();
            e.setMonitorTrip(monitorTcvTraDTO.getMonitorTrip());
            e.setMonitorCountVeErpTra(erpCount);
            e.setMonitorCountVeTcTra(tcCount);
            e.setMonitorTuyen(monitorTcvTraDTO.getMonitorTuyen());
            e.setMonitorBks(monitorTcvTraDTO.getMonitorBks());
            e.setMonitorGxb(monitorTcvTraDTO.getMonitorGxb());
            e.setMonitorChieu("CHIỀU B");
            if (erpCount > tcCount) {
                setListMissedTicket(monitorTcvTraDTO, new MonitorTcvTraDTO());
                initListTripIdToCallApiSyncTicket(monitorTcvTraDTO.getMissedTickets(), didIds);
                log.info(StringUtils.join("Monitoring Ticket From Erp Drop Off BvvIds : ", monitorTcvTraDTO.getErpBvvIds()));
                log.info(StringUtils.join("Monitoring Ticket From TC Drop Off BvvIds: ", monitorTcvTraDTO.getTcBvvIds()));
                isLostTicket.set(true);
            }
            list.add(e);

        });
        if (!list.isEmpty()) {
            tcMonitorTransferVeRepository.saveAll(list);
        }
        if (isLostTicket.get()) {
            try {
                hotlines.forEach(hotline -> CommonUtils.sendSMS(hotline, this.smsMessage));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void removeTripHasBeenMonitor(MonitorTcvDTO monitorTcvDTO, List<MonitorTcvDTO> monitorTcvTraDTOS) {
        Iterator<MonitorTcvDTO> itr = monitorTcvTraDTOS.iterator();
        while (itr.hasNext()) {
            MonitorTcvDTO monitorTcvTraDTO = itr.next();
            if (Objects.equals(monitorTcvDTO.getMonitorTrip(), monitorTcvTraDTO.getMonitorTrip())) {
                itr.remove();
            }

        }
    }

    private void transferTicketByDidId(Integer didId) {
        log.info("Start Auto Call API Sync Ticket");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "cron_bvv_tt_update_trip.php")
                .queryParam("did_id", didId)
                .queryParam("token", token);
        log.debug(builder.toUriString());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                String.class);
        log.info(StringUtils.join("End Call Api Sync Ticket : ", responseEntity.getStatusCode().toString()));
    }

    private void setListMissedTicket(MonitorTcvDTO monitorTcvDTO, MonitorTcvTraDTO monitorTcvTraDTO) {
        String erpTicket = monitorTcvDTO.getErpBvvIds();
        String tcTicket = monitorTcvDTO.getTcBvvIds();
        String erpTicketDropOff = monitorTcvTraDTO.getErpBvvIds();
        String tcTicketDropOff = monitorTcvTraDTO.getTcBvvIds();
        Set<Integer> idErpTickets = convertStringToArrayInteger(erpTicket);
        Set<Integer> idTcTickets = convertStringToArrayInteger(tcTicket);
        Set<Integer> idErpTicketsDropOff = convertStringToArrayInteger(erpTicketDropOff);
        Set<Integer> idTcTicketsDropOff = convertStringToArrayInteger(tcTicketDropOff);
        monitorTcvDTO.setMissedTickets(initMissedTickets(idErpTickets, idTcTickets));
        monitorTcvTraDTO.setMissedTickets(initMissedTickets(idErpTicketsDropOff, idTcTicketsDropOff));
    }

    private List<BanVeVeEntity> initMissedTickets(Set<Integer> idErpTickets, Set<Integer> idTcTickets) {
        List<BanVeVeEntity> missedTickets = new ArrayList<>();
        idErpTickets.forEach(id -> {
            if (!idTcTickets.contains(id)) {
                Optional<BanVeVeEntity> optionalBanVeVeEntity = bvvRepository.findById(id);
                if (optionalBanVeVeEntity.isPresent()) {
                    missedTickets.add(optionalBanVeVeEntity.get());
                }
            }
        });
        return missedTickets;
    }

    private Set<Integer> convertStringToArrayInteger(String arr) {
        if (Strings.isEmpty(arr)) {
            return new HashSet<>();
        }
        return Arrays.stream(Stream.of(arr.split(",")).mapToInt(Integer::parseInt).toArray()).boxed()
                .collect(Collectors.toSet());
    }

    private void initListTripIdToCallApiSyncTicket(List<BanVeVeEntity> banVeVeEntities, Set<Integer> didIds) {
        banVeVeEntities.forEach(banVeVeEntity -> {
            LocalDateTime lastTimeUpdateErpTicket =
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(banVeVeEntity.getBvvTimeLastUpdate()),
                            ZoneId.systemDefault());
            LocalDateTime currentTime = LocalDateTime.now();
            long timeBetween = ChronoUnit.MINUTES.between(lastTimeUpdateErpTicket, currentTime);
            if (timeBetween >= NUMBER_MINUTE_TO_CALL_API) {
                didIds.add(banVeVeEntity.getBvvBvnId());
            }
        });
    }
}
