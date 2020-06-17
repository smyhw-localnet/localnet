package online.smyhw.localnet.network.protocol;

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
import online.smyhw.localnet.network.Client_sl;

public class localnetTCP extends TCP_LK  implements StandardProtocol
{
	
	public Client_sl client; 
	
	public localnetTCP(List input)
	{
		super((Socket) input.get(0),1);//这里，调用父类构造方法
		this.client = (Client_sl) input.get(1);
		Begin();
	}
	
	public void Begin()
	{
		try
		{
			this.Smsg("{\"type\":\"auth\",\"ID\":\""+LN.ID+"\"}");//发送自身ID
		}catch(Exception e){message.info(" 终端\""+this.s.getInetAddress()+"\"鉴权时异常！丢弃线程"+e.getMessage());return;}
	}
	
	public void SendData(HashMap<String,String> input)
	{
		String send = Json.Create(input);
		Smsg(send);
	}
	
	public void CLmsg(String msg)
	{	
		this.client.CLmsg(msg);
	}
	public void Serr_u(TCP_LK_Exception e)
	{
		Disconnect();
		return;
	}
	
	public void Disconnect()
	{
		this.isERROR=true;
	}
	
	public byte[] encryption(byte[] input,int type)
	{
		byte[] re=null;
		DataDecryptEvent temp1 = new DataDecryptEvent(input,type,this);
		re = temp1.output;
		if(temp1.Error) {return null;}
		return re;
	}
	
}