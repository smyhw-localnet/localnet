package online.smyhw.localnet.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import online.smyhw.localnet.message;

public class EventManager 
{
	public static Hashtable<String,CopyOnWriteArrayList <Method>> EventMap = new Hashtable<String,CopyOnWriteArrayList <Method>>();
	
	public synchronized static void AddListener(String type,Method ff)
	{
		List<Method>ListenerList = EventMap.get(type);
		if(ListenerList==null) {ListenerList = new CopyOnWriteArrayList<Method>();}
		ListenerList.add(ff);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized static void DOevent(LN_Event dd)
	{
		List<Method>ListenerList = EventMap.get(dd.EventName);
		if(ListenerList==null) {return;}
		Iterator<Method> temp2 = ListenerList.iterator();
		while(temp2.hasNext())
		{
			Method temp3 = temp2.next();
			try
			{
				temp3.invoke(null,dd);
			} 
			catch (Exception e)
			{
				message.warning("完蛋警告！事件"+dd.GetEventName()+"在处理时出错！",e);
			}
		}
	}

}
