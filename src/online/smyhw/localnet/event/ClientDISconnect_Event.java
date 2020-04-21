package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

public class ClientDISconnect_Event extends LN_Event
{
	Client_sl client;
	public ClientDISconnect_Event(Client_sl client)
	{
		this.EventName="ClientDISconnect";
		this.client=client;
		EventManager.DOevent(this);
	}
}
