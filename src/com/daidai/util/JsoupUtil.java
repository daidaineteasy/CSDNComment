package com.daidai.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtil {
	
	//登录获取到的cookie
	static Map<String, String> cookieMap = new HashMap<String, String>();
	
	/**
	 * @param uri
	 * @param paramsMap	form表单中的参数和值
	 * @return
	 */
	public static boolean login(String uri, Map<String, String> paramsMap){
		
		boolean loginFlag = false;
		
		Response response = null;
		try {
			response = Jsoup.connect(uri)
					.userAgent("Mozilla")
			        .data(paramsMap)  
			        .method(Method.POST)
			        .timeout(20000)  
			        .execute();
			
			if (response.statusCode() == 200) {  
				cookieMap = response.cookies();
				loginFlag = true;
			} 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return loginFlag;
	}

	/**
	 * 抓取url网址页面链接上满足后边正则的url链接
	 */
	public static Set<String> getHrefList(String url, String regular){
		
		Set<String> urlSet = new HashSet<String>();
		
		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla").timeout(20000).get();
			
			Elements links = doc.getElementsByTag("a");
			
			String linkHref = "";
//			String linkText = "";
			
//			Pattern pattern = Pattern.compile("^http://blog\\.csdn\\.net/[^\\s]*/article/details/[0-9]+$");
			Pattern pattern = Pattern.compile(regular);
			Matcher matcher = null;
			
			for (Element link : links) {
				linkHref = link.attr("href");
//			  	linkText = link.text();
			  
				matcher = pattern.matcher(linkHref);
			  
				if(matcher.find()){
					urlSet.add(linkHref);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlSet;
	}
	
	/**
	 * 通过博客URL，获取form表单的url
	 * @param articleUrl
	 * @return
	 */
	public static String getFormUrl(String articleUrl){
		
		String formUrl = articleUrl.replace("article/details/", "comment/submit?id=");
		
		return formUrl;
	}
	
	/**
	 * 获取CSDN登录参数
	 * @return
	 */
	public static Map<String, String> getParamsMap(){
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		
		String url = "https://passport.csdn.net/account/login?from=http://my.csdn.net/my/mycsdn";
		Document doc = null;
		Element infoTable = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla").get();
			
			//获取除用户名密码外的表单参数
			infoTable = doc.getElementsByAttributeValue("name", "lt").first();
			paramsMap.put("lt", infoTable.attr("value"));
			
			infoTable = doc.getElementsByAttributeValue("name", "execution").first();
			paramsMap.put("execution", infoTable.attr("value"));
			
			infoTable = doc.getElementsByAttributeValue("name", "_eventId").first();
			paramsMap.put("_eventId", infoTable.attr("value"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return paramsMap;
	}
	
	/**
	 * 提交评论/留言内容
	 * @param url
	 * @param params
	 */
	public static void doPost(String uri, Map<String,String> paramsMap){
		
		System.out.println("URL链接:"+uri);
		
		uri = getFormUrl(uri);
		
		Response response = null;
		try {
			response = Jsoup.connect(uri)
					.userAgent("Mozilla/5.0")
			        .data(paramsMap)
			        .method(Method.POST)
			        .timeout(20000)
			        .cookies(cookieMap)
			        .ignoreContentType(true)
			        .execute();
			
			if (response.statusCode() == 200) {  
				System.out.println("评论成功！");
			} else{
				System.out.println("评论失败！（HTTP CODE："+response.statusCode()+"）");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		System.out.println();
	}
	
	public static void main(String[] args) {
//		Set<String> urlSet = getHrefList("http://blog.csdn.net/");
//		for(String url : urlSet){
//			System.out.println(url);
//		}
		
//		System.out.println(getFormUrl("http://blog.csdn.net/nomasp/article/details/49801779"));
	}
}
