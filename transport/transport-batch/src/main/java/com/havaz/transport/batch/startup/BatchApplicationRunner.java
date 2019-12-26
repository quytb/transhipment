package com.havaz.transport.batch.startup;

import com.havaz.transport.core.constant.EnvironmentProfile;
import com.havaz.transport.core.utils.SpringUtil;
import com.havaz.transport.core.utils.Strings;
import io.sentry.Sentry;
import io.sentry.SentryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class BatchApplicationRunner implements ApplicationRunner {

    private static final String SENTRY_DSN = "https://1d45918068b34580b860f40d7da66e10@sentry.havaz.vn/19";

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
            init.addTag("Module", "BATCH");
        }
    }
}
