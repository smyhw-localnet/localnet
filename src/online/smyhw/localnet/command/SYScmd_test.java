package online.smyhw.localnet.command;

import java.util.Hashtable;

import online.smyhw.localnet.message;
import online.smyhw.localnet.lib.CommandFJ;
import online.smyhw.localnet.lib.Json;
import online.smyhw.localnet.network.Client_sl;

public class SYScmd_test 
{
	public static void cmd(Client_sl User,String cmd) 
	{
		User.sendto("test");
		String test1 = CommandFJ.fj(cmd, 1);
		Hashtable test2 = new Hashtable();
		test2.put("awa", test1);
		String re = Json.Create(test2);
		User.sendto(re);
		return;
	}
}
