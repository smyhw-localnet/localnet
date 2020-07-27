package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

/**
 * 
 * 在一个客户端完成握手后</br>
 * 此时客户端已经走完协议握手流程，是一个正常通信的客户端
 * @author smyhw
 *
 */
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
