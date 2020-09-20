package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Client_sl;

/**
 * 触发时机:</br>
 * 协议完成初始化，但尚未发送localnet握手包</br>
 * 换而言之,此时该终端还未在localnet注册</br>
 * ---</br>
 * 此时，该终端ID未知,也不在localnet的用户列表中，亦不可被Find_Client找到，SendAll也不会选中它</br>
 * 但是此时可以获得其Client_sl实例，并且因为协议初始化已经完成，所以可以进行发送信息等操作
 * @author smyhw
 *
 */
public class ClientConnect_Event extends LN_Event
{
	Client_sl client;
	boolean Cancel = false;
	public ClientConnect_Event(Client_sl client)
	{
		this.EventName="ClientConnect";
		this.client=client;
		EventManager.DOevent(this);
	}
	
	public void setCancel()
	{
		this.Cancel = true;
	}
	
	public boolean getCancel()
	{
		return this.Cancel;
	}
	
	public Client_sl getClient()
	{
		return this.client;
	}
}
