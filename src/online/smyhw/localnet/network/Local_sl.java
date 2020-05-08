package online.smyhw.localnet.network;

import java.net.Socket;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;
import online.smyhw.localnet.event.DataDecryptEvent;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;

public class Local_sl extends Client_sl
{

	public Local_sl()
	{
		super(new Socket());
		this.ID=LN.ID;
	}
	
	public void CLmsg(String msg)
	{
		message.show(msg);
	}
	
	public void Smsg(String msg)
	{
		return;
	}
	
	public void sendto(String msg)
	{
		msg="[local-SL]"+msg;
		message.show(msg);
	}
	
	public void Serr_u(TCP_LK_Exception e) {}
	
	public synchronized String Rmsg() throws TCP_LK_Exception {return null;}

	public synchronized byte[] encryption(byte[] input,int type)
	{
		return input;
	}
}
