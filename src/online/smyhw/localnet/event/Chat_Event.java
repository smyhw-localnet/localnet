package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

/**
 * direction = true 
 * 本地发向远程
 * direction = false
 * 远程发向本地
 * 
 * remote可能为null，即本地终端发往本地终端，这时direction默认为false
 * 这是为了方便本地终端执行远程命令
 * 
 * @author smyhw
 *
 */
public class Chat_Event extends LN_Event
{
	public Client_sl remote;
	public String msg;
	public boolean Cancel = false;
	public boolean direction;
	public Chat_Event(Client_sl remote,boolean direction,String msg)
	{
		this.EventName="Chat";
		this.remote=remote;
		this.msg=msg;
		this.direction = direction;
		EventManager.DOevent(this);
	}
}
