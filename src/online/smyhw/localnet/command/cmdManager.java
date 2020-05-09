package online.smyhw.localnet.command;

import java.util.Hashtable;

import online.smyhw.localnet.message;
import online.smyhw.localnet.event.DoCommandEvent;
import online.smyhw.localnet.lib.*;
import online.smyhw.localnet.network.Client_sl;
import java.lang.reflect.Method;

public class cmdManager
{
	public static Hashtable<String,Method> cmd_list = new Hashtable<String, Method>();//存储指令和对应的类

	
	/**
	 * 
	 * 方法目的：加载默认指令
	 */
	public static void Initialization()
	{
		try 
		{
			cmdManager.add_cmd("doc", SYScmd_doc.class.getMethod("cmd", Client_sl.class,String.class));
			cmdManager.add_cmd("nwm", SYScmd_nwm.class.getMethod("cmd", Client_sl.class,String.class));
			cmdManager.add_cmd("help", SYScmd_help.class.getMethod("cmd", Client_sl.class,String.class));
			cmdManager.add_cmd("kick", SYScmd_kick.class.getMethod("cmd", Client_sl.class,String.class));
			cmdManager.add_cmd("list", SYScmd_list.class.getMethod("cmd", Client_sl.class,String.class));
			cmdManager.add_cmd("test", SYScmd_test.class.getMethod("cmd", Client_sl.class,String.class));
		} 
		catch (Exception e) 
		{
			message.warning("警告！加载系统指令时出错，可能会造成不可预知的问题！",e);
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
			if(cmd_list.containsKey(cmd)==false)
			{
				cmdManager.cmd_list.put(cmd, mff);
			}
			else{message.warning("添加指令\""+cmd+"\"失败，该指令已存在");}
			message.info("指令\""+cmd+"\"添加完成{"+cmd_list.containsKey(cmd)+"}");
		} 
		catch (Exception e) 
		{
			message.warning("添加指令\""+cmd+"\"时出现异常！");
			e.printStackTrace();
		}
	}

	
	public static void ln(Client_sl User,String command)
	{
		//触发事件
		if(new DoCommandEvent(User,command).Cancel) {message.info("终端<"+User.ID+">使用指令<"+command+">因事件处理而被拒绝执行");return;}
		String command_0=CommandFJ.fj(command,0);
		message.info("处理指令<"+command+">");
		if(cmd_list.containsKey(command_0)==false) {User.sendMsg("未知指令！\n请使用cmdList列出指令列表");return;};
		try
		{
			Method re = (Method) cmd_list.get(command_0);
			re.invoke(null,User, command);
		}
		catch(Exception e) 
		{
			message.warning("执行指令\""+command+"\"时出现错误",e);
		}
	}
}