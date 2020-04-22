package online.smyhw.localnet.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;
import online.smyhw.localnet.lib.CommandFJ;
import online.smyhw.localnet.network.Client_sl;

public class SYScmd_doc 
{
	public static void cmd(Client_sl User,String cmd) 
	{
		String doc_path;
		if(CommandFJ.js(cmd)<2) {doc_path="index";}
		else {doc_path=CommandFJ.fj(cmd, 1);}
		BufferedReader br;
		try {br = new BufferedReader(new InputStreamReader(LN.class.getResourceAsStream("/data/doc/"+doc_path),"utf-8"));} 
		catch (UnsupportedEncodingException e) 
		{
			message.warning("doc指令执行时出错，解码doc错误！", e);
			return;
		}
		catch(NullPointerException e)
		{
			message.warning("[doc]:文档不存在！（查看 /doc help）");
			return;
		}
		
		try 
		{
			for(String temp="<localnet>文档:";temp!=null;temp=br.readLine())
			{
				User.sendto(temp);
			}
		} 
		catch (IOException e) 
		{
			message.warning("读取文档时发生IO异常！", e);
		}
	}
}
