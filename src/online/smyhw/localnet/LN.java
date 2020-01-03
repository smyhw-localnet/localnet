package online.smyhw.localnet;

import java.io.*;
import java.net.*;
import java.util.*;

import online.smyhw.localnet.command.*;
import online.smyhw.localnet.event.*;
import online.smyhw.localnet.network.*;
import online.smyhw.localnet.plugins.PluginsManager;

/**
 * LocalNet项目
 * Powerd By smyhw
 * 
 * 
 * 最后更新时间：2020.12.3
 * @author smyhw
 *
 */
public class LN
{

	public static String ID;//用户唯识别代号
	public static OnlineThread online_thread;//主联机进程
	public static input user_input;//主用户输入线程
	public static int set_debug=0;//设置，1=开启debug输出
//	public static int set_test=0;//
	public static int set_re=0;//是否记录指令回显
	public static BufferedReader input;//主输入流
	public static ArrayList<Client_sl> client_list = new ArrayList< Client_sl>();//客户端列表
	public static Server_sl server_TCP;//连接到的服务器
//	public static char[][] UI = new char[100][62];//缓存用户UI界面
	public static Client_sl server_sl;//虚拟本机客户端
	//mdata模式，
	//1:聊天模式/发送缓存
	//2:转发模式
	public static int mdata_mod=1;
	//mdata 缓存大小/行
	public static int mdata_hc_num=10;
	
	public static void main(String args[])
	{
		try
		{
			message.show("localnet初始化中...");
			LNlib.call_back();
			input = new BufferedReader(new InputStreamReader(System.in));//初始化用户输入流
			message.show("输入ID:");
			try {LN.ID = LN.input.readLine();} catch (IOException e) {e.printStackTrace();return;}
//			online_thread = new OnlineThread();//实例化主联机进程
//			online_thread.start();//启动主联机进程
			message.show("为了保证安全性，联机进程默认未启用，请使用nwm手动启动！");
			user_input=new input(input);//实例化用户主输入进程
			cmdManager.Initialization();//初始化系统指令
			PluginsManager.start();//初始化插件
			try {server_sl = new Client_sl(new Socket());}catch (Exception e) {}
			message.info("本地虚拟客户端实例开始创建");
			server_sl.s_out=new lnStream();
			server_sl.ID=LN.ID;
//			server_sl.ISln=true;
			NetWorkManager.doclient(1, server_sl, 0);
			message.info("本地虚拟客户端实例完成创建");
			
			smyhw.main();//awa
		}
		catch(Exception e)
		{
			message.error("严重错误！localnet初始化失败，程序将退出");
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * 
	 * 至关重要的一个方法，接受并处理来自所有终端发来的信息
	 * 
	 * 
	 * @param UserID
	 * @param msg
	 */
	static LinkedList<String> msgList = new LinkedList<String>();
	public static void mdata(Client_sl User,String msg)
	{
		char keyword = msg.charAt(0);
		switch(keyword)
		{
		case '/':
		{
			msg=msg.substring(1);
			cmdManager.ln(User,msg);
			return;
		}
		case '!':
		{
			msg=msg.substring(1);
			if(User==LN.server_sl) {}else {User.Smsg("警告！指令信息严禁递归发送！这可能会导致不可预知的严重后果！");return;}

			msg="/"+msg;
			if(LN.server_TCP==null) {message.warning("未连接到远程服务器，不能向远程服务器发送指令！");}
			else{LN.server_TCP.Smsg(msg);}
			return;
		}
		case '*':
		{
			msg=msg.substring(1);
			//触发聊天事件
			if(new chat_Event(User,msg).Cancel) {return;}
			if(LN.server_TCP!=null) {LN.server_TCP.Smsg(msg);return;}//如果连接到了服务器，直接发送至服务器即可
			switch(LN.mdata_mod)
			{
			case 1:
			{
				msg=User.ID+"："+msg;
				msgList.add(msg);
				if(msgList.size()>=LN.mdata_hc_num) {msgList.removeFirst();}
				Iterator<String> dd = msgList.iterator();
//				localnet.online_thread.SendAll(localnet.ID+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				String finmsg = "\r\n";
				while(dd.hasNext())
				{
					finmsg=finmsg+dd.next()+"\n";
				}
				LNlib.SendAll(finmsg);
				break;
			}
			case 2:
			{
				msg=User.ID+"："+msg;
				LNlib.SendAll(msg);
				break;
			}
			
			}
			
			return;
		}
		case '&':
		{
			if(User.ID!=null) {User.Smsg("!1请误重复鉴权!");return;}
			msg=msg.substring(1);
			if(LNlib.ID_repeat(msg)) 
			{
				User.Smsg("!1ID重复！");
				return;
			}
			if(!LNlib.ID_rightful(msg))
			{
				User.Smsg("!1ID不合法！");
				return;
			}
			User.ID=msg;
//			System.out.println("aaa");
			NetWorkManager.doclient(1, User, 0);
			new Client_connect_Event(User);//激发事件
			return;
		}
		default:
			msg="*"+msg;
			mdata(User,msg);
			return;
		}

		


	}
	
	
}

//真正的主方法XD
class smyhw
{

	public static void main()
	{
		message.show("+==========smyhw==========+");
		message.show("|   version:1.0           |");
		message.show("|                         |");
		message.show("|                         |");
		message.show("+=========localnet========+");
//		message.show("\n\n\n\n\n\n\n\n\n");
		LN.user_input.start();//启动用户主输入进程
		loop();//进入主循环
	}
	public static void loop()
	{
		while(true)
		{
			try {Thread.sleep(20);} catch (InterruptedException e) {message.show("主循环出现延迟问题！");e.printStackTrace();}
		}
	}
}


class input extends Thread
{
	String server = "localnet";//连接到的服务器
	String input;//用户输入的数据
	input(BufferedReader input) throws Exception
	{
		
	}
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(1000);
				if(LN.server_TCP!=null) {server=LN.server_TCP.ID.trim();}
				message.input(LN.ID+"@"+server+">");
				
				input = LN.input.readLine();
				message.info("取得用户输入："+input);

				LN.mdata(LN.server_sl,input);
				
			}
			catch(Exception e)
			{
				message.warning("获取用户输入出错！");
			}
		}
	} 
}




