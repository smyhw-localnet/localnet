package online.smyhw.localnet.network;

import java.io.Serializable;
import java.net.Socket;
import java.util.Hashtable;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;
import online.smyhw.localnet.data.DataManager;
import online.smyhw.localnet.data.data;
import online.smyhw.localnet.event.*;
import online.smyhw.localnet.lib.TCP_LK;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;

public class Client_sl extends TCP_LK
{
	
	public  data ClientData = new data();
	public  data TempClientData = new data();
	
	public String ID;
	public Client_sl(Socket s)
	{
		super(s,1);//这里，调用父类构造方法
		new ClientConnect_Event(this);
		try
		{
			this.sendto("&"+LN.ID);//发送自身ID

		}catch(Exception e){message.info(" 客户端\""+ID+"\"鉴权时异常！丢弃线程"+e.getMessage());e.printStackTrace();return;}

	}
	
	public void sendto(String msg)
	{
		message.info("[网络动向]发送消息<"+msg+">至客户端<"+this.ID+">");
		Smsg(msg);
	}
	
	public void CLmsg(String msg)
	{
		if(ID==null && !(msg.startsWith("&")))
		{this.sendto("!1客户端，请先报告你的ID!");return;}
		message.info("[网络动向]接收到来自客户端<"+this.ID+">的消息<"+msg+">");
		LN.mdata(this, msg);
	}
	public void Serr_u(TCP_LK_Exception e)
	{
//		StackTraceElement[] temp=Thread.currentThread().getStackTrace();
//		StackTraceElement temp2=(StackTraceElement)temp[3];
//		message.info("客户端<"+this.ID+">连接出错，位置："+temp2.getFileName()+":"+temp2.getClassName()+":"+temp2.getMethodName()+":"+temp2.getLineNumber());
		NetWorkManager.doclient(0, this, 0);
		message.show("客户端<"+this.ID+">断开连接{"+e.type+"}:"+e.getMessage());
		DataManager.SaveData("./TerminalData/"+this.ID, ClientData);//保存数据
		new ClientDISconnect_Event(this);
		return;
	}
	
	public byte[] encryption(byte[] input,int type)
	{
		byte[] re=null;
		DataDecryptEvent temp1 = new DataDecryptEvent(input,type,this);
		re = temp1.output;
		if(temp1.Error) {return null;}
		return re;
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