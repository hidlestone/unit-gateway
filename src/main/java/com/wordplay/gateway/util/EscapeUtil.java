package com.wordplay.gateway.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HTMLFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转义和反转义工具类
 */
public class EscapeUtil {

	public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";

	private static final char[][] TEXT = new char[64][];

	static {
		for (int i = 0; i < 64; i++) {
			TEXT[i] = new char[]{(char) i};
		}

		// special HTML characters
		TEXT['\''] = "&#039;".toCharArray(); // 单引号
		TEXT['"'] = "&#34;".toCharArray(); // 双引号
		TEXT['&'] = "&#38;".toCharArray(); // &符
		TEXT['<'] = "&#60;".toCharArray(); // 小于号
		TEXT['>'] = "&#62;".toCharArray(); // 大于号
	}

	/**
	 * 转义文本中的HTML字符为安全的字符
	 *
	 * @param text 被转义的文本
	 * @return 转义后的文本
	 */
	public static String escape(String text) {
		return encode(text);
	}

	/**
	 * 还原被转义的HTML特殊字符
	 *
	 * @param content 包含转义符的HTML内容
	 * @return 转换后的字符串
	 */
	public static String unescape(String content) {
		return decode(content);
	}

	/**
	 * 清除所有HTML标签，但是不删除标签内的内容
	 *
	 * @param content 文本
	 * @return 清除标签后的文本
	 */
	public static String clean(String content) {
		// 为了不开启该属性：encodeQuotes，重新定义了初始值
		// encodeQuotes 开启该属性，" 会转义成 &quot;
		Map<String, Object> conf = new HashMap<>();
		Map<String, List<String>> vAllowed = new HashMap();
		ArrayList<String> a_atts = new ArrayList();
		a_atts.add("href");
		a_atts.add("target");
		vAllowed.put("a", a_atts);
		ArrayList<String> img_atts = new ArrayList();
		img_atts.add("src");
		img_atts.add("width");
		img_atts.add("height");
		img_atts.add("alt");
		vAllowed.put("img", img_atts);
		ArrayList<String> no_atts = new ArrayList();
		vAllowed.put("b", no_atts);
		vAllowed.put("strong", no_atts);
		vAllowed.put("i", no_atts);
		vAllowed.put("em", no_atts);
		conf.put("vAllowed", vAllowed);
		conf.put("vSelfClosingTags", new String[]{"img"});
		conf.put("vNeedClosingTags", new String[]{"a", "b", "strong", "i", "em"});
		conf.put("vDisallowed", new String[0]);
		conf.put("vAllowedProtocols", new String[]{"http", "mailto", "https"});
		conf.put("vProtocolAtts", new String[]{"src", "href"});
		conf.put("vRemoveBlanks", new String[]{"a", "b", "strong", "i", "em"});
		conf.put("vAllowedEntities", new String[]{"amp", "gt", "lt", "quot"});
		conf.put("encodeQuotes", false);
		HTMLFilter htmlFilter = new HTMLFilter(conf);
		return htmlFilter.filter(content);
	}

	/**
	 * Escape编码
	 *
	 * @param text 被编码的文本
	 * @return 编码后的字符
	 */
	private static String encode(String text) {
		if (StrUtil.isEmpty(text)) {
			return StrUtil.EMPTY;
		}

		final StringBuilder tmp = new StringBuilder(text.length() * 6);
		char c;
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if (c < 256) {
				tmp.append("%");
				if (c < 16) {
					tmp.append("0");
				}
				tmp.append(Integer.toString(c, 16));
			} else {
				tmp.append("%u");
				if (c <= 0xfff) {
					// issue#I49JU8@Gitee
					tmp.append("0");
				}
				tmp.append(Integer.toString(c, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * Escape解码
	 *
	 * @param content 被转义的内容
	 * @return 解码后的字符串
	 */
	public static String decode(String content) {
		if (StrUtil.isEmpty(content)) {
			return content;
		}

		StringBuilder tmp = new StringBuilder(content.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < content.length()) {
			pos = content.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (content.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(content.substring(lastPos));
					lastPos = content.length();
				} else {
					tmp.append(content.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static void main(String[] args) {
		// String html = "<script>alert(1);</script>";
		String html = "{\"memberId\": \"2845432456461564\"}";

		String cleanHtml = EscapeUtil.clean(html);
		System.out.println("cleanHtml: " + cleanHtml);
		System.out.println("unescapeCleanHtml: " + EscapeUtil.unescape(cleanHtml));
		String escape = EscapeUtil.escape(cleanHtml);
		System.out.println("escapeCleanHtml: " + escape);
		System.out.println("unescapeEscapeCleanHtml: " + EscapeUtil.unescape(escape));

		// String escape = EscapeUtil.escape(html);
		// String html = "<scr<script>ipt>alert(\"XSS\")</scr<script>ipt>";
		// String html = "<123";
		// String html = "123>";
		// System.out.println("clean: " + EscapeUtil.clean(html));
		// System.out.println("escape: " + escape);
		// System.out.println("unescape: " + EscapeUtil.unescape(escape));
	}
}
