package com.havaz.transport.batch.quartzjob;

import com.havaz.transport.batch.service.MonitorTransferTcvService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.Assert;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class MonitorTransferTicketJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(MonitorTransferTicketJob.class);

    public static final String JOB_NAME = "MONITOR_TRANSFER_TICKET_JOB";
    public static final String TRIGGER_NAME = "MONITOR_TRANSFER_TICKET_TRIGGER";
    public static final String GROUP = "MONITOR_TRANSFER_TICKET_GROUP";

    private MonitorTransferTcvService monitorTransferTcvService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("--------------------JOB MONITOR TRANSFER TICKET START----------------");
        monitorTransferTcvService.insertMonitorRecord();
        log.info("--------------------JOB MONITOR TRANSFER TICKET END------------------");
    }

    @Autowired
    public void setMonitorTransferTcvService(MonitorTransferTcvService monitorTransferTcvService) {
        Assert.notNull(monitorTransferTcvService, "monitorTransferTcvService cannot be null");
        this.monitorTransferTcvService = monitorTransferTcvService;
    }
}
