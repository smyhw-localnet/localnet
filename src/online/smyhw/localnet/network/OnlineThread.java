package online.smyhw.localnet.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;
import online.smyhw.localnet.event.SocketConnectEvent;

/**
 * @author smyhw
 * 
 * 
 */
public class OnlineThread extends Thread
{
	ServerSocket server_connect;
	String protocol;
	int port;
	public OnlineThread(int port,String protocol) throws Exception
	{
		if(!NetWorkManager.testProtocol(protocol))
		{
			message.warning("[网络线程]协议<"+protocol+">未知");
			return;
		}
		ServerSocket ss = new ServerSocket(port);
		this.protocol = protocol;
		this.port = port;
		this.server_connect =ss;
		message.show("[网络线程]监听端口<"+port+">使用协议<"+protocol+">");
		this.start();
	}
	public void run()
	{
		while(true)
		{
			try 
			{
				Socket connect = server_connect.accept();
				message.info("[网络线程]socket连接<"+connect.getInetAddress()+">");
				new SocketConnectThread(connect,this.protocol);
			}catch(IOException e){}catch(Exception e){}
		}
	}
	
	public ServerSocket getServerSocket()
	{
		return this.server_connect;
	}
	
}

class  SocketConnectThread extends Thread
{
	Socket s;
	String protocol;
	public SocketConnectThread(Socket s,String protocol)
	{
		this.s = s;
		this.protocol = protocol;
		this.start();
	}
	public void run()
	{
		//触发SocketConnectEvent事件，若该事件被取消，则直接返回
		if(new SocketConnectEvent(s).getCancel()) {return;}
		else 
		{
			List temp = new ArrayList();
			temp.add(s);
			new Client_sl(this.protocol,temp);
		}
	}
}