package online.smyhw.localnet.network;

import java.io.Serializable;
import java.net.Socket;
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
import online.smyhw.localnet.lib.TCP_LK;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;
import online.smyhw.localnet.network.protocol.*;

public class Client_sl
{
	
	public  data ClientData = new data();
	public  data TempClientData = new data();
	
	public String remoteID;
	public String protocolType;
	public StandardProtocol protocolClass;
	public Client_sl(String protocol,List args)
	{
		switch(protocol)
		{
		case "localnetTCP":
			protocolClass = new localnetTCP(args,this);
			break;
		case "local":
			protocolClass = new local(args,this);
			break;
		default:
			message.warning("一个协议类型未知的客户端被拒绝创建");
			return;
		}
		new ClientConnect_Event(this);
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
		message.info("[网络动向]发送消息<"+send+">至终端<"+this.remoteID+">");
		sendData(Hmsg);
	}
	
	/**
	 * 发送原始HashMap信息
	 * @param input
	 */
	public void sendData(HashMap<String,String> input)
	{
//		String send = Json.Create(input);
		message.info("[网络动向]发送消息<"+input.toString()+">至终端<"+this.remoteID+">");
		protocolClass.SendData(input);
	}
	
	public void sendNote(String NoteType,String noteMsg)
	{
		HashMap<String,String> send = new HashMap<String,String>();
		send.put("type", "note");
		send.put("NoteType", NoteType);
		send.put("NoteText", noteMsg);
		this.sendData(send);
	}
	
	public void CLmsg(HashMap<String,String> re)
	{	
		message.info("[网络动向]接收到来自客户端<"+this.remoteID+">的消息<"+re.toString()+">");
		if(!LNlib.CheckMapNode(re))
		{
			message.info("接收到来自客户端<"+this.remoteID+">的消息缺少必要消息节点");
			this.sendNote("4", "消息缺失必要节点");
			return;
		}
		if(remoteID==null && !re.get("type").equals("auth")){this.sendNote("1","客户端，请先报告你的ID!");return;}
		LN.mdata(this, new DataPack(re));
	}
	public void Serr_u(TCP_LK_Exception e)
	{
		Disconnect(e.type+"->"+e.getMessage());
		return;
	}
	
	public void Disconnect(String msg)
	{
		NetWorkManager.doclient(0, this, 0);
		message.show("客户端<"+this.remoteID+">断开连接{"+msg+"}");
		DataManager.SaveData("./TerminalData/"+this.remoteID, ClientData);//保存数据
		this.protocolClass.Disconnect();
		new ClientDISconnect_Event(this);
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