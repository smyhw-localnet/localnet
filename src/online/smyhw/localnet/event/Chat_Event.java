package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

public class Chat_Event extends LN_Event
{
	public Client_sl User;
	public String msg;
	public boolean Cancel = false;
	public Chat_Event(Client_sl User,String msg)
	{
		this.EventName="Chat";
		this.User=User;
		this.msg=msg;
		EventManager.DOevent(this);
	}
}
