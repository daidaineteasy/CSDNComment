package com.content;

import java.util.Set;

import com.daidai.util.JsoupUtil;

/**
 * 加载Url地址信息的类
 * @author daidai
 *
 */
public class UrlContent {

	static Set<String> urlSet;
	
	static int urlCount = 0;

	/**
	 * 抓取URL链接地址
	 */
	public static void initUrlList(){
		urlSet = JsoupUtil.getHrefList("http://blog.csdn.net/", "^http://blog\\.csdn\\.net/[^\\s]*/article/details/[0-9]+$");
		if(null != urlSet){
			urlCount = urlSet.size();
		}
	}

	public static Set<String> getUrlSet() {
		return urlSet;
	}

	public static int getUrlCount() {
		return urlCount;
	}
	
}
