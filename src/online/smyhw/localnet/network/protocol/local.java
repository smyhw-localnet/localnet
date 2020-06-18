package online.smyhw.localnet.network.protocol;

import java.util.HashMap;
import java.util.List;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.network.Client_sl;

public class local implements StandardProtocol 
{
	Client_sl client;
	public local(List input,Client_sl sy)
	{
		this.client=sy;
	}

	@Override
	public void SendData(HashMap<String, String> msg) 
	{

	}

	@Override
	public void Disconnect() {
	}

}
