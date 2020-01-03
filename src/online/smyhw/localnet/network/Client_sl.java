package online.smyhw.localnet.network;

import java.net.Socket;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.LNlib;
import online.smyhw.localnet.message;
import online.smyhw.localnet.command.cmdManager;
import online.smyhw.localnet.event.Client_DISconnect_Event;
import online.smyhw.localnet.event.Client_connect_Event;
import online.smyhw.localnet.lib.TCP_LK;

public class Client_sl extends TCP_LK
{
	
//	Boolean ISln = false;
	
	public String ID;
	public Client_sl(Socket s)
	{
		super(s,1);//这里，调用父类构造方法
		try
		{
			Thread.sleep(1000);
			this.Smsg("&"+LN.ID);//发送自身ID

		}catch(Exception e){message.info(" 客户端\""+ID+"\"连接异常！丢弃线程"+e.getMessage());e.printStackTrace();return;}

	}
	
	public void CLmsg(String msg)
	{
		if(ID==null && !(msg.startsWith("&")))
		{this.Smsg("!1客户端，请先报告你的ID!");return;}
		LN.mdata(this, msg);
	}
	public void Serr_u()
	{		
		StackTraceElement[] temp=Thread.currentThread().getStackTrace();
		StackTraceElement temp2=(StackTraceElement)temp[3];
		NetWorkManager.doclient(0, this, 0);
		message.warning("一个连接出错！丢弃！，位置："+temp2.getFileName()+":"+temp2.getClassName()+":"+temp2.getMethodName()+":"+temp2.getLineNumber());
		new Client_DISconnect_Event(this);
		return;
	}
	
}