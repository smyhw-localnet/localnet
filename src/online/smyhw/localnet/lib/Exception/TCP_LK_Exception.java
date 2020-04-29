package online.smyhw.localnet.lib.Exception;

import online.smyhw.localnet.lib.TCP_LK;

public class TCP_LK_Exception extends Exception 
{
	/**
	 * type=1	 未知客户端类型
	 * type=2	无法获取输入输出流
	 * type=3	数据发送出错
	 * type=4	接受客户端数据超时（过久没有返回心跳包？）
	 * type=5	收到的数据包无法解码（utf-8解编码错误）
	 * type=6	未知异常
	 * type=7	延时错误（sleep函数抛出的异常：线程在休眠中被其他线程终止）
	 * type=8	加解密错误
	 */
	public int type = 6;
	public Exception upEXP;
	public TCP_LK tcp_lk;
/**	这些构造方法是被禁用的
	public TCP_LK_Exception() {}

	public TCP_LK_Exception(String message) 
	{
		super(message);
	}
*/
	public TCP_LK_Exception(String message,TCP_LK tcp_lk) 
	{
		super(message);
		this.tcp_lk=tcp_lk;
		tcp_lk.Serr_u(this);
		tcp_lk.isERROR=true;
		
	}
	
	public TCP_LK_Exception(String message,TCP_LK tcp_lk,int type,Exception upEXP) 
	{
		super(message+"{"+"type="+type+";"+"}");
		
		this.tcp_lk=tcp_lk;
		this.upEXP=upEXP;
		this.type=type;
		tcp_lk.Serr_u(this);
		tcp_lk.isERROR=true;
	}
	
	public TCP_LK_Exception(String message,TCP_LK tcp_lk,int type) 
	{
		super(message+"{"+"type="+type+";"+"}");
		
		this.tcp_lk=tcp_lk;
		this.type=type;
		tcp_lk.Serr_u(this);
		tcp_lk.isERROR=true;
	}
	
}
