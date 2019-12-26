package com.havaz.transport.batch.configuration;

import com.havaz.transport.batch.quartzjob.MonitorTransferTicketJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.Assert;

import java.util.TimeZone;

@Configuration
public class QuartzConfig {

    private final String monitorTransferTicketCronExpression;

    public QuartzConfig(@Value("${havaz.quartz.cron-expression.monitor-transfer-ticket}") String monitorTransferTicketCronExpression) {
        Assert.hasText(monitorTransferTicketCronExpression, "monitorTransferTicketCronExpression must not empty or blank");
        this.monitorTransferTicketCronExpression = monitorTransferTicketCronExpression;
    }

    @Bean
    public SchedulerFactoryBeanCustomizer customizer() {
        return new SchedulerFactoryBeanCustomizer() {
            @Override
            public void customize(SchedulerFactoryBean schedulerFactoryBean) {
                schedulerFactoryBean.setStartupDelay(5);
            }
        };
    }

    @Bean
    public JobDetail monitorTransferTicketJob() {
        return JobBuilder.newJob(MonitorTransferTicketJob.class)
                .withIdentity(MonitorTransferTicketJob.JOB_NAME,
                              MonitorTransferTicketJob.GROUP)
                .storeDurably().build();
    }

    @Bean
    public CronTrigger monitorTransferTicketTrigger() {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                .cronSchedule(this.monitorTransferTicketCronExpression)
                .inTimeZone(TimeZone.getDefault());
        return TriggerBuilder.newTrigger().forJob(monitorTransferTicketJob())
                .withIdentity(MonitorTransferTicketJob.TRIGGER_NAME,
                              MonitorTransferTicketJob.GROUP)
                .withSchedule(cronScheduleBuilder).build();
    }
}
