package online.smyhw.localnet.command.sysCmd;

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
		Client_sl tUS = LNlib.Find_Client(ID);
		if(tUS==null) 
		{
			User.sendMsg("指定的终端ID不存在!");
			return;
		}
		tUS.Disconnect("被本地终端踢出");
		return;
	}
}
