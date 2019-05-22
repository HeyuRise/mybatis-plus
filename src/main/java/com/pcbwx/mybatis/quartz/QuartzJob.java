package com.pcbwx.mybatis.quartz;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.pcbwx.mybatis.service.SupportService;

/**
 * 定时调度类
 * @author 孙贺宇
 * @version 1.0
 *
 */
@Configuration
@EnableScheduling
public class QuartzJob {
	
	private Logger logger = LoggerFactory.getLogger(QuartzJob.class);
	
	@Autowired
	private SupportService supportService;
	
	private static AtomicInteger reloadFlag = new AtomicInteger();
	@Scheduled(fixedRateString = "${reload.timer.fixedRate}") // 5分钟
	public void reloadCache() {
		logger.info("reloadCache的任务调度！！！");
		if (reloadFlag.incrementAndGet() > 1) {
			reloadFlag.decrementAndGet();
			return;
		}
		try {
			supportService.doReloadCache();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		reloadFlag.decrementAndGet();		
		logger.info("reloadCache的任务调度结束！！！");
	}

	private static AtomicInteger planGenerateFlag = new AtomicInteger();	
	@Scheduled(cron = "${plan.generate.timer.corn}") // 1分钟
	public void generateExePlan() {
		logger.info("planGenerate的任务调度！！！");
		if (planGenerateFlag.incrementAndGet() > 1) {
			planGenerateFlag.decrementAndGet();
			return;
		}

		planGenerateFlag.decrementAndGet();	
		logger.info("planGenerate的任务调度结束！！！");
	}
	
}
