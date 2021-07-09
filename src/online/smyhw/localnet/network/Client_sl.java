package online.smyhw.localnet.network;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.LNlib;
import online.smyhw.localnet.message;
import online.smyhw.localnet.data.DataManager;
import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.data.data;
import online.smyhw.localnet.event.*;
import online.smyhw.localnet.lib.Json;
import online.smyhw.localnet.lib.Exception.Json_Parse_Exception;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;
import online.smyhw.localnet.network.protocol.*;
import online.smyhw.localnet.plugins.PluginsManager;

import java.net.Socket;

public class Client_sl
{
	
	public  data ClientData = new data();
	public  data TempClientData = new data();
	
	public String remoteID;
	public String localID = LN.ID;
	public String protocolType;
	public StandardProtocol protocolClass;
	boolean isReady = false;//该项目指示本Client_sl是否初始化完成(一般会卡在处理ClientConnect_Event这边的样子)
	public ArrayList<DataPack> reReadyMsg = new ArrayList<DataPack>();//存储在初始化完成之前收到的数据，某些事件的处理器可能会用到
	public Client_sl(String protocol,List args)
	{
		Class PrC;
		//协议选择器
		try 
		{
			PrC = Class.forName(protocol,false, PluginsManager.cloader);
		}
		catch (ClassNotFoundException e1) 
		{
			try 
			{
				PrC = Class.forName("online.smyhw.localnet.network.protocol."+protocol);
			}
			catch (ClassNotFoundException e) 
			{
				message.warning("一个协议类型未知的客户端被拒绝创建{"+protocol+"}");
				return;
			}
		}
		try 
		{
			protocolClass = (StandardProtocol) PrC.getConstructors()[0].newInstance(args,this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			message.warning("网络协议{"+protocol+"}初始化异常",e);
			return;
		}
		this.protocolType = protocol;
		if(new ClientConnect_Event(this).getCancel())
		{
			this.Disconnect("事件被取消");
			return;
		}
		
		this.isReady=true;
		//就绪后应处理积压的消息
		System.out.println("reReadyMsg:"+reReadyMsg.size());
		while(!reReadyMsg.isEmpty())
		{
			CLmsg(reReadyMsg.get(0));
			reReadyMsg.remove(0);
		}
		try {this.sendData(new DataPack("{\"type\":\"auth\",\"ID\":\""+localID+"\"}"));} catch (Json_Parse_Exception e) {e.printStackTrace();}//这不该出现异常
	}
	
	/**
	 * 向该客户端发送信息
	 * @param msg
	 */
	public void sendMsg(String msg)
	{
		HashMap<String,String> Hmsg = new HashMap<String,String>();
		Hmsg.put("type", "message");
		Hmsg.put("message", msg);
		String send = Json.Create(Hmsg);
		message.info("[终端]发送字符串消息<"+send+">至终端<"+this.remoteID+">");
		sendData(new DataPack(Hmsg));
	}
	
	/**
	 * 发送数据包信息
	 * @param input
	 */
	public void sendData(DataPack input)
	{
//		String send = Json.Create(input);
		message.info("[终端]发送数据包消息<"+input.getStr()+">至终端<"+this.remoteID+">");
		protocolClass.SendData(input);
	}
	
	public void sendNote(String NoteType,String noteMsg)
	{
		HashMap<String,String> send = new HashMap<String,String>();
		send.put("type", "note");
		send.put("NoteType", NoteType);
		send.put("NoteText", noteMsg);
		this.sendData(new DataPack(send));
	}
	
	public void CLmsg(DataPack re)
	{	
		if(!this.isReady)
		{//本Client_sl还未初始化完成，不向外部传送信息
			this.reReadyMsg.add(re);
			return;
		}
		message.info("[终端]接收到来自客户端<"+this.remoteID+">的消息<"+re.getStr()+">");
		if(!LNlib.CheckMapNode(re.getMap()))
		{
			message.info("[终端]来自终端<"+this.remoteID+">的消息缺少必要消息节点");
			this.sendNote("4", "消息缺失必要节点");
			return;
		}
		if(remoteID==null && !re.getValue("type").equals("auth")){this.sendNote("1","请先报告你的ID");return;}
		if(!this.libCLmsg(re))
		{
			LN.mdata(this, re);
		}
	}
	
	/**
	 * 作为本类唯一一个回调函数，CLmsg需要有一个用以覆盖的拓展
	 * @param re 数据包
	 * @return 如果为true，该消息不会继续被localnet处理
	 */
	public boolean libCLmsg(DataPack re)
	{
		return false;
	}
	
	public void Serr_u(TCP_LK_Exception e)
	{
		this.lib_Serr_u(e);
		Disconnect(e.type+"->"+e.getMessage());
		return;
	}
	
	/**
	 * 用于当此类作为lib使用时，处理错误<br>
	 * 当此方法被执行时，连接还没有断开
	 * @param e 发生的错误
	 * @return 返回值暂时没有意义，默认为false
	 */
	public boolean lib_Serr_u(TCP_LK_Exception e)
	{
		return false;
	}
	
	public void Disconnect(String msg)
	{
		new ClientDISconnect_Event(this);
		NetWorkManager.doclient(0, this, 0);
		message.show("[终端]终端<"+this.remoteID+">断开连接{"+msg+"}");
		DataManager.SaveData("./TerminalData/"+this.remoteID, ClientData);//保存数据
		this.protocolClass.Disconnect();
	}
	
	//ClientData存取方法
	
	/**
	 * 从ClientData中获取相应的信息
	 * @param pluginID 插件ID；当然，您也可以访问其他插件的数据
	 * @param DataID 信息ID
	 * @return 如果信息ID不存在，则返回null；如果信息存在，则返回信息
	 */
	public Object GetClientData(String PluginID,String DataID) 
	{
		Hashtable<String,Object> PluginData = (Hashtable<String,Object>)ClientData.get(PluginID);
		if(PluginData==null) {return null;}
		Object re = PluginData.get(DataID);
		return re;
	}
	
	/**
	 * 向ClientData里存放指定的信息，如果信息已经存在，则覆盖</br>
	 * 注意，你不能存储<b>不能序列化</b>的数据，如果需要存储这些数据，请使用TempClientData系列方法(PutTempClientData/GetTempClientData)</br>
	 * 与TempClientData不同，这里的数据在客户端断开重连后仍然存在，关闭或重启服务器也不会影响这里的数据，他们会被存储到硬盘上</br>
	 * (强制中断服务器进程可能会导致这里的数据无法及时保存)</br>
	 * @param pluginID 插件ID；当然，您也可以访问其他插件的数据
	 * @param DataID 信息ID
	 * @param Data 需要存放的信息
	 * @return 如果存储失败(存储的数据不可序列化),则返回false
	 */
	public Boolean PutClientData(String PluginID,String DataID,Object Data)
	{
		if(Data instanceof Serializable) {}
		else
		{
			message.warning("插件<"+PluginID+">尝试存储不能序列化的数据！");
			return false;
		}
		Hashtable<String,Object> PluginData = (Hashtable<String,Object>)ClientData.get(PluginID);
		if(PluginData==null) 
		{//该插件ID从未创建过信息，创建HashMap
			ClientData.set(PluginID, new Hashtable<String,Object>());
			PluginData = (Hashtable<String,Object>)ClientData.get(PluginID);
		}
		PluginData.put(DataID,Data);
		return true;
	}
	
	//TempClientData存取方法
	
	/**
	 * 从TempClientData中获取相应的信息
	 * @param pluginID 插件ID；当然，您也可以访问其他插件的数据
	 * @param DataID 信息ID
	 * @return 如果信息ID不存在，则返回null；如果信息存在，则返回信息
	 */
	public Object GetTempClientData(String PluginID,String DataID) 
	{
		Hashtable<String,Object> PluginData = (Hashtable<String,Object>)TempClientData.get(PluginID);
		if(PluginData==null) {return null;}
		Object re = PluginData.get(DataID);
		return re;
	}
	
	/**
	 * 向TempClientData里存放指定的信息，如果信息已经存在，则覆盖</br>
	 * 注意，这里的信息将在客户端断开后被清空
	 * @param pluginID 插件ID；当然，您也可以访问其他插件的数据
	 * @param DataID 信息ID
	 * @param Data 需要存放的信息
	 */
	public void PutTempClientData(String PluginID,String DataID,Object Data)
	{
		Hashtable<String,Object> PluginData = (Hashtable<String,Object>)TempClientData.get(PluginID);
		if(PluginData==null) 
		{//该插件ID从未创建过信息，创建HashMap
			TempClientData.set(PluginID, new Hashtable<String,Object>());
			PluginData = (Hashtable<String,Object>)TempClientData.get(PluginID);
		}
		PluginData.put(DataID,Data);
		return;
	}
	
}