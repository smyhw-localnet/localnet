package online.smyhw.localnet.network.protocol;

import java.util.HashMap;
import java.util.List;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;
import online.smyhw.localnet.network.Client_sl;

public class local implements StandardProtocol 
{
	Client_sl client;
	public local(List input,Client_sl sy)
	{
		this.client=sy;
		HashMap<String,String> authMap = new HashMap<String,String>();
		authMap.put("type", "auth");
		authMap.put("ID", LN.ID);
		sy.CLmsg(authMap);
	}

	@Override
	public void SendData(HashMap<String, String> msg) 
	{
		this.client.CLmsg(msg);
	}

	@Override
	public void Disconnect() 
	{
		message.warning("有程式试图将本地客户端断开连接，这是不可能的操作.");
	}

}
