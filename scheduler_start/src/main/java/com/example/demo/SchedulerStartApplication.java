package com.example.demo;

import java.util.LinkedList;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


@SpringBootApplication
public class SchedulerStartApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SchedulerStartApplication.class, args);
		
		
		start();
		
		//https://github.com/kingbbode/spring-batch-quartz/blob/master/src/main/java/com/kingbbode/config/BatchConfiguration.java 
		//참고하자
	}
	

	public static void start() throws Exception{
		
		System.out.println("start");
	
//		JobDetail jobDetail = newJob(TestJob.class)
//                .build();
		
		// JobDataMap을 이용해서 원하는 정보 담기
	    // Job 1 구성
        JobDataMap jobDataMap1 = new JobDataMap();
        jobDataMap1.put("JobName", "Job Chain 1");
        JobDetail jobDetail1 = newJob(TestJob.class)
                .usingJobData(jobDataMap1)
                .build();

        // Job 2 구성
        JobDataMap jobDataMap2 = new JobDataMap();
        jobDataMap2.put("JobName", "Job Chain 2");
        JobDetail jobDetail2 = newJob(TestJob.class)
                .usingJobData(jobDataMap2)
                .build();

        // Job 3 구성
        JobDataMap jobDataMap3 = new JobDataMap();
        jobDataMap3.put("JobName", "Job Chain 3");
        JobDetail jobDetail3 = newJob(TestJob.class)
                .usingJobData(jobDataMap3)
                .build();
        
     // 실행할 모든 Job의 JobDetail를 jobDetail1의 JobDataMap에 담는다.
        List<JobDetail> jobDetailQueue = new LinkedList<>();
        jobDetailQueue.add(jobDetail1);
        jobDetailQueue.add(jobDetail2);
        jobDetailQueue.add(jobDetail3);
        jobDetail1.getJobDataMap().put("JobDetailQueue", jobDetailQueue);


        // 실행 시점을 결정하는 Trigger 생성
        Trigger trigger = newTrigger().build();

        // 스케줄러 실행 및 JobDetail과 Trigger 정보로 스케줄링
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
	    defaultScheduler.start();
        defaultScheduler.scheduleJob(jobDetail1, trigger);
        //Thread.sleep(3 * 1000);  // Job이 실행될 수 있는 시간 여유를 준다
       
        
        // 스케줄러 종료
//        defaultScheduler.shutdown(true);
          
	}
	
	@Bean
	public JobDetail sampleJobDetail() {
		return JobBuilder.newJob(SampleJob.class).withIdentity("sampleJob")
				.usingJobData("name", "World").storeDurably().build();
	}

	@Bean
	public Trigger sampleJobTrigger() {
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInSeconds(2).repeatForever();

		return TriggerBuilder.newTrigger().forJob(sampleJobDetail())
				.withIdentity("sampleTrigger").withSchedule(scheduleBuilder).build();
	}
}