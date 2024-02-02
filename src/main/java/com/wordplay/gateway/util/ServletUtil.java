package com.wordplay.gateway.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ServletUtil {

	/**
	 * UTF-8 字符集
	 */
	public static final String UTF8 = "UTF-8";
	/**
	 * 失败标记
	 */
	public static final Integer FAIL = 500;

	/**
	 * 内容编码
	 *
	 * @param str 内容
	 * @return 编码后的内容
	 */
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, UTF8);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * 设置webflux模型响应
	 *
	 * @param response ServerHttpResponse
	 * @param value    响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value) {
		return webFluxResponseWriter(response, HttpStatus.OK, value, FAIL);
	}

	/**
	 * 设置webflux模型响应
	 *
	 * @param response ServerHttpResponse
	 * @param code     响应状态码
	 * @param value    响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value, int code) {
		return webFluxResponseWriter(response, HttpStatus.OK, value, code);
	}

	/**
	 * 设置webflux模型响应
	 *
	 * @param response ServerHttpResponse
	 * @param status   http状态码
	 * @param code     响应状态码
	 * @param value    响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus status, Object value, int code) {
		return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, value, code);
	}

	/**
	 * 设置webflux模型响应
	 *
	 * @param response    ServerHttpResponse
	 * @param contentType content-type
	 * @param status      http状态码
	 * @param code        响应状态码
	 * @param value       响应内容
	 * @return Mono<Void>
	 */
	public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus status, Object value, int code) {
		response.setStatusCode(status);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
		ResponseResult result = new ResponseResult(String.valueOf(code), value.toString());
		DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(result).getBytes());
		return response.writeWith(Mono.just(dataBuffer));
	}

}
