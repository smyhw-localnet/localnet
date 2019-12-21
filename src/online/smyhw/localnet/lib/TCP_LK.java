package online.smyhw.localnet.lib;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import online.smyhw.localnet.*;

/**
 * 
 * 
 * 
 * @author smyhw
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
public class TCP_LK
{
	public int kt=10000;
	public int type=0;
	public Socket s;
	public InputStream s_in;
	public OutputStream s_out;
	public LK_Receive lkr;
	public xt_sender xts;
	
	
	public TCP_LK(Socket s,int type)
	{
		gz(s,type);
	}
	public void gz(Socket s,int type)
	{
		this.type=type;
		this.s =s;
		try
		{
//			in = new DataInputStream(s.getInputStream());
//			out = new DataOutputStream(s.getOutputStream());
			s_in = s.getInputStream();
			s_out = s.getOutputStream();
			s.setSoTimeout(kt*3);
		}
		catch(Exception e) 
		{
			message.warning("TCP_LK获取数据流时出错，请检查你传入的socket!,信息如下:\n"+e.getMessage());
			e.printStackTrace();
			Serr();
			return;
		}
		if(type !=1 && type!=2)
		{
			message.warning("TCP_LK的连接类型是未知的:"+type);
			Serr();
			return;
		}
		if(type==1)
		{
			lkr = new LK_Receive(s_in,this,1);
			xts = new xt_sender(s_out,kt,this);
		}
		if(type==2)
		{
			lkr = new LK_Receive(s_in,this,2);
		}
	}
	
	
	/**
	 * @author smyhw
	 * 
	 * @see
	 * 
	 * 当收到有效消息时，将调用这个方法
	 * 所以，请重写这个方法以添加你自己的代码
	 * 注意，这个方法是异步的！将不会阻塞主进程！
	 * 
	 * @param msg 收到的信息
	 */
	public void CLmsg(String msg)
	{
		
		
	}
	
	public void Smsg(String msg)
	{
		try 
		{
			s_out.write(msg.getBytes("UTF-8"));
		}
		catch (Exception e) 
		{
			e.printStackTrace();message.warning("TCP_LK发送消息出错！");
			Serr();
			return;
		}
	}
	
	public String Rmsg()
	{
		try 
		{
			String Sdata;
			byte[] data = new byte[1024];
			s_in.read(data);
			Sdata=new String(data,"UTF-8").trim();
			return Sdata;
		}
		catch(Exception e)
		{
			e.printStackTrace();message.warning("TCP_LK接收消息出错！");Serr();return "error";
		}
	}
	
	public final void Serr()
	{
		try 
		{
			lkr.stop();
			if(type==1)
			{xts.stop();}
			s.close();
		} catch (Exception e) {}
		Serr_u();
	}
	public void Serr_u(){}
}

class LK_Receive extends Thread
{
	InputStream s_in;
	TCP_LK ba;
	int type;
	LK_Receive(InputStream s_in,TCP_LK ba,int type)
	{
		this.s_in=s_in;
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
				String Sdata = ba.Rmsg();
				if(Sdata.startsWith("#xt")) 
				{	
					if(type==1){continue;}
					if(type==2) {ba.Smsg("#xt");continue;}
				}
				ba.CLmsg(Sdata);
			}
		} 
		catch (Exception e) 
		{
			message.warning("TCP_LK的一个信息接收时出错，信息如下:\n"+e.getMessage());
			e.printStackTrace();
			ba.Serr();
			return;
		}
		
	}
}

class xt_sender extends Thread
{
	OutputStream s_out;
	int Stime;
	TCP_LK ba;
	xt_sender(OutputStream s_out,int Stime,TCP_LK ba)
	{
		this.s_out = s_out;
		this.Stime=Stime;
		this.ba=ba;
		this.start();
	}
	public void run()
	{
		try
		{
			while(true)
			{
				ba.Smsg("#xt");
				sleep(Stime);
			}
			
		}
		catch(Exception e)
		{
			message.warning("TCP_LK的一个连接发送心跳时出错，信息如下:\n"+e.getMessage());
			e.printStackTrace();
			ba.Serr();
			return;
		}
	}

}
