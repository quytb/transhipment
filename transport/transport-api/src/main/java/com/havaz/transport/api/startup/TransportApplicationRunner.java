package com.havaz.transport.api.startup;

import com.havaz.transport.api.common.CacheData;
import com.havaz.transport.api.mqtt.MQTTCallback;
import com.havaz.transport.api.service.CommonService;
import com.havaz.transport.core.constant.EnvironmentProfile;
import com.havaz.transport.core.utils.SpringUtil;
import com.havaz.transport.core.utils.Strings;
import com.havaz.transport.dao.entity.TcConfigurationEntity;
import com.havaz.transport.dao.repository.TcConfigRepository;
import io.sentry.Sentry;
import io.sentry.SentryClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TransportApplicationRunner implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(TransportApplicationRunner.class);

    private static final String SENTRY_DSN = "https://1d45918068b34580b860f40d7da66e10@sentry.havaz.vn/19";
    private static final String START_COUNT = "START_COUNT";

    @Autowired
    private CommonService commonService;

    @Autowired
    private TcConfigRepository tcConfigRepository;

    @Autowired
    private MQTTCallback mqttCallback;

    @Autowired
    private Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] activeProfiles = environment.getActiveProfiles();

        if (SpringUtil.isActiveProfiles(EnvironmentProfile.VUNG_TAU_PROD,
                                        EnvironmentProfile.HASON_HAIVAN_PROD,
                                        EnvironmentProfile.HAI_VAN_PROD)) {
            SentryClient init = Sentry.init(SENTRY_DSN);
            init.setEnvironment(String.join(Strings.DASH, activeProfiles));
            init.addTag("Module", "API");
        }

        LOG.info("Loading data startup... ");
        CacheData.THONG_TIN_NHA_XE = commonService.layThongTinNhaXe();
        CacheData.CONFIGURATION_DATA = commonService.getAllConfig();
        countStartSystem();
        LOG.info("Load data startup done.");

        // Listen MQTT
        mqttCallback.messageArrived();
    }

    private void countStartSystem() {
        String startCount = CacheData.CONFIGURATION_DATA.get(START_COUNT);
        if (StringUtils.isNotEmpty(startCount)) {
            int count = Integer.parseInt(startCount);
            CacheData.START_NUMBER = count;
            LOG.info("Start number:: {}", count);
            count++;
            TcConfigurationEntity tcConfigurationEntity = new TcConfigurationEntity();
            tcConfigurationEntity.setConfigKey(START_COUNT);
            tcConfigurationEntity.setConfigValue(String.valueOf(count));
            tcConfigRepository.save(tcConfigurationEntity);
        }
    }

}
