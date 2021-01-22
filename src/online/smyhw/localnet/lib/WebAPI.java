package online.smyhw.localnet.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import online.smyhw.localnet.message;

//2019.9.23
//调用webAPI用的
/**
 * 
 * @apiNote java的HTTPAPI很丰富，这里只提供简易方法
 * @author smyhw
 * 旨在提供简单的请求方法</br>
 * 不会提供完整的http操控方法</br>
 * 因为java原生的方法，太tm多了</br>
 * 还不能直接定义http头...</br>
 */
public class WebAPI// extends HttpURLConnection
{
	
	/**
	 * 
	 * 
	 * @param method 请求方法(get或post)
	 * @param url 请求的url
	 * @param proxy 代理，没有请传null
	 * @param content 若为post请求，这里设置载荷，否则,传任何值都可以
	 * @return 接口的返回值
	 * @throws 所有异常将抛出
	 */
	@Deprecated
	public static URLConnection doApi(String method,String url,Proxy proxy,String content) throws Exception
	{
		URL tmp1 = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) tmp1.openConnection(proxy);
		connection.setRequestMethod(method);
		connection.connect();
		return connection;
	}
	 //@return 返回调用的结果
	//如果发生错误则返回error
	@Deprecated
	public static String get(String URL_input)
	{
		URL url;
		HttpURLConnection connection;
		String re;
		try {url = new URL(URL_input);} catch (Exception e) {message.error("[lib][WebAPI.get]{创建URL出错！}");;return "error";}
		try {connection = (HttpURLConnection) url.openConnection();} catch (IOException e) {message.error("[lib][WebAPI.get]{打开连接出错！}");;return "error";}
		try {connection.setRequestMethod("GET");} catch (ProtocolException e) {message.error("[lib][WebAPI.get]{设置连接类型出错}");;return "error";}
		try {connection.connect();} catch (IOException e) {message.error("[lib][WebAPI.get]{执行连接出错}");return "error";}
		try {
			if (connection.getResponseCode() == 200) //200为正常返回
			{
				InputStream is = connection.getInputStream();
                // 封装输入流is，并指定字符集
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
//                    sbf.append("\r\n");
                }
                re = sbf.toString();
                br.close();
                return re;
			}
			else
			{
				message.error("[lib][WebAPI.get]{http错误！（这不是您的问题，也不是程序的问题，是对方服务器发回了http错误码！）}");return "http_error";
			}
		} catch (IOException e) {message.error("[lib][WebAPI.get]{获取连接后信息出错！}");return "error";}
//		return "error";
	}


	/**
	 * 注意，这个方法仅返回载荷，而不论返回头如何</br>
	 * 只会返回String类型，载荷类型不会就直接跑异常</br>
	 * @param url 请求的url
	 * @return 返回的http载荷
	 * @throws 所有异常都将抛出
	 */
	public static String simpleGet(String url) throws Exception
	{
		URL url_ = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) url_.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		String re = getContent(connection); 
       return re;
	}
	
	/**
	 * 简单POST请求</br>
	 * 仅返回载荷，不论状态码</br>
	 *  载荷仅支持String，否则抛异常</br>
	 * 没有载荷或发生其他异常，都会直接抛出</br>
	 * @param url 需要请求的地址
	 * @param data 需要发送的POST载荷
	 * @return 返回的载荷
	 * @throws Exception 任何异常都会抛给上层
	 */
	public static String simplePost(String url,String data) throws Exception
	{
		HttpURLConnection connection = (HttpURLConnection) doApi("POST", url, null, data);
		String re = getContent(connection); 
		return re;
	}
	
	/**
	 * 
	 * 根据给定的http连接提取载荷</br>
	 * 不会判断状态码,载荷类型等信息</br>
	 * 只会返回String类型，载荷类型不会就直接跑异常</br>
	 * @param connection 给定的HTTP连接
	 * @return 返回载荷
	 * @throws Exception 任何异常都将抛给上级
	 */
	public static String getContent(HttpURLConnection connection) throws Exception
	{
		InputStream is = connection.getInputStream();
	       // 封装输入流is，并指定字符集
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	       // 存放数据
	       StringBuffer sbf = new StringBuffer();
	       String temp = null;
	       while ((temp = br.readLine()) != null) 
	       {
	           sbf.append(temp);
	       }
	       String re = sbf.toString();
	       br.close();
	       return re;
	}
}
