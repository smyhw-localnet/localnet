package online.smyhw.localnet.event;

import online.smyhw.localnet.lib.TCP_LK;
import online.smyhw.localnet.network.Client_sl;

/**
 * 
 * 在使用localnetTCP的情况下，发送和接收数据包将触发此事件</br>
 * 该事件可以使localnetTCP协议支持加密
 * @param type 该值为0时解密,为1时加密
 * @author smyhw
 *
 */
public class DataDecryptEvent extends LN_Event 
{
	public byte[] input,output;
	public int Decrypt_type;
	public Client_sl cl;
	public boolean Error = false;
	public DataDecryptEvent(byte[] input,int type,Client_sl cl)
	{
		this.EventName="DataDecrypt";
		this.input=input;
		this.output=input;
		this.cl = cl;
		this.Decrypt_type=type;
		EventManager.DOevent(this);
	}
}
