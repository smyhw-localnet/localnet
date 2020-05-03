package online.smyhw.localnet.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import online.smyhw.localnet.message;

/**
 * 
 * 一个配置文件对应一个configer实例
 * @author smyhw
 *
 */
public class configer 
{
	File file;
	HashMap<String,String> configMAP = new HashMap<String,String>();
	public configer(String filename)
	{
		file = new File(filename);
		if(!file.exists())
		{
			try
			{
				if(file.createNewFile()) 
				{
					
				}
				else
				{
					message.error("严重警告！配置文件\""+file.getPath()+"\""+"不存在且创建失败！配置表将为空！");
				}
			}
			catch (Exception e)
			{
				message.error("严重警告！配置文件\""+file.getPath()+"\""+"不存在且创建失败(发生异常)！配置表将为空！");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * 放弃内存中的配置表，从磁盘中重新读取
	 * @return 如果返回false，则读取失败！
	 * 
	 */
	public boolean reload()
	{
		boolean re = false;
		try
		{
			HashMap<String,String> configMAP = new HashMap<String,String>();
			while(true)
			{
				
				
			}
		}
		catch(Exception e)
		{
			message.error("严重警告！重载配置表失败！");
		}
		return re;
	}
	
	public boolean set(String key,String value)
	{
		boolean re =false;
		key=key.trim();
		if(key.indexOf(':')!=-1) {message.warning("一个配置项目写入失败！键中含有非法字符':'");return false;}
		
		return re;
	}
	public boolean set(String key ,int value)
	{return set(key,value+"");}
	public boolean set(String key,boolean value)
	{return set(key,value+"");}
	
	//返回0时有可能是出错了XD
	//如果key中有冒号，则直接返回0
	public int get(String key)
	{
		key=key.trim();
		if(key.indexOf(':')!=-1) {return 0;}
		try
		{
			BufferedReader sc = new BufferedReader(new FileReader(file));
			while(true)
			{
				String temp = sc.readLine();
				if(temp==null) {sc.close();return 0;}
				if(temp.startsWith(key+":")) 
				{
					String temp1[] = temp.split(":");
					int re=Integer.parseInt(temp1[1]);
					sc.close();
					return re;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
//		return 0;
	}
}
