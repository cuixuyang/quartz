package com.quartz.scheduler;

import com.quartz.task.AutoSnapshotTask;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @author cuixuyang
 * @date 2019/11/19 14:25
 * @description
 */
@Configuration
public class QuartzScheduler {

    private static final Logger logger = LoggerFactory.getLogger(QuartzScheduler.class);
    // 任务调度
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;

    /**
     * 开始执行任务
     *
     * @throws SchedulerException
     */
    public void startJob() throws SchedulerException {
        startSnapshotJob();
        scheduler.start();
    }


    /**
     * 获取Job信息
     *
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    public Map<String,String> getJobInfo(String name, String group) throws SchedulerException {
        Map<String,String> map = new HashMap<>();
        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            map.put("time",cronTrigger.getCronExpression());
            map.put("state",scheduler.getTriggerState(triggerKey).name());
        } catch (NullPointerException e) {
            logger.error("job name = " + name + " is not existed");
        }
        return map;
    }


    /**
     * 修改某个任务的执行时间
     *
     * @param name
     * @param group
     * @param time
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

    /**
     *  启动策略
     */
    private void startSnapshotJob() {
        //创建一个jobDetail的实例，将该实例与SnapshotTask Class绑定
        JobDetail jobDetail = JobBuilder.newJob(AutoSnapshotTask.class).withIdentity("job","job_group").build();
        //设置参数
        jobDetail.getJobDataMap().put("userId","");
        jobDetail.getJobDataMap().put("userName","");
        //生成规则
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule("0 0 0 */1 * ?");
//                CronScheduleBuilder.cronSchedule("0 0/1 * * * ?");
        //创建一个Trigger触发器的实例，定义该job立即执行，并且按规则执行
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger","trigger_group").
                withSchedule(cronScheduleBuilder).build();
        //创建schedule实例
        try {
            scheduler.scheduleJob(jobDetail,cronTrigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
