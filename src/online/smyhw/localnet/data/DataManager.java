package online.smyhw.localnet.data;

import java.io.File;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.io.*;

import online.smyhw.localnet.message;

public class DataManager 
{
	/**
	 * 
	 * 将一个config实例保存到指定位置
	 * @param URL 需要保存到的文件位置(例如:"./configs/awa.config",不包括双引号)
	 * @param data 需要保存的config实例
	 * @return 若保存成功,返回true,否则返回false;
	 * 
	 */
	public static boolean SaveConfig(String URL,config data)
	{
		File file = new File(URL);
		try 
		{
			if(!file.exists()) {file.createNewFile();}
			List<String> flist = Files.readAllLines(Paths.get(URL));
			HashMap<String,String> temp1 = (HashMap<String, String>) data.main_data.clone();
			temp1.forEach
			(
				(key, value) -> 
				{
					try
					{
						boolean temp3=false;//标识下面的for循环是否匹配到正确的行
					    for (String str : flist) 
					    {
					    	String temp2[] = str.split("=");
					    	if(temp2.length!=2) {continue;}
					    	if(temp2[0].equals(key)) 
					    	{
					    		str=key+"="+value;
					    		temp3=true;
					    		break;
					    	}
					    }
					    if(!temp3) //如果以上的for循环没有匹配到正确行，说明该配置在原配置文件中不存在，则在最后追加
					    {
					    	flist.add(key+"="+value);
					    }
					}
					catch(Exception e)
					{
						message.warning("配置条目<"+key+"="+value+">写入失败，请视情况自行写入！");
					}
				}
			);
			//遍历完hashmap，将处理过的list写会=回文件里
			PrintWriter temp4 = new PrintWriter(file);
		    for (String str : flist) 
		    {
		    	temp4.println(str);
		    }
		    temp4.close();
			return true;
		} 
		catch (Exception e) 
		{
			message.warning("警告!DataManager类SaveConfig时方法出错!",e);
			return false;
		}
//		return false;
	}
	
	/**
	 * 
	 * 从指定的文件读取config
	 * @param URL 文件位置(例如:"./configs/awa.config",不包括双引号)
	 * @return 读取出来的config实例,若文件不存在或读取出错,会返回一个空的config实例
	 * 
	 */
	public static config LoadConfig(String URL)
	{
		File file = new File(URL);
		if(!file.exists())
		{
			message.info("文件"+URL+"不存在,LoadConfig方法将返回空实例");
			return new config();
		}
		config re = new config();
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			while(true)
			{
				String temp1 = br.readLine();
				message.info("[config]+"+temp1);
				if(temp1==null) {break;}
				if(temp1.startsWith("//")||temp1.startsWith("#")){continue;}//如果是注释，则跳过
				String temp2[] = temp1.split("=");
				if(temp2.length<2) 
				{
					message.info("config行\""+temp1+"\"读取不到分隔符\"=\",跳过行");
					continue;
				}
				if(temp2.length>2) 
				{
					message.info("config行\""+temp1+"\"读取到过多的分隔符\"=\",跳过行");
					continue;
				}
				re.set(temp2[0], temp2[1]);
			}
			br.close();
			return re;
		}
		catch (Exception e) 
		{
			message.info("文件"+URL+"读取出错,LoadConfig方法将返回空实例,错误如下");
			e.printStackTrace();
			return new config();
		}
//		return null;
	}
	
	public static boolean SaveData(String URL,String data)
	{
		File file = new File(URL);
		ObjectOutputStream oos;
		try 
		{
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(data);
			oos.close();
			return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;
		}

//		return false;
	}
	
	public static data LoadData(String URL)
	{
		File file = new File(URL);
		ObjectInputStream ois;
		try 
		{
			ois = new ObjectInputStream(new FileInputStream(file));
			data re  = (data) ois.readObject();
			ois.close();
			return re;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return new data();
		}
//		return new data();
	}
}
