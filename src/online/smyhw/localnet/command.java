package online.smyhw.localnet;

import java.util.HashMap;
import online.smyhw.localnet.lib.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;

public class command
{
	public static HashMap<String,Method> cmd_list = new HashMap<String, Method>();//存储指令和对应的类

	/**
	 * 
	 * @author hanhz
	 * 
	 * @param cmd 需要添加的指令
	 * @param rclass 处理这个指令的类
	 */
	public synchronized static void add_cmd(String cmd,Method mff)
	{
		try
		{
			message.info("添加指令"+cmd);
			if(cmd_list.containsKey(cmd)==false)
			{cmd_list.put(cmd, mff);}
			else{message.warning("添加指令\""+cmd+"\"失败，该指令已存在");}
		} catch (SecurityException e) 
		{message.info("添加指令\""+cmd+"\"时出现异常！");e.printStackTrace();}
	}
	
	/**
	 * 
	 * 
	 * @param from
	 * @param cmd
	 */
	public static void NETcmd(Client_sl from,String cmd)
	{
		
	}
	
	public static void ln(String command)
	{
//		if(UserID.equals(localnet.ID)) 
//		{
//			message.info("本地用户，最高权限!");
//		}
//		else
//		{
//			if(localnet.security(UserID, CommandFJ.fj(command, 0)))//判断是否有权运行该指令
//			{}
//			else {message.show("Insufficient authority\n权限不足");return;}
//		}
		String command_0=CommandFJ.fj(command,0);
//		message.info("开始解析:"+command);
		//这里判断几个系统指令
		switch(command_0)
		{
			case "cmdList":
			{
				String re = cmd_list.toString();
				re="\n".concat(re);
				re=re.replace('=','\n');
				re=re.replace(',', '\n');
				message.show(re);
				return;
			}
			case "connect":
			{
				String IP= CommandFJ.fj(command,1);
				int Port = Integer.parseInt(CommandFJ.fj(command,2));
				try {localnet.server_TCP=new Server_sl(new Socket(IP,Port));} catch (IOException e) {message.warning("连接至服务器\""+IP+":"+Port+"\"时出错！");e.printStackTrace();}
				return;
			}
			case "help":
			{
				message.show("请使用cmdList来列出指令映射表！");
				return;
			}
		}
		if(cmd_list.containsKey(command_0)==false) {message.show("未知指令！\n请使用cmdList列出指令列表");return;};
		try
		{
			Method re = (Method) cmd_list.get(command_0);
			re.invoke(null,null, command);
		}
		catch(Exception e) 
		{
			message.error("执行指令\""+command+"\"时出现错误");
			e.printStackTrace();
		}
	}
}