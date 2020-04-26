package online.smyhw.localnet.event;

public class DataDecryptEvent extends LN_Event 
{
	public byte[] input,output;
	public DataDecryptEvent(byte[] input)
	{
		this.EventName="DataDecrypt";
		this.input=input;
		EventManager.DOevent(this);
	}
}
