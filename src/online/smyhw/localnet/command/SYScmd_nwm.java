package online.smyhw.localnet.command;

import online.smyhw.localnet.*;
import online.smyhw.localnet.lib.*;
import online.smyhw.localnet.network.Client_sl;
import online.smyhw.localnet.network.NetWorkManager;


public class SYScmd_nwm
{
	
	public static void cmd(Client_sl User,String cmd)
	{
		if(User!=LN.local_sl) {User.sendMsg("抱歉,您不能使用服务器的关键指令!");}
		switch(CommandFJ.fj(cmd, 1))
		{
		case "set":
			set.command(cmd);
			return;
		case "bind":
			if(CommandFJ.js(cmd)<=2) {NetWorkManager.bind(set.port);}
			else
			{
				NetWorkManager.bind(Integer.parseInt(CommandFJ.fj(cmd, 2)));
			}
			return;
		case "connect":
			if(CommandFJ.js(cmd)<=3) {NetWorkManager.connect(set.rhost, set.rport);}
			else
			{
			NetWorkManager.connect(CommandFJ.fj(cmd, 2),Integer.parseInt(CommandFJ.fj(cmd, 3)));
			}
			return;
		case "help":
			message.info("+++NWM(NetWorkManager)+++");
			message.info("nwm help --显示此列表");
			message.info("nwm set port <int> ---设置本地监听端口号");
			message.info("nwm set rhost <IP> ---设置需要连接的IP");
			message.info("nwm set rport <int> ---设置需要连接的远程端口");
			message.info("nwm bind [Port] ---根据设置的端口开始监听");
			message.info("nwm connect [IP] [Port] ---根据设置的远程IP和端口尝试连接");
			return;
		default:
			message.info("未知的网络管理器指令，使用nwm help来显示帮助");
		}
	}
}
class set
{	
	static int port=0;
	static String rhost="0.0.0.0";
	static int rport =0;
	
	public static void list()
	{
		message.info("bind_port:"+set.port);
		message.info("RHOST"+set.rhost);
		message.info("rport:"+set.rport);
	}
	public static void command(String cmd)
	{
		switch(CommandFJ.fj(cmd, 2))
		{
		case "port":
			try {set.port=Integer.parseInt(CommandFJ.fj(cmd, 3));}catch(Exception e) {e.printStackTrace();}
			message.show("设置监听端口为："+set.port);
			return;
		case "rport":
			try {set.rport=Integer.parseInt(CommandFJ.fj(cmd, 3));}catch(Exception e) {e.printStackTrace();}
			message.show("设置连接端口为："+set.rport);
			return;
		case "rhost":
			set.rhost=CommandFJ.fj(cmd, 3);
			message.show("设置连接IP为："+set.rhost);
			return;
		default:
			message.info("未知的设置选项");
			return;
		}
	}
}
