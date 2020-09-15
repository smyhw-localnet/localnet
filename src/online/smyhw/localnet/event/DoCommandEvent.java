package online.smyhw.localnet.event;

import online.smyhw.localnet.network.*;

/**
 * 在处理指令之前触发</br>
 * 
 * @author smyhw
 *
 */
public class DoCommandEvent extends LN_Event 
{
	public Client_sl user;
	public String command;
	public boolean Cancel = false;
	public DoCommandEvent(Client_sl user,String command)
	{
		this.user=user;
		this.command=command;
		this.EventName="DoCommandEvent";//修改
		EventManager.DOevent(this);
	}
	
	/**
	 * 取消此事件(这将导致该指令不会被执行)
	 */
	public void setCancel()
	{
		this.Cancel = true;
	}
}
