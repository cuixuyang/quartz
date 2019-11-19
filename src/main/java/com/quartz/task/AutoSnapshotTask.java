package com.quartz.task;

import com.quartz.service.CustomService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AutoSnapshotTask implements Job {

    @Autowired
    private CustomService customService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //获取参数
        String userId = context.getJobDetail().getJobDataMap().getString("userId");
        String userName = context.getJobDetail().getJobDataMap().getString("userName");
        customService.doSomeThing();
        }

}
