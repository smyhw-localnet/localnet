package online.smyhw.localnet.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import online.smyhw.localnet.message;

public class EventManager 
{
	public static ArrayList<Method> ClientConnect_Listener = new ArrayList<Method>();
	public static ArrayList<Method> ClientDISconnect_Listener = new ArrayList<Method>();
	public static ArrayList<Method> Chat_Listener = new ArrayList<Method>();
	public static ArrayList<Method> ChatINFO_Listener = new ArrayList<Method>();
	public synchronized static void AddListener(String type,Method ff)
	{
		switch(type)
		{
			case "Client_connect":
				ClientConnect_Listener.add(ff);
				break;
			case "Client_disconnect":
				ClientDISconnect_Listener.add(ff);
				break;
			case "chat":
				Chat_Listener.add(ff);
				break;
			case "ChatINFO":
				ChatINFO_Listener.add(ff);
				break;
			default:
				message.warning("警告,"+type+"不是监听器类型！");
				return;
		}
	}
	
	/**
	 * 
	 * 该方法被弃用,不再产生任何作用,请勿调用!
	 * @param type
	 * @param ff
	 */
	public synchronized static void RemoveListener(String type,Method ff)
	{
		int a =1 ;if(a==1)return;
		switch(type)
		{
			case "Client_connect":
				ClientConnect_Listener.remove(ff);
				break;
			case "Client_disconnect":
				ClientDISconnect_Listener.remove(ff);
				break;
			case "chat":
				Chat_Listener.remove(ff);
				break;
			default:
				message.warning("警告,"+type+"不是监听器类型！");
				return;
		}
	}
	
	public synchronized static void DOevent(LN_Event dd)
	{
		ArrayList<Method> temp1 = null;
		switch(dd.GetEventName())
		{
		case "ClientConnect":
			temp1=(ArrayList<Method>)ClientConnect_Listener.clone();
			break;
		case "ClientDISconnect":
			temp1=(ArrayList<Method>)ClientDISconnect_Listener.clone();
			break;
		case "Chat":
			temp1=(ArrayList<Method>)Chat_Listener.clone();
			break;
		case "ChatINFO":
			temp1=(ArrayList<Method>)ChatINFO_Listener.clone();
			break;
		default:
			message.warning("警告:"+dd.GetEventName()+"执行事件时发现为未知事件名称");
		}
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
				message.error("完蛋警告！事件"+dd.GetEventName()+"在处理时出错！");
				e.printStackTrace();
			}
		}
	}

}
