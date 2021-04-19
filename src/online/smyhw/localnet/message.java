package online.smyhw.localnet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 信息发送/显示 以及 日志
 * @author smyhw
 */
public class message
{
	/**
	 * 向System.out.println输出,<br>
	 * 并续写提示符
	 * @param out_put
	 */
	protected static void fin_out(String out_put)
	{
		String prompt = LN.ID+"@"+LN.ID+">";
		String tmp1 = "";
		for(int i=0;i<=prompt.length();i++) {
			tmp1 = tmp1+" ";
		}
		System.out.print("\r"+tmp1);
		System.out.print("\r"+out_put+"\n");
		System.out.print(LN.ID+"@"+LN.ID+">");
	}
	
	/**
	 * 发送最普通的，展示给一般用户看的信息</br>
	 * 该信息会被记录进日志</br>
	 * @param input 需要显示的信息
	 */
	public static void show(String input)
	{
		log("[show]"+input);
		LN.LNconfig.toString();
		if(LN.LNconfig.get_boolean("data_for_show", true))
		{
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			input = "["+df.format(new Date())+"]"+"[show]"+input;
		}
		fin_out(input);
	}
	
	/**
	 * 发送info调试信息</br>
	 * 该信息仅在debug模式开启时输出</br>
	 * 但是始终会被记录进日志</br>
	 * @param input 需要发送的信息
	 */
	public static void info(String input)
	{
		log("[info]"+input);
		if(LN.LNconfig.get_int("debug",1)!=1) {return;}
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		input = "["+df.format(new Date())+"]"+"[info]"+input;
		fin_out(input);
		
	}
	
	/**
	 * 显示警告信息</br>
	 * 无论是否开启debug模式,该信息始终会被发送给用户</br>
	 * 该信息会被记录进日志
	 * @param input 需要发送的信息
	 */
	public static void warning(String input)//无论debug模式是否开启，总会输出warning信息
	{
		log("[warning]"+input);
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		input = "["+df.format(new Date())+"]"+"[warning]"+input;
		fin_out(input);
	}
	
	/**
	 * 可以包含一个异常的警告处理，将会在输出相关message后继续输出异常信息
	 * @param message
	 * @param e
	 */
	public static void warning(String message ,Exception e)
	{
		warning(message);
		e.printStackTrace();
	}
	
	
	/**
	 * 调用此方法意味着程序无法继续执行，调用此方法后程序将退出！
	 * @param input
	 */
	public static void error(String input)
	{
		log("[error]"+input);
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		input = "["+df.format(new Date())+"]"+"[error]"+input;
		System.out.println(input);
		System.exit(7);
	}
	
	
	public static void error(String message,Exception e)
	{
		e.printStackTrace();
		error(message);
	}
	
	public static void startLogThread()
	{
		LogThread temp = new LogThread();
		temp.start();
		logthread = temp;
	}
	
	/**
	 * 注意，该方法会自动对传入的消息加上时间标签
	 * @param input
	 */
	static LogThread logthread;
	static void log(String input)
	{
		if(!LN.LibMode)
		{
			logthread.msgList.add(input);
		}
	}
}



class LogThread extends Thread
{
	public ArrayList<String> msgList = new ArrayList<String>();
	protected PrintWriter LN_log;
	LogThread()
	{
		if(!new File("./logs").exists())
		{
			System.out.println("日志目录不存在，将创建...");
			new File("./logs").mkdir();
		}
		SimpleDateFormat df = new SimpleDateFormat("YY_HH_mm_ss");
		String log_name = "./logs/"+df.format(new Date())+".log";
		if(!new File(log_name).exists()) {try {new File(log_name).createNewFile();} 
		catch (IOException e) 
		{System.out.println("日志文件不存在且创建失败，程序退出！");e.printStackTrace();System.exit(1);}}
		try {LN_log = new PrintWriter(new File((log_name)));} 
		catch (FileNotFoundException e) 
		{System.out.println("日志文件不存在且创建后仍然无法写入，程序退出！");e.printStackTrace();System.exit(1);}
	}
	
	public void run()
	{
		while(true)
		{
			
			if(msgList.isEmpty()) 
			{
				try {Thread.sleep(500);} catch (InterruptedException e) {message.warning("log线程被强杀，程序退出", e);System.exit(1);}
				continue;
			}
			String temp = msgList.get(0);
			DoLog(temp);
			msgList.remove(0);
		}
	}
	
	
	protected void DoLog(String input)
	{
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		input = "["+df.format(new Date())+"]"+input;
		LN_log.println(input);
		LN_log.flush();
	}
}