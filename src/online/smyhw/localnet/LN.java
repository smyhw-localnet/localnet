package online.smyhw.localnet;

import java.io.*;
import java.util.*;
import online.smyhw.localnet.command.*;
import online.smyhw.localnet.data.DataManager;
import online.smyhw.localnet.data.config;
import online.smyhw.localnet.event.*;
import online.smyhw.localnet.lib.Json;
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
	public static BufferedReader input;//主输入流
	public static ArrayList<Client_sl> client_list = new ArrayList< Client_sl>();//客户端列表
	public static Server_sl server_sl;//连接到的服务器
	public static Local_sl local_sl;//虚拟本机客户端
	

	//主配置文件
	public static config LNconfig = new config();
	
	public static void main(String args[])
	{
		try
		{
			message.startLogThread();
			message.info("localnet初始化中...");
			message.info("实例化日志线程...");
			//实例化日志线程
			message.info("检查目录完整性...");
			DataManager.CheckIntegrity();
			message.info("读取配置文件...");
			LNconfig=DataManager.LoadConfig("./LN.config");
			DataManager.SaveConfig("./LN.config", LNconfig);
			ID = LNconfig.get_String("ID","awa");
			message.info("读取配置文件:ID="+ID);
			message.info("读取配置文件:debug="+LNconfig.get_int("debug",1));
			message.info("加载lib...");
			LNlib.call_back();
			message.info("初始化用户输入流");
			input = new BufferedReader(new InputStreamReader(System.in));//初始化用户输入流
			message.info("实例化用户输入进程");
			user_input=new input(input);//实例化用户主输入进程
			message.info("初始化系统指令");
			cmdManager.Initialization();//初始化系统指令
			message.info("初始化插件");
			PluginsManager.start();//初始化插件
			message.info("本地虚拟客户端实例开始创建");
			local_sl = new Local_sl();
			NetWorkManager.doclient(1, local_sl, 0);
			message.info("本地虚拟客户端实例完成创建");
			message.show("为了保证安全性，联机进程默认未启用，请使用nwm手动启动！");
			if(EventManager.DataDecrypt_Listener.isEmpty())//检测是否存在插件监听加密事件
			{message.warning("警告，没有检测到加密插件，localnet的信息将以明文传输！（如果你的数据会经过非安全网络传输，请务必对数据进行加密！）");}
			message.info("localnet初始化完成!");
			smyhw.main();//awa
		}
		catch(Exception e)
		{
			message.error("严重错误！localnet初始化失败，程序将退出",e);
			System.out.println(e.getMessage());
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
	public static void mdata(Client_sl User,HashMap<String,String> msg)
	{
		String type = (String) msg.get("type");
		switch(type)
		{
		case "command":
		{
			String CmdText = (String) msg.get("CmdText");
			cmdManager.ln(User,CmdText);
			return;
		}
		case "note":
		{
			String NoteType = (String) msg.get("NoteType");
			String NoteText = (String)msg.get("NoteText");
			message.warning("来自终端<"+User.ID+">的警告{"+NoteType+"}:"+NoteText);
			return;
		}
		case "message":
		{
			String message = (String) msg.get("message");
			//触发聊天事件
			if(new Chat_Event(User,message).Cancel) {return;}
			if(LN.server_sl!=null) 
			{//如果连接到了服务器，检查是否是服务器发来的
				if(User==LN.server_sl) 
				{
					online.smyhw.localnet.message.show("["+User.ID+"]:"+message);
					return;
				}
				LN.server_sl.Smsg(Json.Create(msg));
				return;	
			}
//			online.smyhw.localnet.message.show("["+User.ID+"]:"+message);//注意，别忘了自己本身也要打印这个消息
			ArrayList<Client_sl> temp1 = (ArrayList<Client_sl>) LN.client_list.clone();
			Iterator<Client_sl> temp2 = temp1.iterator();
			while(temp2.hasNext())
			{
				Client_sl temp3 = temp2.next();
				if(temp3==User) {continue;}
				if(new ChatINFO_Event(User,temp3,message).Cancel) {continue;}
				HashMap send = new HashMap();
				if(User==LN.local_sl)
				{send.put("type", "message");}
				else
				{send.put("type", "forward_message");}
				send.put("message", message);
				temp3.sendData(send);
			}
			return;
		}
		case "auth":
		{
			if(User.ID!=null) {User.sendMsg("!1请误重复鉴权!");return;}
			ID = msg.get("ID");
			if(LNlib.ID_repeat(ID)) 
			{
				User.sendNote("1","ID重复！");
				return;
			}
			if(!LNlib.ID_rightful(ID))
			{
				User.sendNote("1","ID不合法");
				return;
			}
			User.ID=ID;
			message.show("终端<"+User.ID+">完成握手");
			message.info("读取终端<"+User.ID+">的ClientData");
			User.ClientData=DataManager.LoadData("./TerminalData/"+User.ID);
			NetWorkManager.doclient(1, User, 0);
			new ClientConnected_Event(User);//激发事件
			return;
		}
		default:
			User.sendNote("2","未知消息类型");
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
	
	public static HashMap ToMap(String input)
	{
		HashMap re = new HashMap();
		if(input.startsWith("/"))
		{
			String msg  = input.substring(1);
			re.put("CmdText", msg);
			re.put("type","command");
		}
		else
		{
			re.put("type", "message");
			re.put("message",input);
		}
		return re;
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(1000);
				if(LN.server_sl!=null) {server=LN.server_sl.ID.trim();}
				message.input(LN.ID+"@"+server+">");
				
				input = LN.input.readLine();
				message.info("取得用户输入："+input);
				HashMap map = ToMap(input);
				LN.mdata(LN.local_sl,map);
				
			}
			catch(Exception e)
			{
				message.warning("获取用户输入出错！",e);
			}
		}
	} 
}




