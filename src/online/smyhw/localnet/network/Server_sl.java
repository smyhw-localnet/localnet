package online.smyhw.localnet.network;

import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.LNlib;
import online.smyhw.localnet.message;
import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.event.ConnectServerEvent;
import online.smyhw.localnet.event.DataDecryptEvent;
import online.smyhw.localnet.lib.Json;
import online.smyhw.localnet.lib.TCP_LK;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;

public class Server_sl extends Client_sl
{
	public Server_sl(Socket s)
	{
		super(s,2);//这里，调用父类构造方法
		new ConnectServerEvent(this);
	}
	public void CLmsg(String msg)
	{
		message.info("收到来自服务器的原始消息："+msg);
		HashMap<String,String> map = Json.Parse(msg);
		if(ID==null && !map.get("type").equals("auth"))
		{message.warning("此服务器尝试在未发送身份信息的情况下发送其他消息，不安全，断开连接！");return;}
		if(ID==null) {ID = (String) map.get("ID");return;}
		LN.mdata(this,new DataPack( map));
	}
	public void Serr_u( TCP_LK_Exception e)
	{
		LN.server_sl = null;
		message.warning("连接到服务器出错，丢弃连接",e);
		return;
	}
	
}


