package com.quartz.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * @author cuixuyang
 * @date 2019/11/19 14:23
 * @description  配置SchedulerFactoryBean以及加载quartz.properties
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private JobFactory jobfactory;

    @Bean(name = "SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException, SchedulerException {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        //如:修改表达式后覆盖原job
        factoryBean.setOverwriteExistingJobs(true);
        //延时启动
        factoryBean.setStartupDelay(20);
        //自定义jobFactory  用于注入
        factoryBean.setJobFactory(jobfactory);
        factoryBean.setQuartzProperties(quartzProperties());
        System.out.println("---------"+ quartzProperties().getProperty("org.quartz.scheduler.instanceName"));
        return factoryBean;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * quartz 初始化监控器
     */
    @Bean
    public QuartzInitializerListener executorListener(){
        return new QuartzInitializerListener();
    }

    /**
     * 通过SchedulerFactoryBean获取Scheduler的实例
     * @return
     * @throws IOException
     */
    @Bean(name="Scheduler")
    public Scheduler scheduler() throws Exception {
        return schedulerFactoryBean().getScheduler();
    }
}
