package com.wordplay.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.wordplay.gateway.handler.SentinelFallbackHandler;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

/**
 * 网关限流配置
 */
@Configuration
public class GatewayConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SentinelFallbackHandler sentinelGatewayExceptionHandler() {
		return new SentinelFallbackHandler();
	}

	@Bean
	@Order(-1)
	public GlobalFilter sentinelGatewayFilter() {
		return new SentinelGatewayFilter();
	}

	@PostConstruct
	public void doInit() {
		// 加载网关限流规则
		initGatewayRules();
	}

	/**
	 * 网关限流规则
	 */
	private void initGatewayRules() {
		// 一分钟内访问三次系统服务出现异常提示表示限流成功
//        Set<GatewayFlowRule> rules = new HashSet<>();
//        rules.add(new GatewayFlowRule("to-applet-api")
//                .setCount(3) // 限流阈值
//                .setIntervalSec(60)); // 统计时间窗口，单位是秒，默认是 1 秒
//        // 加载网关限流规则
//        GatewayRuleManager.loadRules(rules);
	}

}


