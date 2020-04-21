package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

public class ClientConnect_Event extends LN_Event
{
	Client_sl client;
	public ClientConnect_Event(Client_sl client)
	{
		this.EventName="ClientConnect";
		this.client=client;
		EventManager.DOevent(this);
	}
}
