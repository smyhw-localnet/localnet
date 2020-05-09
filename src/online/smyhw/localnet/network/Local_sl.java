package online.smyhw.localnet.network;

import java.net.Socket;
import java.util.HashMap;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;
import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.lib.Json;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;

public class Local_sl extends Client_sl
{

	public Local_sl()
	{
		super(new Socket());
		this.ID=LN.ID;
	}
	
	public void Smsg(String msg)
	{
		DataPack temp = new DataPack(Json.Parse(msg));
		if(temp.getValue("type").equals("message"))
		{
			temp.add("type", "forward_message");
			temp.add("From", "本地信息");
		}
		LN.mdata(this, temp);
	}
	
	public void Serr_u(TCP_LK_Exception e) {}
	
	public synchronized String Rmsg() throws TCP_LK_Exception {return null;}

	public synchronized byte[] encryption(byte[] input,int type)
	{
		return input;
	}
}
