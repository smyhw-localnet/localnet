package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

/**
 * 触发时机:</br>
 * 协议完成初始化，但尚未发送localnet握手包</br>
 * 换而言之,此时该终端还未在localnet注册</br>
 * ---</br>
 * 此时，该终端ID未知,也不在localnet的
 * @author smyhw
 *
 */
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
