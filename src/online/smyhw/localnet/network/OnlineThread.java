package online.smyhw.localnet.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;

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
			message.warning("协议<"+protocol+">未知");
			return;
		}
		ServerSocket ss = new ServerSocket(port);
		this.protocol = protocol;
		this.port = port;
		this.server_connect =ss;
		message.show("监听端口<"+port+">使用协议<"+protocol+">");
		this.start();
	}
	public void run()
	{
		while(true)
		{
			try 
			{
				Socket connect = server_connect.accept();
				message.info("socket连接<"+connect.getInetAddress()+">");
				List temp = new ArrayList();
				temp.add(connect);
				new Client_sl(this.protocol,temp);
			}catch(IOException e){}catch(Exception e){}
		}
	}
	
	public ServerSocket getServerSocket()
	{
		return this.server_connect;
	}
}