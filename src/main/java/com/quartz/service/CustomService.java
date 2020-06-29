package com.quartz.service;

import com.quartz.scheduler.QuartzScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cuixuyang
 * @date 2019/11/19 14:32
 * @description
 */
@Service
public interface CustomService {

    void doSomeThing();
}
