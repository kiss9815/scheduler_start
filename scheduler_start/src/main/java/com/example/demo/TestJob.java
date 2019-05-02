package com.example.demo;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestJob extends BaseJob{

	@Override
	protected void doExecute(JobExecutionContext context) {


		System.out.println("--------------------------job doExecute 수행---------------------------");
		log.info("### {} is being executed!",  context.getJobDetail().getJobDataMap().get("JobName").toString());
	}

}
