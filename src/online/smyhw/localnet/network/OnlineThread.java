package online.smyhw.localnet.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
				List temp = new ArrayList();
				temp.add(connect);
				new Client_sl("localnetTCP",temp);
			}catch(IOException e){}catch(Exception e){}
		}
	}
}