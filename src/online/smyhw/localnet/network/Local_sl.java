package online.smyhw.localnet.network;

import java.util.ArrayList;

import online.smyhw.localnet.LN;

public class Local_sl extends Client_sl
{

	public Local_sl()
	{
		super("local",new ArrayList());
		this.remoteID=LN.ID;
	}
}
