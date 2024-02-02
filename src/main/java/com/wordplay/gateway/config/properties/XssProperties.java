package com.wordplay.gateway.config.properties;

import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * XSS跨站脚本配置，nacos自行添加
 */
@RefreshScope
public class XssProperties {

	/**
	 * Xss开关
	 */
	private Boolean enabled;

	/**
	 * 排除路径
	 */
	private List<String> excludeUrls = new ArrayList<>();

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}
	
}