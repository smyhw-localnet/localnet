package online.smyhw.localnet.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import online.smyhw.localnet.message;

//2019.9.23
//调用webAPI用的
public class WebAPI// extends HttpURLConnection
{
	 //@return 返回调用的结果
	//如果发生错误则返回error
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
                return re;
			}
			else
			{
				message.error("[lib][WebAPI.get]{http错误！（这不是您的问题，也不是程序的问题，是对方服务器发回了http错误码！）}");return "http_error";
			}
		} catch (IOException e) {message.error("[lib][WebAPI.get]{获取连接后信息出错！}");return "error";}
//		return "error";
	}
}
