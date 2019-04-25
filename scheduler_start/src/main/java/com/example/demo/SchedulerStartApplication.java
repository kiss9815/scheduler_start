package com.example.demo;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SchedulerStartApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerStartApplication.class, args);
		
//		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//
//        // and start it off
//        scheduler.start();
		
		
		//https://github.com/kingbbode/spring-batch-quartz/blob/master/src/main/java/com/kingbbode/config/BatchConfiguration.java 
		//참고하자
	}
	
	@Bean
	public void start() {
//		JobDetail jobDetail = 
//	            new JobDetail("myJob",// Job 명
//	              "name",  // Job 그룹명('null' 값인 경우 DEFAULT_GROUP 으로 정의됨)
//	              TestJob.class);       // 실행할 Job 클래스
//	 
//	  Trigger trigger = TriggerUtils.makeDailyTrigger(8, 30);  // 매일 08시 30분 실행
//	  trigger.setStartTime(new Date()); // 즉시 시작
//	  trigger.setName("myTrigger");
//	 
//	  sched.scheduleJob(jobDetail, trigger);
          
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