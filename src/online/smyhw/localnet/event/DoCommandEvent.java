package online.smyhw.localnet.event;

import online.smyhw.localnet.network.*;

public class DoCommandEvent extends LN_Event 
{
	public Client_sl user;
	public String command;
	public boolean Cancel = false;
	public DoCommandEvent(Client_sl user,String command)
	{
		this.user=user;
		this.command=command;
		this.EventName="ExampleEvent";//修改
		EventManager.DOevent(this);
	}
}
