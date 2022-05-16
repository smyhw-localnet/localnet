package online.smyhw.localnet.command.sysCmd;

import java.util.ArrayList;
import java.util.Iterator;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;
import online.smyhw.localnet.network.Client_sl;

public class SYScmd_list 
{
	public static void cmd(String cmd) 
	{
		String re = new String();
		re="在线终端列表:\n";
		ArrayList<Client_sl> temp1 = (ArrayList<Client_sl>) LN.client_list.clone();
		Iterator<Client_sl> temp2 = temp1.iterator();
		while(temp2.hasNext())
		{
			Client_sl temp3 = temp2.next();
			re=re+temp3.remoteID+"\n";
		}
		message.show(re);
		return;
	}
}
