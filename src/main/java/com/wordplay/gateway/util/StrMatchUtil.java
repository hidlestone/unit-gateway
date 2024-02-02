package com.wordplay.gateway.util;

import cn.hutool.core.collection.CollUtil;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhuangpf
 */
public class StrMatchUtil {

	/**
	 * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
	 *
	 * @param str  指定字符串
	 * @param strs 需要检查的字符串数组
	 * @return 是否匹配
	 */
	public static boolean matches(String str, List<String> strs) {
		if (CollUtil.isEmpty(strs)) {
			return false;
		}
		for (String pattern : strs) {
			if (isMatch(pattern, str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通配符匹配要过滤的路径
	 *
	 * @param path 要匹配的路径
	 * @param urls Spring通配符路径
	 */
	public static boolean matches(String path, String... urls) {
		return Arrays.stream(urls).anyMatch(igUrl -> {
			PathMatcher pm = new AntPathMatcher();
			return pm.match(igUrl, path);
		});
	}

	/**
	 * 判断url是否与规则配置:
	 * ? 表示单个字符;
	 * * 表示一层路径内的任意字符串，不可跨层级;
	 * ** 表示任意层路径;
	 *
	 * @param pattern 匹配规则
	 * @param url     需要匹配的url
	 */
	public static boolean isMatch(String pattern, String url) {
		AntPathMatcher matcher = new AntPathMatcher();
		return matcher.match(pattern, url);
	}

}
