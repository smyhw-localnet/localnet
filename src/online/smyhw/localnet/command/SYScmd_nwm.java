package online.smyhw.localnet.command;

import java.io.IOException;
import java.net.ServerSocket;

import online.smyhw.localnet.*;
import online.smyhw.localnet.lib.*;
import online.smyhw.localnet.network.Client_sl;
import online.smyhw.localnet.network.NetWorkManager;


public class SYScmd_nwm
{
	
	public static void cmd(Client_sl User,String cmd)
	{
		switch(CommandFJ.fj(cmd, 1))
		{
		case "set":
			set.command(cmd);
			return;
		case "bind":
			NetWorkManager.bind(set.port);
			return;
		case "connect":
			NetWorkManager.connect(set.rhost, set.rport);
			return;
		case "help":
			message.info("+++NWM(NetWorkManager)+++");
			message.info("nwm help --显示此列表");
			message.info("nwm set port <int> ---设置本地监听端口号");
			message.info("nwm set rhost <IP> ---设置需要连接的IP");
			message.info("nwm set rport <int> ---设置需要连接的远程端口");
			message.info("nwm bind ---根据设置的端口开始监听");
			message.info("nwm connect ---根据设置的远程IP和端口尝试连接 //该功能弃用！由ln自备！");
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
