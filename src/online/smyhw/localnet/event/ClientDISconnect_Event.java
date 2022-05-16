package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

/**
 * 终端已经断开连接</br>
 * 可以在此处操作终端数据等，但是请勿尝试涉及收发数据的操作</br>
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
