package com.zfoo.scheduler;

import com.zfoo.scheduler.model.anno.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author islandempty
 * @since 2021/7/4
 **/

@Component
public class SchedulerController {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    @Scheduler(cron = "0/5 * * * * ?")
    public void cronScheduler1(){
        logger.info("scheduler1 每5秒时间调度任务");
    }

    @Scheduler(cron = "0,10,20,40 * * * * ?")
    public void cronScheduler2(){
        logger.info("scheduler2 每分钟的10秒，20秒，40秒调度任务");
    }
}

