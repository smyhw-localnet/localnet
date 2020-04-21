package online.smyhw.localnet.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import online.smyhw.localnet.message;

public class OnlineThread extends Thread
{
	ServerSocket server_connect;
	Socket connect;
	OnlineThread(ServerSocket ss)
	{
		this.server_connect =ss;
//		try {server_connect = new ServerSocket(port);}
//		catch (IOException e) {e.printStackTrace();}//申请端口
		this.start();
	}
	public void run()
	{
		while(true)
		{
			try 
			{
				connect = server_connect.accept();
				message.info("接收到一个新连接。。。");
				new Client_sl(connect);
			}catch(IOException e){}catch(Exception e){}
		}
	}
}