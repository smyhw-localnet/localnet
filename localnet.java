package online.smyhw.localnet;

import java.io.*;
import java.util.*;
import java.net.*;

public class localnet
{
	public static String ID;//用户唯识别代号
	public static Date time;//时间类
	public static online online_thread;//主联机进程
	public static input user_input;//主用户输入线程
	public static int set_debug=1;//设置，1=开启debug输出
	public static int set_test=0;//
	public static BufferedReader input;//主输入流
	
	public static void main(String args[]) throws Exception
	{
		message.show("localnet初始化中...");
		input = new BufferedReader(new InputStreamReader(System.in));//初始化用户输入流
		online_thread = new online();//实例化主联机进程
		time = new Date();//实例化时间类
		online_thread.start();//启动主联机进程
		user_input=new input(input);//实例化用户主输入进程
		smyhw.main();//awa
	}
}


//真正的主方法XD
class smyhw
{

	public static void main() throws Exception
	{
		message.show("+==========smyhw==========+");
		message.show("|   version:1.0           |");
		message.show("|                         |");
		message.show("|                         |");
		message.show("+=========localnet========+");
		message.show("\n\n\n\n\n\n\n\n\n");
		message.info("输入ID");
		localnet.ID = localnet.input.readLine();
		localnet.user_input.start();//启动用户主输入进程
		while(true) {}
	}
	public static void loop()
	{
		while(true)
		{
			
		}
	}
}
class online extends Thread
{
	ServerSocket server_connect;
	Socket connect;
	online() throws Exception
	{
		server_connect = new ServerSocket(4201);//申请端口
	}
	public void run()
	{
		try {
			while(true)
			{
				connect = server_connect.accept();
				message.info("一个客户端开始连接");
				online_threads temp = new online_threads(connect);
				temp.start();
			}
		}catch(IOException e){}catch(Exception e){}
	}
}

class online_threads extends Thread
{
	DataInputStream in;
	DataOutputStream out;
	Socket awa_connect;
	online_threads(Socket connect) throws Exception
	{
		awa_connect=connect;
		in = new DataInputStream(connect.getInputStream());
		out = new DataOutputStream(connect.getOutputStream());
		message.info("成功建立出入连接");
	}
	public void run()
	{
		try
		{
			out.writeUTF(localnet.ID);
			String ID=in.readUTF();
			out.writeUTF("OK");
			String net_command=in.readUTF();
			command.net(ID,net_command);
			awa_connect.close();
		}catch(IOException e){}
	}
}

class input extends Thread
{
	String input;//用户输入的数据
	input(BufferedReader input) throws Exception
	{
		
	}
	public void run()
	{
		try
		{
			while(true)
			{
				message.info("等待用户输入");
				input = localnet.input.readLine();
				message.info("取得用户输入："+input);
				command.local(input);
			}
		}
		catch(Exception e)
		{
			
		}
	}
}
