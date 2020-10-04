package online.smyhw.localnet.event;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import online.smyhw.localnet.message;

public class EventManager 
{
	public static Hashtable<String,CopyOnWriteArrayList <Method>> EventMap = new Hashtable<String,CopyOnWriteArrayList <Method>>();
	
	/**
	 * 添加事件监听者</br>
	 * 
	 * @param type 事件名称
	 * @param ff  监听的方法
	 */
	public synchronized static void AddListener(String type,Method ff)
	{
		CopyOnWriteArrayList<Method>ListenerList = EventMap.get(type);
		if(ListenerList==null) {ListenerList = new CopyOnWriteArrayList<Method>();}
		ListenerList.add(ff);
		EventMap.put(type, ListenerList);
	}
	
	/**
	 * 事件总线，处理发送的事件</br>
	 * 
	 * @param dd 要处理的事件
	 */
	@SuppressWarnings("unchecked")
	public static void DOevent(LN_Event dd)
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
				message.warning("事件<"+dd.GetEventName()+">在交由方法<"+temp3.getName()+">处理时出错！",e);
			}
		}
	}

}
