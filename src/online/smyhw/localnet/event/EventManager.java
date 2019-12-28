package online.smyhw.localnet.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import online.smyhw.localnet.message;

public class EventManager 
{
	public static ArrayList<Method> Client_connect_Listener = new ArrayList<Method>();
	public static ArrayList<Method> Client_DISconnect_Listener = new ArrayList<Method>();
	public static ArrayList<Method> chat_Listener = new ArrayList<Method>();
	public synchronized static void AddListener(String type,Method ff)
	{
		switch(type)
		{
			case "Client_connect":
				Client_connect_Listener.add(ff);
				break;
			case "Client_disconnect":
				Client_DISconnect_Listener.add(ff);
				break;
			case "chat":
				chat_Listener.add(ff);
				break;
			default:
				message.warning("警告,"+type+"不是监听器类型！");
				return;
		}
	}
	public synchronized static void RemoveListener(String type,Method ff)
	{
		switch(type)
		{
			case "Client_connect":
				Client_connect_Listener.remove(ff);
				break;
			case "Client_disconnect":
				Client_DISconnect_Listener.remove(ff);
				break;
			case "chat":
				chat_Listener.remove(ff);
				break;
			default:
				message.warning("警告,"+type+"不是监听器类型！");
				return;
		}
	}
	
	public synchronized static void DOevent(Client_DISconnect_Event dd)
	{
		ArrayList<Method> temp1 = (ArrayList<Method>) Client_DISconnect_Listener.clone();
		Iterator<Method> temp2 = temp1.iterator();
		while(temp2.hasNext())
		{
			Method temp3 = temp2.next();
			try
			{
				temp3.invoke(null,dd);
			} 
			catch (Exception e)
			{
				message.error("完蛋警告！一个事件在处理时出错！");
				e.printStackTrace();
			}
		}
	}
	public synchronized static void DOevent(Client_connect_Event dd)
	{
		ArrayList<Method> temp1 = (ArrayList<Method>) Client_connect_Listener.clone();
		Iterator<Method> temp2 = temp1.iterator();
		while(temp2.hasNext())
		{
			Method temp3 = temp2.next();
			try
			{
				temp3.invoke(null,dd);
			} 
			catch (Exception e)
			{
				message.error("完蛋警告！一个事件在处理时出错！");
				e.printStackTrace();
			}
		}
		
	}
	public synchronized static void DOevent(chat_Event dd)
	{
		ArrayList<Method> temp1 = (ArrayList<Method>) chat_Listener.clone();
		Iterator<Method> temp2 = temp1.iterator();
		while(temp2.hasNext())
		{
			Method temp3 = temp2.next();
			try
			{
				temp3.invoke(null,dd);
			} 
			catch (Exception e)
			{
				message.error("完蛋警告！一个事件在处理时出错！");
				e.printStackTrace();
			}
		}
		
	}
}
