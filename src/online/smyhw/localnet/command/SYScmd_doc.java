package online.smyhw.localnet.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;
import online.smyhw.localnet.data.DocManager;
import online.smyhw.localnet.lib.CommandFJ;
import online.smyhw.localnet.network.Client_sl;

public class SYScmd_doc 
{
	public static void cmd(Client_sl User,String cmd) 
	{
		String doc_path;
		if(CommandFJ.js(cmd)<2) {doc_path="index";}
		else {doc_path=CommandFJ.fj(cmd, 1);}
		String re = DocManager.getDoc(doc_path);
		User.sendMsg(re);
	}
}
