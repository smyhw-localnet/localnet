package online.smyhw.localnet;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import online.smyhw.localnet.event.chat_Event;
import online.smyhw.localnet.lib.TCP_LK;

/**
 * LocalNet项目
 * Powerd By smyhw
 * 
 * 
 * 最后更新时间：2019.12.19
 * @author smyhw
 *
 */
public class localnet
{

	public static String ID;//用户唯识别代号
	public static online online_thread;//主联机进程
	public static input user_input;//主用户输入线程
	public static int set_debug=0;//设置，1=开启debug输出
//	public static int set_test=0;//
	public static int set_re=0;//是否记录指令回显
	public static BufferedReader input;//主输入流
	public static ArrayList<Client_sl> client_list = new ArrayList< Client_sl>();//客户端列表
	public static Server_sl server_TCP;
//	public static char[][] UI = new char[100][62];//缓存用户UI界面
	public static Client_sl server_sl;
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
			try {localnet.ID = localnet.input.readLine();} catch (IOException e) {e.printStackTrace();return;}
			online_thread = new online();//实例化主联机进程
//			online_thread.start();//启动主联机进程
			message.show("为了保证安全性，联机进程默认未启用，请使用nwm手动启动！");
			user_input=new input(input);//实例化用户主输入进程
			{//在初始化插件指令前，先往指令集合里加入系统自带指令
				command.add_cmd("cmdList", null);
				command.add_cmd("help", null);
			}

			plugin.start();//初始化指令
			try {server_sl = new Client_sl(new Socket());}catch (Exception e) {}
			message.info("本地虚拟客户端实例开始创建");
			server_sl.s_out=new lnStream();
			server_sl.ID=localnet.ID;
//			server_sl.ISln=true;
			online.doclient(1, server_sl, 0);
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
	 * 启动来联机线程用的
	 * @param i 
	 */
	public static void ots(ServerSocket i) {online_thread.server_connect=i;online_thread.start();;}

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
		if(new chat_Event(User,msg).Cancel) {return;}
		switch(localnet.mdata_mod)
		{
		case 1:
		{
			msg=User.ID+"："+msg;
			msgList.add(msg);
			if(msgList.size()>=localnet.mdata_hc_num) {msgList.removeFirst();}
			Iterator<String> dd = msgList.iterator();
//			localnet.online_thread.SendAll(localnet.ID+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
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
		localnet.user_input.start();//启动用户主输入进程
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
class online extends Thread
{
	ServerSocket server_connect;
	Socket connect;
	online()
	{
//		try {server_connect = new ServerSocket(port);}
//		catch (IOException e) {e.printStackTrace();}//申请端口
	}
	public void run()
	{
		while(true)
		{
			try 
			{
				connect = server_connect.accept();
				message.info("一个外来连接接入！");
				new Client_sl(connect);
			}catch(IOException e){}catch(Exception e){}
		}
	}
	/**
	 * 
	 * 将收到的消息发送给所有的客户端
	 */

	
	/**
	 * 
	 * 操作客户端列表</br>
	 * 这个方法是线程安全的</br>
	 * 因为线程安全，所以可能会阻塞！</br>
	 * type==0	将信息移除list</br>
	 * type==1	将信息加入list</br>
	 * type==2	根据索引查询信息</br>
	 * type==4	根据索引插入信息</br>
	 * type==7	根据索引移除信息</br>
	 * </br>
	 * !如果任何一个参数不需要,则该参数可以为任何值</br>
	 * @param lj
	 */
	public synchronized static Client_sl doclient(int type,Client_sl lj,int sy)
	{
		Client_sl re = null;
		switch(type)
		{
		case 0:
			localnet.client_list.remove(lj);
			break;
		case 1:
			localnet.client_list.add(lj);
			break;
		case 2:
			re=localnet.client_list.get(sy);
			break;
		case 4:
			localnet.client_list.add(sy, lj);
			break;
		case 7 :
			localnet.client_list.remove(sy);
			break;
		default:
			message.warning("警告，doclient收到未定义的操作类型，将不会对list做出任何改动！");
			break;
		}
		return re;
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
				if(localnet.server_TCP!=null) {server=localnet.server_TCP.ID.trim();}
				message.input(localnet.ID+"@"+server+">");
				
				input = localnet.input.readLine();
				message.info("取得用户输入："+input);
				switch(input.substring(0, 1))
				{
				case "/":
					command.ln(localnet.server_sl,input.substring(1));
					break;
				case "!":
					input=input.substring(2);
					input="/"+input;
				default:
					if(localnet.server_TCP==null) {localnet.mdata(localnet.server_sl, input);}
					else{localnet.server_TCP.Smsg(input);}
					break;
				}
				
			}
			catch(Exception e)
			{
				message.warning("获取用户输入出错！");
			}
		}
	} 
}



class Server_sl extends TCP_LK
{
	String ID;
	public Server_sl(Socket s)
	{
		super(s,2);//这里，调用父类构造方法
		try
		{
			byte[] temp = new byte[1024];
			s.getInputStream().read(temp);
			ID=new String(temp,"UTF-8");
			this.Smsg("&"+localnet.ID);//发送自身ID

		}catch(Exception e){message.info("服务器\""+ID+"\"连接异常！丢弃线程");e.printStackTrace();return;}

	}
	public void CLmsg(String msg)
	{
		if(ID==null && !(msg.startsWith("&")))
		{message.info("此服务器尝试在未发送身份信息的情况下发送其他消息，不安全，断开连接！");}
		switch(msg.charAt(0))
		{
		case '&':
			msg=msg.replace('&', ' ');
			msg=msg.trim();
			if(LNlib.ID_repeat(msg)) 
			{
				message.show("服务器ID重复（与本机?）");
				return;
			}
			if(!LNlib.ID_rightful(msg))
			{
				message.show("服务器使用了不合法的ID!");
				return;
			}
			this.ID=msg;
			break;
		default:
			//我tm看谁控制台分辨率这么高????
			System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			message.show(msg);
			break;
		}
	}
	public void Serr_u()
	{

		StackTraceElement[] temp=Thread.currentThread().getStackTrace();
		StackTraceElement temp2=(StackTraceElement)temp[5];
		message.warning("连接到服务器出错，丢弃连接，位置："+temp2.getFileName()+":"+temp2.getClassName()+":"+temp2.getMethodName()+":"+temp2.getLineNumber());
		return;
	}
}



