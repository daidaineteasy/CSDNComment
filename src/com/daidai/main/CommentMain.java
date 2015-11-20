package com.daidai.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.content.UrlContent;
import com.daidai.util.JsoupUtil;

public class CommentMain {

	//需要发表多少评论
	static int num = 0;
	//评论内容
	static String content;
	//CSDN网站用户名
	static String userName = "";
	//CSDN网站登录密码
	static String password = "";
	
	public static void main(String[] args) {
		
		//步骤1：先进行登录
		login();
		
		//步骤2：获取博客URL
		getUrl();
		
		//步骤3：开始进行评论
		startComment();
		
	}
	
	/**
	 * 登录操作
	 */
	@SuppressWarnings("resource")
	public static void login(){
		
		java.util.Scanner inputScanner = null;
		
		System.out.println("请输入您的CSDN网站的用户名：");
		
		while(true){
			inputScanner = new java.util.Scanner(System.in);
			if(inputScanner.hasNext()){
				userName = inputScanner.next();
			}
			if(null == userName || "".equals(userName.trim())){
				System.out.println("用户名不能为空，请重新输入！");
				inputScanner = null;
				continue ;
			}
			break;
		}
		
		System.out.println("请输入您的CSDN网站的密码：");
		
		
		while(true){
			inputScanner = new java.util.Scanner(System.in);
			if(inputScanner.hasNext()){
				password = inputScanner.next();
			}
			if(null == password || "".equals(password.trim())){
				System.out.println("密码不能为空，请重新输入！");
				inputScanner = null;
				continue ;
			}
			break;
		}
		
		System.out.println("正在进行登录......");
		
		Map<String, String> paramsMap = JsoupUtil.getParamsMap();
		paramsMap.put("username", userName);
		paramsMap.put("password", password);
		
		boolean flag = JsoupUtil.login("http://passport.csdn.net/account/login", paramsMap);
		
		if(flag){
			System.out.println("登录成功！");
		}else{
			System.out.println("登录失败请重新进行登录！");
			login();
		}
		
	}
	
	/**
	 * 初始化博客列表
	 */
	public static void getUrl(){
		System.out.println("正在获取最新博客url......");
		UrlContent.initUrlList();
		System.out.println("总过获取最新url地址数为："+UrlContent.getUrlCount());
	}
	
	/**
	 * 开始进行评论留言
	 */
	@SuppressWarnings("resource")
	public static void startComment(){
		java.util.Scanner inputScanner = null;
		String inputStr = null;
		
		System.out.println("您需要给多少篇博客进行评论（输入需为正整数）：");
		
		while(true){
			inputScanner = new java.util.Scanner(System.in);
			if(inputScanner.hasNext()){
				inputStr = inputScanner.next();
			}
			if(null == inputStr || "".equals(inputStr.trim())){
				System.out.println("输入内容不能为空，请重新输入！");
				inputScanner = null;
				continue ;
			}else if(!inputStr.matches("[0-9]+")){
				System.out.println("输入内容必须为正整数，请重新输入！");
				inputScanner = null;
				continue ;
			}else{
				num = Integer.valueOf(inputStr);
				inputScanner = null;
				break ;
			}
		}
		
		System.out.println("请输入评论内容，内容不能为空：");
		
		while(true){
			inputScanner = new java.util.Scanner(System.in);
			if(inputScanner.hasNext()){
				content = inputScanner.next();
			}
			if(null == content || "".equals(content.trim())){
				System.out.println("评论内容不能为空，请重新输入！");
				inputScanner = null;
				continue ;
			}
			break;
		}
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("commentid", "");
		paramsMap.put("content", content);
		paramsMap.put("replyId", "");
		paramsMap.put("comment_userId", "521203");
		paramsMap.put("userId", "521203");
		
		List<String> list = new ArrayList<String>(UrlContent.getUrlSet());
		
		//开始进行评论留言
		for(int i=0; i< UrlContent.getUrlCount() && i<num; i++){
			JsoupUtil.doPost(list.get(i), paramsMap);
		}
		
	}
	
}
