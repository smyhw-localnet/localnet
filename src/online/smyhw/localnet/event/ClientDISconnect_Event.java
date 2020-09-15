package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

/**
 * 终端即将断开连接，但是仍未做任何操作前</br>
 * 可以在此处操作终端数据等</br>
 * 注意，此时到客户端的连接可能已经中断，任何尝试发送数据的操作都可能会导致不可预料的错误
 * @author smyhw
 *
 */
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
