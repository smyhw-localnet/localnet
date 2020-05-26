package online.smyhw.localnet.lib;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import online.smyhw.localnet.message;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;

/**
 * 
 * 
 * 
 * @author smyhw
 * localnet协议类
 * powered by smyhw
 * 
 * 
 * type为1时作为服务端</br>
 * type为2时作为客户端</br>
 * type为其他任何值时都会输出错误信息！</br>
 * </br>
 * 成员变量kt值是多少毫秒发送一次心跳包</br>
 * 这个值仅在做为服务端时生效，客户端仅在收到服务端心跳时返回确认心跳</br>
 * </br>
 * 注意，您应该重写CLmsg来添加你自己的代码</br>
 * CLmsg将在收到消息时被调用</br>
 * </br>
 * 如果你需要发送消息，你应该调用Smsg方法</br>
 * </br>
 * 您或许还需重写Serr_u方法来做在连接出错/被关闭/断开后的处理</br>
 * 注意，时Serr_u，别重写Serr。。。那个方法里包含释放出错连接相关资源的代码！</br>
 * emmm，上面那行不用看了，Serr我写成final了XD</br>
 * 
 */
public abstract class TCP_LK
{
	public int kt=10000;
	public int type=0;
	public Socket s;
	public InputStream s_in;
	public OutputStream s_out;
	public LK_Receive lkr;
	public xt_sender xts;
	public String XT="{\"type\":\"connect\",\"operation\":\"xt\"}";
	public boolean isERROR = false;//如果该值为true,则表示连接已经中断
	
	
	public TCP_LK(Socket s,int type)
	{
		gz(s,type);
	}
	public TCP_LK(Socket s,int type,String XT)
	{
		this.XT=XT;
		gz(s,type);
	}
	public void gz(Socket s,int type)
	{
		this.type=type;
		this.s =s;
		try
		{
			s_in = s.getInputStream();
			s_out = s.getOutputStream();
			s.setSoTimeout(kt*3);
		}
		catch(Exception e) 
		{
			new TCP_LK_Exception("[TCP_LK]:无法从传入的socket中获取输入输出流",this,2,e);
		}
		if(type !=1 && type!=2)
		{
			new TCP_LK_Exception("[TCP_LK]:传入的终端类型是未知的（1代表服务端，2代表客户端，而当前传入的参数是"+type+"）",this,1,null);
		}
		if(type==1)
		{
			lkr = new LK_Receive(this,1,this.XT);
			xts = new xt_sender(kt,this,this.XT);
		}
		if(type==2)
		{
			lkr = new LK_Receive(this,2,this.XT);
		}
	}
	

	
	/**
	 * @author smyhw
	 * 
	 * @see
	 * 
	 * 当收到有效消息时，将调用这个方法
	 * 所以，请实现这个方法以添加你自己的代码
	 * 注意，这个方法是异步调用的！将不会阻塞主进程！
	 * 
	 * @param msg 收到的信息
	 */
	public abstract void CLmsg(String msg);
	
	/**
	 * @author smyhw
	 * 
	 * 你可以重写这个方法来使用你自己的加密</br>
	 * 默认是没有任何加密的!</br>
	 * 
	 * @param input 需要加密的数据
	 * @param type 该值为0时解密,为1时加密
	 */
	public synchronized byte[] encryption(byte[] input,int type)
	{
		return input;
	}
	
	
	
	public void Smsg(String msg)
	{
		if(this.isERROR) {return;}
		try
		{
			byte[] b_msg = msg.getBytes("UTF-8");
			b_msg = encryption(b_msg,1);//加密
			if(b_msg==null) {message.info("加密操作出错");return;}
			byte[] temp3 = (b_msg.length+"|").getBytes("UTF-8"); 
			byte[] temp4 = new byte[temp3.length+b_msg.length];
			System.arraycopy(temp3, 0, temp4, 0, temp3.length);
			System.arraycopy(b_msg, 0, temp4, temp3.length, b_msg.length);
//			message.info("发送数据:"+new String(temp4,"UTF-8").trim());
			SendMessage(temp4);
		}
		catch(TCP_LK_Exception e)
		{
			//什么都不用做，因为这个异常在抛出后，这个连接就报废了。。。
		}
		catch(UnsupportedEncodingException ee)
		{
			TCP_LK_Exception TLE = new TCP_LK_Exception("[TCP_LK]:对将要发送的消息时编码出错！",this);
			TLE.type=6;
			TLE.upEXP=ee;
		}
	}
	
	/**
	 * 您应该调用Smsg方法发送符合localnet协议的消息，而不是该方法</br>
	 * 该方法 不 会将消息封装为localnet协议报文 
	 * @throws TCP_LK_Exception 
	 */
	public void SendMessage(byte[] input) throws TCP_LK_Exception
	{
		try 
		{
			s_out.write(input);
		}
		catch (Exception e) 
		{
			TCP_LK_Exception TLE = new TCP_LK_Exception("[TCP_LK]:发送消息出错！",this);
			TLE.upEXP=e;
			TLE.type=3;
			throw TLE;
		}
	}
	
	

	
	public synchronized String Rmsg() throws TCP_LK_Exception
	{
		try 
		{
			//读取标识位(报文长度)
			int len=0;
			String temp2="";
			while(true)
			{
				if(s_in.available()<1) {Thread.sleep(1000);continue;}
				byte[] temp1 = new byte[1];
				s_in.read(temp1);
				String temp3;
				try {temp3 = new String(temp1,"UTF-8").trim();} 
				catch (UnsupportedEncodingException e) 
				{
					throw new TCP_LK_Exception("[TCP_LK]:客户端<"+s.getInetAddress()+">发回的数据解码错误",this,5,e);
				}
				if(!temp3.equals("|")) 
				{
					temp2=temp2 + temp3;
					continue;
				}
				else
				{
					len = Integer.parseInt(temp2);
					break;
				}
				
			}
			
			String Sdata;
			//获取报文主体
			while(true)
			{
				if(s_in.available()<len) {Thread.sleep(1000);continue;}
				byte[] temp1 = new byte[len];
				s_in.read(temp1);
				temp1 = encryption(temp1,0);
				if(temp1==null) 
				{
					message.info("解密操作出错");
					throw new TCP_LK_Exception("[TCP_LK]:客户端<"+s.getInetAddress()+">发回的数据解密错误！",this,8);
				}
				Sdata = new String(temp1,"UTF-8").trim();
				break;
			}
			
			return Sdata;
			
		}
		catch(java.net.SocketTimeoutException e)
		{
			TCP_LK_Exception TLE = new TCP_LK_Exception("[TCP_LK]:客户端<"+s.getInetAddress()+">过久没有发回心跳包",this);
			TLE.type=4;
			throw TLE;
		}
		catch(java.io.IOException ee)
		{
			TCP_LK_Exception TLE = new TCP_LK_Exception("[TCP_LK]:客户端<"+s.getInetAddress()+">发生IO异常",this);
			TLE.upEXP=ee;
			TLE.type=6;
			throw TLE;
		}
		catch(InterruptedException eee)
		{
			TCP_LK_Exception TLE = new TCP_LK_Exception("[TCP_LK]:客户端<"+s.getInetAddress()+">线程发生延迟错误（这个线程被非法终止）",this);
			TLE.upEXP=eee;
			TLE.type=7;
			throw TLE;
		}
	}
	
	/**
	 * 实现这个方法来在该连接出错时对你的程序进行相应的处理</br>
	 * 注意，这个方法将在本类进行自处理之前执行，请勿在此执行耗时的操作</br>
	 * 如果此方法耗时，则可能导致继续发送心跳等操作导致二次报错
	 * @param e 发送的异常（如果有）
	 */
	public abstract void Serr_u(TCP_LK_Exception e);
}

class LK_Receive extends Thread
{
	TCP_LK ba;
	int type;
	String XT;
	LK_Receive(TCP_LK ba,int type,String XT)
	{
		this.XT=XT;
		this.ba =ba;
		this.type=type;
		this.start();
	}
	
	public void run()
	{
		try 
		{
			while(true)
			{
				if(ba.isERROR) {return;}
				String Sdata = ba.Rmsg();
//				message.info("TCP_LK接到原始消息:"+Sdata);
				if(Sdata.equals(XT)) 
				{	
					if(type==1){continue;}
					if(type==2) {ba.Smsg(this.XT);continue;}
				}
				ba.CLmsg(Sdata);
			}
		} 
		catch (TCP_LK_Exception e) 
		{
			//啥也不用做，在这个异常抛出时，这个连接就已经废止了...
		}
		
	}
}

class xt_sender extends Thread
{
	String XT;
	int Stime;
	TCP_LK ba;
	xt_sender(int Stime,TCP_LK ba,String XT)
	{
		this.XT = XT;
		this.Stime=Stime;
		this.ba=ba;
		this.start();
	}
	public void run()
	{
		while(true)
		{
			ba.Smsg(this.XT);
			try {sleep(Stime);} catch (InterruptedException e) {new TCP_LK_Exception("[TCP_LK]:心跳包延迟出错",ba);}
		}
	}

}
