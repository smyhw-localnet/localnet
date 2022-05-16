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
 * 最后更新时间：2022.1.1
 * @author smyhw
 *
 */
public class LN
{
	public static final int Version = 777;
	public static String ID;//本地终端ID
	public static List<OnlineThread> online_threads = Collections.synchronizedList(new ArrayList<OnlineThread>());//联机进程列表
	public static input user_input;//控制台输入线程
	public static ArrayList<Client_sl> client_list = new ArrayList< Client_sl>();//终端列表
	

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
			helper.call_back();
			message.info("初始化系统指令");
			cmdManager.Initialization();//初始化系统指令
			message.info("初始化插件");
			PluginsManager.start();//初始化插件
			message.info("实例化控制台输入进程");
			user_input=new input(new BufferedReader(new InputStreamReader(System.in)));//实例化用户主输入进程
			message.info("执行启动脚本...");
			user_input.DoScript();
			user_input.lock=false;
			message.info("localnet初始化完成!");
			smyhw.main();//显示欢迎屏幕并阻塞
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
	 * 现在，这是处理外来数据包的专用逻辑序列了
	 * 
	 * @param User 来路终端
	 * @param msg 实际数据包
	 */
	public static void mdata(Client_sl User,DataPack msg)
	{
		//如果客户端已经被踢出或异常
		if((!(((String) msg.getValue("type")).equals("auth"))) && (helper.Find_Client(User.remoteID)==null)) {
			User.sendNote("异常连接", "连接发生异常或您已被踢出，请尝试重新连接");
			return;
		}
		String type = (String) msg.getValue("type");
		switch(type)
		{
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
			//触发聊天事件
			if(new Chat_Event(User,false,message).Cancel) {return;}
			online.smyhw.localnet.message.show("["+User.remoteID+"]:"+message);
			return;
		}
		case "auth":
		{
			if(User.remoteID!=null) {User.sendNote("1","鉴权重复");return;}
			String ID = msg.getValue("ID");
//			LNlib.XT_sendall("{\"type\":\"connect\",\"operation\":\"xt\"}");
			if(helper.ID_repeat(ID)) 
			{
				User.sendNote("1","ID重复！");
				return;
			}
			if(!helper.ID_rightful(ID))
			{
				User.sendNote("1","ID不合法");
				return;
			}
			User.remoteID=ID;
			message.info("读取终端<"+User.remoteID+">的ClientData");
			User.ClientData=DataManager.LoadData("./TerminalData/"+User.remoteID);
			NetWorkManager.doclient(1, User, 0);
			new ClientConnected_Event(User);//激发事件
			message.show("终端<"+User.remoteID+">上线");
			return;
		}
		
		case"connect":
		{
			switch(msg.getValue("operation"))
			{
			case "disconnect":
				User.Disconnect("终端<"+User.remoteID+">主动断开连接");
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
		message.show("|   version:2.1           |");
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
	BufferedReader input_reader;
	boolean lock = true;
	input(BufferedReader input) throws Exception
	{
		this.input_reader = input;
	}
	
	/**
	 * 执行指定脚本
	 */
	public void DoScript(List<String> Script)
	{
		for(String i:Script)
		{
			exec(i);
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
	
	/**
	 * 控制台输入处理流程
	 * 
	 * @param input
	 */
	public synchronized void exec(String input)
	{
		if(input.startsWith("/"))
		{
			String msg  = input.substring(1);
			cmdManager.exec(msg);
		}else {
			//TODO 事件<控制台输入>
		}
		
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				if(this.lock) {Thread.sleep(1000);continue;}
				String tmp1 = this.input_reader.readLine();
//				message.info("取得用户输入："+input);
				System.out.print(LN.ID+"@localnet>");
				exec(tmp1);
			}
			catch(Exception e)
			{
				message.warning("控制台输入出错！",e);
			}
		}
	} 
}




