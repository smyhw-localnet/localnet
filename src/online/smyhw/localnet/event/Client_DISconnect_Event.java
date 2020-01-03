package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

public class Client_DISconnect_Event
{
	Client_sl client;
	public Client_DISconnect_Event(Client_sl client)
	{
		this.client=client;
		EventManager.DOevent(this);
	}
}
