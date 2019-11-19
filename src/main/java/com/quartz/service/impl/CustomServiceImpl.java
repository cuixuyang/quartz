package com.quartz.service.impl;

import com.quartz.scheduler.QuartzScheduler;
import com.quartz.service.CustomService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author cuixuyang
 * @date 2019/11/19 14:32
 * @description
 */
@Component
public class CustomServiceImpl implements CustomService {
    @Autowired
    private QuartzScheduler quartzScheduler;


    @Override
    public void doSomeThing() {
        try {
            quartzScheduler.startJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        System.out.println("Hello");
    }
}
