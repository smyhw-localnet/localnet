package online.smyhw.localnet.command.sysCmd;

import online.smyhw.localnet.network.Client_sl;

public class SYScmd_test 
{
	public static void cmd(Client_sl User,String cmd) 
	{
		User.sendMsg("test");
		return;
	}
}
