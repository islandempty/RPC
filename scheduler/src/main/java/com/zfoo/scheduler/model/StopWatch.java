package com.zfoo.scheduler.model;

import com.zfoo.scheduler.util.TimeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author islandempty
 * @since 2021/9/10
 **/
public class StopWatch {
    private long startTime = TimeUtils.currentTimeMillis();

    public long cost() {
        return TimeUtils.currentTimeMillis() - startTime;
    }

    /**
     * 从StopWatch被创建，到调用这个方法消耗的时间
     *
     * @return 返回消耗的时间，保留两位小数，格式xx.xx
     */
    public String costSeconds() {
        var cost = cost() / (float) TimeUtils.MILLIS_PER_SECOND;
        var decimal = new BigDecimal(cost);
        return decimal.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public String costMinutes() {
        var cost = cost() / (float) TimeUtils.MILLIS_PER_MINUTE;
        var decimal = new BigDecimal(cost);
        return decimal.setScale(2, RoundingMode.HALF_UP).toString();
    }

    public long getStartTime() {
        return startTime;
    }
}

