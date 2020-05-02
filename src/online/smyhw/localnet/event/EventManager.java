package online.smyhw.localnet.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import online.smyhw.localnet.message;

public class EventManager 
{
	public static ArrayList<Method> ClientConnect_Listener = new ArrayList<Method>();
	public static ArrayList<Method> ClientConnected_Listener = new ArrayList<Method>();
	public static ArrayList<Method> ClientDISconnect_Listener = new ArrayList<Method>();
	public static ArrayList<Method> Chat_Listener = new ArrayList<Method>();
	public static ArrayList<Method> ChatINFO_Listener = new ArrayList<Method>();
	public static ArrayList<Method> DataDecrypt_Listener = new ArrayList<Method>();
	public static ArrayList<Method> ExampleEvent_Listener = new ArrayList<Method>();//添加监听器列表
	public static ArrayList<Method> ConnectServerEvent_Listener = new ArrayList<Method>();
	public static ArrayList<Method> DoCommandEvent_Listener = new ArrayList<Method>();
	public synchronized static void AddListener(String type,Method ff)
	{
		switch(type)
		{
			case "Client_connect":
				ClientConnect_Listener.add(ff);
				break;
			case "Client_connected":
				ClientConnected_Listener.add(ff);
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
			case "DataDecrypt":
				DataDecrypt_Listener.add(ff);
				break;
			case "ExampleEvent":
				ExampleEvent_Listener.add(ff);//添加
				break;
			case "ConnectServerEvent":
				ConnectServerEvent_Listener.add(ff);
				break;
			case "DoCommandEvent":
				DoCommandEvent_Listener.add(ff);
				break;
			default:
				message.warning("警告,"+type+"不是监听器类型！");
				return;
		}
	}
	

	
	@SuppressWarnings("unchecked")
	public synchronized static void DOevent(LN_Event dd)
	{
		ArrayList<Method> temp1 = null;
		switch(dd.GetEventName())
		{
		case "ClientConnect":
			temp1=(ArrayList<Method>)ClientConnect_Listener.clone();
			break;
		case "ClientConnected":
			temp1=(ArrayList<Method>)ClientConnected_Listener.clone();
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
		case "DataDecrypt":
			temp1=(ArrayList<Method>)DataDecrypt_Listener.clone();
			break;
		case "ExampleEvent"://添加
			temp1=(ArrayList<Method>)ExampleEvent_Listener.clone();
			break;
		case "ConnectServerEvent":
			temp1=(ArrayList<Method>)ConnectServerEvent_Listener.clone();
			break;
		case "DoCommandEvent":
			temp1=(ArrayList<Method>)DoCommandEvent_Listener.clone();
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
