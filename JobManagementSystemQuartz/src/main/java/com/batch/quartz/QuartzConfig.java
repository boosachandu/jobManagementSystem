/**
 * 
 */
package com.batch.quartz;

import java.io.IOException;
import java.util.Properties;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {
 
 @Bean
 public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
  JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
  jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
  
  return jobRegistryBeanPostProcessor;
 }
 
 @Bean
 public JobDetail moveCSVtoDbJob() {
     //Set Job data map
     JobDataMap jobDataMap = new JobDataMap();
     System.out.println("inside quartz job" + jobDataMap);
     jobDataMap.put("jobName", "moveCSVtoDb");
      
     return JobBuilder.newJob(QuartzJobLauncher.class)
             .withIdentity("moveCSVtoDb",null)
             .setJobData(jobDataMap)
             .storeDurably()
             .build();
 }
 
 @Bean
 public Trigger moveCSVtoDbTrigger()
 {
     SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
             .simpleSchedule()
             .withIntervalInSeconds(10)
             .repeatForever();

     return TriggerBuilder
             .newTrigger()
             .forJob(moveCSVtoDbJob())
             .withIdentity("moveCSVtoDbTrigger",null)
             .withSchedule(scheduleBuilder)
             .build();
 }
 
 @Bean
 public JobDetail updateAlertAgeJob() {
     //Set Job data map
     JobDataMap jobDataMap = new JobDataMap();
     System.out.println("inside quartz job 2" + jobDataMap);
     jobDataMap.put("jobName", "updateAlertAge");
      
     return JobBuilder.newJob(QuartzJobLauncher.class)
             .withIdentity("updateAlertAge",null)
             .setJobData(jobDataMap)
             .storeDurably()
             .build();
 }
 
 @Bean
 public Trigger updateAlertAgeTrigger()
 {
     SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
             .simpleSchedule()
             .withIntervalInSeconds(15)
             .repeatForever();

     return TriggerBuilder
             .newTrigger()
             .forJob(updateAlertAgeJob())
             .withIdentity("updateAlertAgeTrigger",null)
             .withSchedule(scheduleBuilder)
             .build();
 }
 
 
 @Bean
 public SchedulerFactoryBean schedulerFactoryBean() throws IOException, SchedulerException
 {
     SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
     scheduler.setTriggers(moveCSVtoDbTrigger(),updateAlertAgeTrigger());
     scheduler.setQuartzProperties(quartzProperties());
     scheduler.setJobDetails(moveCSVtoDbJob(),updateAlertAgeJob());
     scheduler.setApplicationContextSchedulerContextKey("applicationContext");
     return scheduler;
 }
  
 public Properties quartzProperties() throws IOException
 {
     PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
     propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
     propertiesFactoryBean.afterPropertiesSet();
     return propertiesFactoryBean.getObject();
 }
} 
