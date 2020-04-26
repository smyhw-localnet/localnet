package online.smyhw.localnet.event;

public class ExampleEvent extends LN_Event 
{
	public ExampleEvent()
	{
		this.EventName="ExampleEvent";//修改
		EventManager.DOevent(this);
	}
}
