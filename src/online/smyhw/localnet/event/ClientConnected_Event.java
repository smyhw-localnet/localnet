package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

public class ClientConnected_Event extends LN_Event
{
	Client_sl client;
	public ClientConnected_Event(Client_sl client)
	{
		this.EventName="ClientConnected";
		this.client=client;
		EventManager.DOevent(this);
	}
}
