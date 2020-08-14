package online.smyhw.localnet.command;

import online.smyhw.localnet.network.Client_sl;

public class SYScmd_help 
{
	public static void cmd(Client_sl User,String cmd) 
	{
		String re = cmdManager.cmd_list.toString();
		re="\n".concat(re);
		re=re.replace('=','\n');
		re=re.replace(',', '\n');
		User.sendMsg(re);
		return;
	}
}
