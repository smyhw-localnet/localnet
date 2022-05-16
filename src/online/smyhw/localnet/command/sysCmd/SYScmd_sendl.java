package online.smyhw.localnet.command.sysCmd;

import online.smyhw.localnet.helper;
import online.smyhw.localnet.message;
import online.smyhw.localnet.event.Chat_Event;
import online.smyhw.localnet.lib.CommandFJ;

public class SYScmd_sendl 
{
	public static void cmd(String cmd) 
	{
		if(CommandFJ.js(cmd)<2) {
			message.show("参数不足(sendl <消息>)");
			return;
		}
		new Chat_Event(helper.get_local_client(),false,CommandFJ.fj(cmd, 1));
	}
}
