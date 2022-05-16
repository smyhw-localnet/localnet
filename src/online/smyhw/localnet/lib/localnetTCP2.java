package online.smyhw.localnet.lib;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import online.smyhw.localnet.lib.Exception.Json_Parse_Exception;

/**
 * 
 *  通讯协议，使用TCP<br>
 *  
 *  严格的数据包形式收发，非流式传输<br>
 *  
 *  1. 自动TCP保活<br>
 *  2. 50毫秒级的中断检测<br>
 *  3. 可以传送任意字符串形式的载荷<br>
 *  4. 回调式错误响应/接收响应<br>
 *  5. 无状态设计<br>
 *  
 *  @author smyhw
 *
 */
public class localnetTCP2 {
	//
	recv_thread recv_th;//接收线程
	heartbeat_thread hb_th;//心跳(仅发送)线程
	Method error_method;//错误回调方法
	Object on_error_obj;
	Method on_recv; //消息接收回调方法
	Object on_recv_obj;
	Socket socket;
	
	
	/**
	 * 接管指定socket并以localnetTCP协议进行通讯
	 * @param s 给定的socket
	 */
	public localnetTCP2(Socket s,Object on_recv_obj,Method on_recv,Object on_error_obj,Method on_error){
//		try {s.setSoTimeout(150);} catch (SocketException e) {e.printStackTrace();}
		this.on_error_obj = on_error_obj;
		this.error_method = on_error;
		this.socket = s;
		this.recv_th = new recv_thread(this);
		this.hb_th = new heartbeat_thread(this);
		this.on_recv_obj = on_recv_obj;
		this.on_recv = on_recv;
	}
	
	/**
	 * 
	 * 根据给定的ip和端口建立连接
	 * @param ip 需要连接的ip
	 * @param port 需要连接的端口
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public localnetTCP2(String ip,int port,Object on_recv_obj,Method on_recv,Object on_error_obj,Method on_error) throws UnknownHostException, IOException {
		this(new Socket(ip,port),on_recv_obj,on_recv,on_error_obj,on_error);
	}
	
	/**
	 * 发送原始字符串
	 * 注意:根据协议，这必须是可解析的json字符串
	 * @param data
	 */
	void send_raw_string(String data) {
		try {
			byte[] b_msg;
			b_msg = data.getBytes("UTF-8");
			byte[] temp3 = (b_msg.length+"|").getBytes("UTF-8"); 
			byte[] temp4 = new byte[temp3.length+b_msg.length];
			System.arraycopy(temp3, 0, temp4, 0, temp3.length);
			System.arraycopy(b_msg, 0, temp4, temp3.length, b_msg.length);
			this.socket.getOutputStream().write(temp4);
		}catch(UnsupportedEncodingException ee) {
			this.on_error("发送数据时编码异常", ee);
		} catch (IOException e) {
			this.on_error("发送数据时IO异常", e);
		}
	}
	
	/**
	 * 发送信息
	 * @param msg
	 */
	public void send_msg(String msg) {
		this.send_raw_string("{"
				+ "\"version\":\"v1.0.0\","
				+ "\"type\":\"content\","
				+ "\"content\":\""+simple_json.Encoded(msg)+"\""
				+ "}");
	}
	
	
	/**
	 * 错误处理
	 * @param err_msg 错误信息
	 */
	@SuppressWarnings("deprecation")
	void on_error(String err_msg,Exception e) {
			//this.socket.close();
			try {
				this.error_method.invoke(this.on_error_obj, err_msg,e);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			//终止线程
			//在调用上游方法之后调用，防止先把自己干了
			this.hb_th.stop();
			this.recv_th.stop();
	}
}

/**
 * 心跳线程
 * @author smyhw
 *
 */
class heartbeat_thread extends Thread{
	//心跳频率
	final int frequency = 50;
	localnetTCP2 connection;
	public heartbeat_thread(localnetTCP2 connection) {
		this.connection = connection;
		this.start();
	}
	public void run() {
			while(true)
			{
				this.connection.send_raw_string("{"
						+ "\"version\":\"v1.0.0\","
						+ "\"type\":\"heartbeat\""
						+ "}");
				try {sleep(frequency);} catch (InterruptedException e) {this.connection.on_error("心跳线程异常", e);}
			}
	}
}

/**
 * 轮询接收消息线程
 * @author smyhw
 *
 */
class recv_thread extends Thread{
	//轮询频率(毫秒)
	final int frequency = 50;
	//
	localnetTCP2 connection;
	
	public recv_thread(localnetTCP2 connection) {
		this.connection = connection;
		this.start();
	}
	
	public void run() {
		while(true) {
			mdata(recv_once());
			
		}
	}
	
	/**
	 * 有效载荷的处理流程
	 * @param re 有效载荷数据
	 */
	void mdata(HashMap<String,String> re) {
		if(!(re.containsKey("version") && re.containsKey("type"))) {this.connection.on_error("载荷字段异常<"+re+">", null);return;}
		if(!(re.get("version").equals("v1.0.0"))) {this.connection.on_error("协议版本不符,对方报告版本<"+re.get("version")+">", null);return;}
		switch(re.get("type")) {
		case"content":
			if(!re.containsKey("content")) {this.connection.on_error("content数据包不存在content字段", null);return;}
			try {this.connection.on_recv.invoke(this.connection.on_recv_obj, re.get("content"));} 
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e){e.printStackTrace();}
			return;
		case "heartbeat":
			//TODO 检测到心跳ID时返回指定心跳包
			return;
		case "re_heartbeat":
			//TODO 特殊心跳包处理
			return;
		default:
			this.connection.on_error("未知载荷类型",null);
			return;
		}
	}
	
	/**
	 * 进行一次完整的数据包接收
	 * @return 返回HashMap形式的有效载荷
	 */
	HashMap<String,String> recv_once(){
		try {
			InputStream s_in = this.connection.socket.getInputStream();
			while (true) {
				//读取标识位(报文长度)
				int len = 0;
				String temp2 = "";
				while (true) {
					if (s_in.available() < 1) {//如果没有数据，就等待
						Thread.sleep(frequency);
						continue;
					}
					byte[] temp1 = new byte[1];
					s_in.read(temp1);
					String temp3 = null;
					try {temp3 = new String(temp1, "UTF-8").trim();} catch (UnsupportedEncodingException e) {this.connection.on_error("包头解码错误", e);}
					if (!temp3.equals("|")) {//如果没有读取到标志符，则继续读取
						temp2 = temp2 + temp3;
						continue;
					} else {
						try{len = Integer.parseInt(temp2);}catch(NumberFormatException e) {this.connection.on_error("包长度解析错误", e);}
						break;
					}

				}
				String Sdata;
				//获取报文主体
				while (true) 
				{
					if (s_in.available() < len) {
						Thread.sleep(1000);
						continue;
					}
					else {break;}
				}
				byte[] temp1 = new byte[len];//和载荷长度一致的数组
				s_in.read(temp1);//读取载荷
				Sdata = new String(temp1, "UTF-8").trim();//将载荷从字节编码为字符串
				HashMap<String,String> re = simple_json.Parse(Sdata);//解析载荷
				if(re!=null) {return re;}
				else {this.connection.on_error("载荷解析异常<"+Sdata+">",null);}
			}
			
		}
		catch(java.net.SocketTimeoutException e)
		{
			connection.on_error("心跳超时",e);
		}
		catch(java.io.IOException ee)
		{
			connection.on_error("IO异常",ee);
		}
		catch(InterruptedException eee)
		{
			connection.on_error("接收线程被终止",eee);
		}
		return null;
	}
		
}

/**
 * 内置简易Json处理器
 * 
 * @author smyhw
 *
 */
class simple_json {
	/**
	 * 解析JSON字符串
	 * 
	 * @param input JSON字符串
	 * @throws Json_Parse_Exception 当传入的Json信息无法解析时
	 */
	public static HashMap<String, String> Parse(String input) {
		HashMap<String, String> re = new HashMap<String, String>();
		// if(!input.startsWith("{")) {return null;};
		// input = input.substring(1);
		// input = input.substring(0, input.length()-1);
		char[] str = input.toCharArray();
		String key = "", value = "";
		int type = 0;// type==0#键;type==1#值
		int stru = 0;// stru==0#构造字符;stru==1#数据字符
		for (int i = 0; i < str.length; i++) {
			if (i > 0 && str[i] == '"' && str[i - 1] != '\\')// 加前置条件i>0是为了防止检测第0个字符的前一位(i-1)导致异常
			{// 如果检测到有效的双引号，则切换stru
				if (stru == 1) {
					stru = 0;
				} else {
					stru = 1;
				}
				continue;
			}
			if (stru == 0) {// 如果读取的是构造字符...
				// 需要考虑逗号问题
				if (str[i] == '{') {
					continue;
				}
				if (str[i] == '}') {// 处于构造字符的大括号代表字符串结束
					// 注意,这里别忘了保存最后一个键值对
					type = 0;
					key = Decoded(key);
					value = Decoded(value);
					re.put(key, value);
					key = "";
					value = "";
					return re;
				}
				if (str[i] == ':') {
					type = 1;
					continue;
				} // 表示接下来读取的是值
				if (str[i] == ',') {// 表示一个键值对已经完成，提交到HashMap
					type = 0;
					key = Decoded(key);
					value = Decoded(value);
					re.put(key, value);
					key = "";
					value = "";
					continue;
				}

				// 能处理到这，说明这个构造字符是tm非法的,直接返回null,表示错误数据
				return null;
			} else {// 如果读取的是数据
				if (type == 0) {// 如果读取的是键
					key = key + str[i];
					continue;
				} else {// 如果读取的是值
					value = value + str[i];
					continue;
				}
			}
		}
		key = Decoded(key);
		value = Decoded(value);
		re.put(key, value);
		return re;
	}

	/**
	 * 根据HashMap构造JSON字符串
	 * 
	 * @param input
	 * @return
	 */
	public static String Create(HashMap<String, String> input) {
		String re = "{";
		Iterator<Entry<String, String>> temp1 = input.entrySet().iterator();
		while (temp1.hasNext()) {
			Entry<String, String> temp2 = temp1.next();
			String key = temp2.getKey();
			String value = temp2.getValue();
			key = Encoded(key);
			value = Encoded(value);
			re = re + "\"" + key + "\":\"" + value + "\",";
		}
		re = re.substring(0, re.length() - 1);
		re = re + "}";
		return re;
	}

	/**
	 * 用于转义特殊字符</br>
	 * <\>(反斜杠)</br>
	 * <">(双引号)</br>
	 * 都会被转义</br>
	 * 
	 * @param input 未转义的字符串
	 * @return 转义后的字符串
	 */
	public static String Encoded(String input) {
		// message.info("en++"+input);
		char[] str = input.toCharArray();
		ArrayList<Character> out_str = new ArrayList<Character>();
		ArrayList<Character> key_word = new ArrayList<Character>();
		key_word.add('\\');
		key_word.add('"');
		for (int i = 0; i < str.length; i++) {
			if (key_word.contains(str[i])) {
				out_str.add('\\');
			}
			out_str.add(str[i]);
		}
		String re = "";
		for (int i = 0; i < out_str.size(); i++) {
			re = re.concat(out_str.get(i) + "");
		}
		// message.info("en--"+re);
		return re;
	}

	/**
	 * 反转义特殊字符
	 * 
	 * @param input
	 * @return
	 * @see public static String Encoded(String input)
	 */
	public static String Decoded(String input) {
		char[] str = input.toCharArray();
		ArrayList<Character> out_str = new ArrayList<Character>();
		ArrayList<Character> key_word = new ArrayList<Character>();
		key_word.add('\\');
		key_word.add('"');
		for (int i = 0; i < str.length; i++) {
			if (str[i] == '\\' && key_word.contains(str[i + 1])) {
				i = i + 1;
			}
			out_str.add(str[i]);
		}
		String re = "";
		for (int i = 0; i < out_str.size(); i++) {
			re = re.concat(out_str.get(i) + "");
		}
		return re;
	}
}

