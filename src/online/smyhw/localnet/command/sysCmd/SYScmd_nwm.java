package online.smyhw.localnet.command.sysCmd;

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
		case "bind":
			if(CommandFJ.js(cmd)<=2) {message.show("参数不足，请使用/nwm help查看使用方法");return;}
			else
			{
				if(CommandFJ.js(cmd)<=3) 
				{//默认使用localnetTCP协议
					NetWorkManager.bind(Integer.parseInt(CommandFJ.fj(cmd, 2)),"localnetTCP");
				}
				else
				{
					NetWorkManager.bind(Integer.parseInt(CommandFJ.fj(cmd, 2)),CommandFJ.fj(cmd, 3));
				}
			}
			return;
		case "connect":
			if(CommandFJ.js(cmd)<=3) {message.show("参数不足，请使用/nwm help查看使用方法");return;}
			else
			{
				NetWorkManager.connect(CommandFJ.fj(cmd, 2),Integer.parseInt(CommandFJ.fj(cmd, 3)));
			}
			return;
//		case "lp":
			
//			return;
		case "help":
			message.info("+++NWM(NetWorkManager)+++");
			message.info("nwm help --显示此列表");
			message.info("nwm bind [Port] [protocol]---根据设置的端口和协议开始监听");
//			message.info("nwm lp ---输出本终端支持的所有协议");
			message.info("nwm connect [IP] [Port] ---根据设置的远程IP和端口尝试连接");
			return;
		default:
			message.info("未知的网络管理器指令，使用nwm help来显示帮助");
		}
	}
}