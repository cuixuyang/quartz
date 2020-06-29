package com.quartz.scheduler;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private QuartzScheduler quartzScheduler;

    /**
     * 初始启动quartz
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //监听springboot启动，在启动时do some thing
        try {
            quartzScheduler.resumeAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
