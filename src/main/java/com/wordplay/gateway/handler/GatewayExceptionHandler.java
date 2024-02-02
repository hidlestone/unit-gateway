package com.wordplay.gateway.handler;

import com.wordplay.gateway.util.ServletUtil;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关统一异常处理
 */
@Order(-1)
@Configuration
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		ServerHttpResponse response = exchange.getResponse();
		if (exchange.getResponse().isCommitted()) {
			return Mono.error(ex);
		}
		String msg;

		if (ex instanceof NotFoundException) {
			msg = "服务未找到";
		} else if (ex instanceof ResponseStatusException) {
			ResponseStatusException responseStatusException = (ResponseStatusException) ex;
			msg = responseStatusException.getMessage();
		} else {
			msg = "内部服务器错误";
		}
		ServerHttpRequest request = exchange.getRequest();
		String errorInfo = String.format("[网关异常处理]Host:%s, 请求路径:%s,异常信息:%s", request.getURI().getHost(), exchange.getRequest().getPath(), ex.getMessage());
		// TODO 异常日志搜集
		// ThreadPoolUtil.getPool().execute(() -> ExceptionUtils.addExceptionLog(new BusinessException(errorInfo)));
		return ServletUtil.webFluxResponseWriter(response, msg);
	}

}
