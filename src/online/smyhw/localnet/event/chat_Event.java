package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

public class chat_Event 
{
	Client_sl User;
	String msg;
	public boolean Cancel = false;
	public chat_Event(Client_sl User,String msg)
	{
		this.User=User;
		this.msg=msg;
		EventManager.DOevent(this);
	}
}
