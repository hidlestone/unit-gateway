package com.wordplay.gateway.filter;

import cn.hutool.core.util.IdUtil;
import com.wordplay.gateway.util.ServletUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Random;

/**
 * 网关鉴权
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

	/**
	 * TRACE_ID
	 **/
	public static final String TRACE_ID_KEY = "traceId";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpRequest.Builder mutate = request.mutate();
		String url = request.getURI().getPath();

		String tradId = IdUtil.getSnowflake(1, 1).nextIdStr() + ":" + new Random().nextInt(999999);
		this.addHeader(mutate, TRACE_ID_KEY, tradId);

		return chain.filter(exchange.mutate().request(mutate.build()).build());
	}

	/**
	 * 添加请求头
	 */
	private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
		if (value == null) {
			return;
		}
		String valueStr = value.toString();
		String valueEncode = ServletUtil.urlEncode(valueStr);
		mutate.header(name, valueEncode);
	}

	@Override
	public int getOrder() {
		return -200;
	}
	
}
