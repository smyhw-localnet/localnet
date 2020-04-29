package online.smyhw.localnet.event;

import online.smyhw.localnet.network.Server_sl;

/**
 * 连接至服务器<b>之前</b>触发的事件
 * <br>此时，socket连接已经创建，但是还未发生任何握手数据
 * </br>成员变量server_sl指示当前事件的Server_sl
 * @author smyhw
 *
 */
public class ConnectServerEvent extends LN_Event 
{
	public Server_sl server_sl;
	public ConnectServerEvent(Server_sl ss)
	{
		this.EventName="ConnectServerEvent";//修改
		this.server_sl=ss;
		EventManager.DOevent(this);
	}
}
