package com.wordplay.gateway.util;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {

	private static final long serialVersionUID = -2477262048060371180L;

	// 操作是否成功
	private boolean success;
	// 操作码
	private String code;
	// 返回信息
	private String message;
	// 响应结果，使用泛型，便于生成Swagger文档
	private T data;

	public ResponseResult(String code, String message) {
		this.code = code;
		this.message = message;
	}

}
