package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

public class ChatINFO_Event extends LN_Event
{
	public Client_sl From_User;
	public Client_sl To_User;
	public String msg;
	public boolean Cancel = false;
	public ChatINFO_Event(Client_sl From,Client_sl To,String msg)
	{
		this.EventName="ChatINFO";
		From_User=From;
		To_User=To;
		this.msg=msg;
		EventManager.DOevent(this);
	}

}
