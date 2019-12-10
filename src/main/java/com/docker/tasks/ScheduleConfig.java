package com.docker.tasks;

import com.docker.entity.FileInfo;
import com.docker.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 定时任务配置
 * Created by CHEN on 2019/11/26.
 */
@Slf4j
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

    @Value("${fs.dir}")
    private String fileDir;

    /** 线程池线程数量 */
    private static final int THREAD_COUNT = 1;

    /**
     * 清除过期的分享文件信息, 每3秒钟执行一次
     */
    @Scheduled(cron = "0/3 * * * * ?")
    public void clearShareCache() {
        Map<String, Date> dataExpireMap = CacheUtil.dataExpireMap;
        Set<String> keysExpire = dataExpireMap.keySet();
        for (String t : keysExpire) {
            Date expireDate = CacheUtil.dataExpireMap.get(t);
            if (expireDate != null && expireDate.compareTo(new Date()) < 0) {
                CacheUtil.dataMap.remove(t);
                CacheUtil.dataExpireMap.remove(t);
            }
        }
    }


    /**
     * 三分钟更新树一次，以防其他方式更改
     * 更新时间问题有待解决
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void updateFileTree() {

        File file  = new File(fileDir);
        FileInfo fileInfo =  new FileInfo(file);
        FileTree.fileInfo = fileInfo;
        System.out.println("列表已更新~~~~~" );
    }


    /**
     * 线程配置, 让多个定时任务在同一个线程池的不同线程中并发执行
     *
     * @param scheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        // 手动创建线程, 可以规避资源耗尽, 且可以设置线程名称规则
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor( THREAD_COUNT, new BasicThreadFactory.Builder().namingPattern("Schedule-Thread-Pool-Thread%d").daemon(true).build() );
        scheduledTaskRegistrar.setScheduler(scheduledExecutorService);
    }
}