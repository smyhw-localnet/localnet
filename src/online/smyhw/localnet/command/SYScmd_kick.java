package online.smyhw.localnet.command;

import online.smyhw.localnet.LNlib;
import online.smyhw.localnet.lib.CommandFJ;
import online.smyhw.localnet.network.Client_sl;

public class SYScmd_kick 
{
	public static void cmd(Client_sl User,String cmd) 
	{
		if(CommandFJ.js(cmd)<2) 
		{
			User.sendMsg("参数错误!请指定需要断开的终端ID");
			return;
		}
		String ID = CommandFJ.fj(cmd, 1);
		User.sendMsg("该指令使用断开socket的方式断开与制定终端的连接!");
		Client_sl tUS = LNlib.Find_Client(ID);
		if(tUS==null) 
		{
			User.sendMsg("指定的终端ID不存在!");
			return;
		}
		try 
		{
			tUS.s.close();
		}
		catch (Exception e) 
		{
			User.sendMsg("踢出失败,异常见主机终端");
			e.printStackTrace();
		}
		return;
	}
}
