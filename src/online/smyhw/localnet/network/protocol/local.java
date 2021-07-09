package online.smyhw.localnet.network.protocol;

import java.util.List;

import online.smyhw.localnet.message;
import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.network.Client_sl;

public class local implements StandardProtocol 
{
	Client_sl client;
	public local(List input,Client_sl sy)
	{
		this.client=sy;
	}

	@Override
	public void SendData(DataPack data) 
	{
		this.client.CLmsg(data);
	}

	@Override
	public void Disconnect() 
	{
		message.warning("[本地协议]有程式试图将本地客户端断开连接，这是不可能的操作.");
	}

}
