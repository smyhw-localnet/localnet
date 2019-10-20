package online.smyhw.localnet.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

public class configer 
{
	File file;
	public configer(String filename)
	{
		file = new File(filename);
	}
	
	//除非成功写入，否则返回false
	//注意，key不能包含冒号(':')，否则直接返回false
	public boolean set(String key,int value)
	{
		if(key.indexOf(':')!=-1) {return false;}
		try 
		{
			FileWriter sc = new FileWriter(file);
			sc.write(key+":"+value);
			sc.close();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
//		return false;
	}
	
	//返回0时有可能是出错了XD
	//如果key中有冒号，则直接返回0
	public int get(String key)
	{
		if(key.indexOf(':')!=-1) {return 0;}
		try
		{
			BufferedReader sc = new BufferedReader(new FileReader(file));
			while(true)
			{
				String temp = sc.readLine();
				if(temp==null) {return 0;}
				if(temp.startsWith(key+":")) 
				{
					String temp1[] = temp.split(":");
					int re=Integer.parseInt(temp1[1]);
					return re;
				}
			}
		}
		catch(Exception e)
		{
			return 0;
		}
//		return 0;
	}
}
