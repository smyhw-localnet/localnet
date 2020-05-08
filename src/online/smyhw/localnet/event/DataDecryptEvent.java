package online.smyhw.localnet.event;

import online.smyhw.localnet.lib.TCP_LK;

public class DataDecryptEvent extends LN_Event 
{
	public byte[] input,output;
	public int Decrypt_type;
	public TCP_LK tcp_lk;
	public boolean Error = false;
	public DataDecryptEvent(byte[] input,int type,TCP_LK tcp_lk)
	{
		this.EventName="DataDecrypt";
		this.input=input;
		this.output=input;
		this.tcp_lk = tcp_lk;
		this.Decrypt_type=type;
		EventManager.DOevent(this);
	}
}
