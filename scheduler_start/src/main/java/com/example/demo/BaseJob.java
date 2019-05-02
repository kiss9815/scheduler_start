package com.example.demo;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import lombok.extern.slf4j.Slf4j;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
@Slf4j
public abstract class BaseJob implements Job{

	@Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        beforeExecute(context);
        doExecute(context);
        afterExecute(context);
        scheduleNextJob(context);
    }

    private void beforeExecute(JobExecutionContext context) {
        log.info("%%% Before executing job");
    }

    protected abstract void doExecute(JobExecutionContext context);

    private void afterExecute(JobExecutionContext context) {
        log.info("%%% After executing job");
        Object object = context.getJobDetail().getJobDataMap().get("JobDetailQueue");
        @SuppressWarnings("unchecked")
		List<JobDetail> jobDetailQueue = (List<JobDetail>) object;

        if (jobDetailQueue.size() > 0) {
            jobDetailQueue.remove(0);
        }
    }

    private void scheduleNextJob(JobExecutionContext context) {
        
        log.info("$$$ Schedule Next Job");
        Object object = context.getJobDetail().getJobDataMap().get("JobDetailQueue");
        List<JobDetail> jobDetailQueue = (List<JobDetail>) object;

        if (jobDetailQueue.size() > 0) {
            JobDetail nextJobDetail = jobDetailQueue.get(0);
            nextJobDetail.getJobDataMap().put("JobDetailQueue", jobDetailQueue);
            Trigger nowTrigger = newTrigger().startNow().build();

            try {
                // 아래의 팩토리 메서드는 이름이 같으면 여러번 호출해도 항상 동일한 스케줄러를 반환한다.
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();
                scheduler.scheduleJob(nextJobDetail, nowTrigger);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
        
        
    }

}
