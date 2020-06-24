package online.smyhw.localnet.network;

import java.net.Socket;
import java.util.ArrayList;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.lib.Json;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;

public class Local_sl extends Client_sl
{

	public Local_sl()
	{
		super("local",new ArrayList());
		this.remoteID=LN.ID;
	}
}
