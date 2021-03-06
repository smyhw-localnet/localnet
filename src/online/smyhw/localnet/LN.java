package online.smyhw.localnet;

import java.io.*;
import java.util.*;
import online.smyhw.localnet.command.*;
import online.smyhw.localnet.data.DataManager;
import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.data.config;
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
	public static final int Version = 777;
	public static String ID;//本地终端ID
	public static List<OnlineThread> online_threads = Collections.synchronizedList(new ArrayList<OnlineThread>());//主联机进程
	public static input user_input;//主用户输入线程
	public static BufferedReader input;//主输入流
	public static ArrayList<Client_sl> client_list = new ArrayList< Client_sl>();//终端列表
	public static Local_sl local_sl;//虚拟本机客户端
	public static boolean LibMode = true; //是否处于lib模式
	

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
			message.info("初始化系统指令");
			cmdManager.Initialization();//初始化系统指令
			message.info("初始化插件");
			PluginsManager.start();//初始化插件
			message.info("本地虚拟客户端实例开始创建");
			local_sl = new Local_sl();
			message.info("本地虚拟客户端实例完成创建");
			message.show("为了保证安全性，联机进程默认未启用，请使用nwm手动启动！");
			message.info("初始化用户输入流");
			input = new BufferedReader(new InputStreamReader(System.in));//初始化用户输入流
			message.info("实例化用户输入进程");
			user_input=new input(input);//实例化用户主输入进程
			message.info("执行自动脚本...");
			user_input.DoScript();
			user_input.lock=false;
			//将lib模式置否
			LibMode = false;
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
	 * 核心交换处理方法
	 * 
	 * @param UserID
	 * @param msg
	 */
	public static void mdata(Client_sl User,DataPack msg)
	{
		String type = (String) msg.getValue("type");
		switch(type)
		{
		case "command":
		{
			String CmdText = (String) msg.getValue("CmdText");
			cmdManager.ln(User,CmdText);
			return;
		}
		case "note":
		{
			String NoteType = (String) msg.getValue("NoteType");
			String NoteText = (String)msg.getValue("NoteText");
			message.warning("来自终端<"+User.remoteID+">的警告{"+NoteType+"}:"+NoteText);
			return;
		}
		case "message":
		{
			String message = (String) msg.getValue("message");
			if(User.remoteID.equals(LN.ID) && msg.getValue("isSend")==null)
			{//本地发送的，目的地不是外部终端的
//				if(new ChatINFO_Event(User,User,message).Cancel) {return;}
				online.smyhw.localnet.message.show("[本地]:"+msg.getValue("message"));
				return;
			}
			//触发聊天事件
			if(new Chat_Event(User,message).Cancel) {return;}
			ArrayList<Client_sl> temp1 = (ArrayList<Client_sl>) LN.client_list.clone();
			Iterator<Client_sl> temp2 = temp1.iterator();
			while(temp2.hasNext())
			{
				Client_sl temp3 = temp2.next();
				if(temp3==User) {continue;}
				//激发事件
				if(new ChatINFO_Event(User,temp3,message).Cancel) {continue;}
				HashMap<String,String> send = new HashMap<String,String>();
				if(User.remoteID.equals(LN.ID))
				{//如果是本地发送，并且目的地不是发给本地的(目的地是本地的已经被上头筛掉了)，则当做正常消息发送出去
						send.put("type", "message");
				}
				else
				{//如果来源不是本地，则作为转发消息转发到所有连接到的终端
					send.put("type", "forward_message");
					send.put("From", User.remoteID);
				}
				send.put("message", message);
				temp3.sendData(new DataPack(send));
			}
			return;
		}
		case "forward_message":
		{
			message.show("["+msg.getValue("From")+"]:"+msg.getValue("message"));
			return;
		}
		case "auth":
		{
			if(User.remoteID!=null) {User.sendNote("1","鉴权重复");return;}
			String ID = msg.getValue("ID");
//			LNlib.XT_sendall("{\"type\":\"connect\",\"operation\":\"xt\"}");
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
			User.remoteID=ID;
			message.show("终端<"+User.remoteID+">完成握手");
			message.info("读取终端<"+User.remoteID+">的ClientData");
			User.ClientData=DataManager.LoadData("./TerminalData/"+User.remoteID);
			NetWorkManager.doclient(1, User, 0);
			new ClientConnected_Event(User);//激发事件
			return;
		}
		
		case"connect":
		{
			switch(msg.getValue("operation"))
			{
			case "xt":
				//能到这的都是向TM本地客户端发的心跳包，不用管
				
				break;
			case "disconnect":
				User.Disconnect("客户端主动断开连接");
				break;
			default:
				User.sendNote("5","未知的连接操作{"+msg.getValue("operation")+"}");
			}
			return;
		}
		default:
			User.sendNote("2","未知消息类型{"+msg.getStr()+"}");
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
	String input;//用户输入的数据
	boolean lock = true;
	input(BufferedReader input) throws Exception
	{
		
	}
	
	/**
	 * 执行指定脚本
	 */
	public void DoScript(List<String> Script)
	{
		for(String i:Script)
		{
			DoInput(i);
		}
	}
	
	/**
	 * 不指定参数则默认执行启动脚本
	 */
	public void DoScript()
	{
		File BeginFile = new File("./StartupScript");
		if(!BeginFile.exists()) {return;}
		List<String> StartupScript =new ArrayList<String>();
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("./StartupScript"));
			while(true)
			{
				String temp1 = br.readLine();
				if(temp1==null) {break;}
				StartupScript.add(temp1);
			}
			DoScript(StartupScript);
		}
		catch (Exception e) 
		{
			message.warning("执行启动脚本时出错",e);
		}
		
	}
	
	public synchronized void DoInput(String input)
	{
		DataPack map = ToPack(input);
		map.add("isSend", "true");
		LN.mdata(LN.local_sl,map);
	}
	
	/**
	 * 将用户输入的原始信息达成数据包</br>
	 * 这会将</>开头的语句识别的指令</br>
	 * @param input 用户输入的信息
	 * @return 数据包实例
	 */
	public DataPack ToPack(String input)
	{
		HashMap<String,String> re = new HashMap<String,String>();
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
		return new DataPack( re);
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(1000);
				if(this.lock) {continue;}
				input = LN.input.readLine();
//				message.info("取得用户输入："+input);
				System.out.print(LN.ID+"@"+LN.ID+">");
				DoInput(input);
			}
			catch(Exception e)
			{
				message.warning("获取用户输入出错！",e);
			}
		}
	} 
}




