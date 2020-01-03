package online.smyhw.localnet.command;

import java.util.HashMap;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;
import online.smyhw.localnet.lib.*;
import online.smyhw.localnet.network.Client_sl;
import online.smyhw.localnet.network.NetWorkManager;

import java.lang.reflect.Method;

public class cmdManager
{
	public static HashMap<String,Method> cmd_list = new HashMap<String, Method>();//存储指令和对应的类

	
	/**
	 * 
	 * 方法目的：加载默认指令
	 */
	public static void Initialization()
	{
		try 
		{
			cmdManager.add_cmd("nwm", SYScmd_nwm.class.getMethod("cmd", Client_sl.class,String.class));
			cmdManager.add_cmd("help", SYScmd_help.class.getMethod("cmd", Client_sl.class,String.class));
		} 
		catch (Exception e) 
		{
			message.warning("警告！加载系统指令时出错，可能会造成不可预知的问题！");
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
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
		{message.warning("添加指令\""+cmd+"\"时出现异常！");e.printStackTrace();}
	}

	
	public static void ln(Client_sl User,String command)
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
		message.info("开始解析:"+command);
		if(cmd_list.containsKey(command_0)==false) {User.Smsg("未知指令！\n请使用cmdList列出指令列表");return;};
		try
		{
			Method re = (Method) cmd_list.get(command_0);
			re.invoke(null,User, command);
		}
		catch(Exception e) 
		{
			message.error("执行指令\""+command+"\"时出现错误");
			e.printStackTrace();
		}
	}
}