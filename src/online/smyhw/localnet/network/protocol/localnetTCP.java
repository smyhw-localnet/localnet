package online.smyhw.localnet.network.protocol;

import java.net.Socket;
import java.util.List;

import online.smyhw.localnet.message;
import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.event.*;
import online.smyhw.localnet.lib.TCP_LK;
import online.smyhw.localnet.lib.Exception.Json_Parse_Exception;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;
import online.smyhw.localnet.network.Client_sl;

public class localnetTCP extends TCP_LK  implements StandardProtocol
{
	
	public Client_sl client; 
	
	/**
	 * list参数表</br>
	 * 1. socket实例 </br>
	 * 2. 参数为1作为服务端，2则为客户端(该项仅影响心跳包收发)</br>
	 * @param input
	 * @param sy
	 */
	public localnetTCP(List input,Client_sl sy)
	{
		super((Socket) input.get(0),input.size()>1 ? (int) input.get(1) : 1);//这里，调用父类构造方法
		this.client = sy;
		
//		try
//		{//发送自身ID
//			if(input.size()>2) {this.Smsg("{\"type\":\"auth\",\"ID\":\""+input.get(2)+"\"}");}
//			else {this.Smsg("{\"type\":\"auth\",\"ID\":\""+LN.ID+"\"}");}
//		}catch(Exception e){message.info(" 终端\""+this.s.getInetAddress()+"\"鉴权时异常！丢弃线程"+e.getMessage());return;}
	}
	
	public void SendData(DataPack input)
	{
		String send = input.getStr();
		Smsg(send);
	}
	
	public void CLmsg(String msg)
	{
		//这里要将接收到的信息以HashMap的形式回传给上游
		DataPack re;
		try 
		{
			re = new DataPack(msg);
		}
		catch (Json_Parse_Exception e)
		{
			message.warning("客户端发送了无法解析的信息");
			return;
		}
		this.client.CLmsg(re);
	}
	public void Serr_u(TCP_LK_Exception e)
	{
		this.client.Serr_u(e);
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