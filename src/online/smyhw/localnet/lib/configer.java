package online.smyhw.localnet.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
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
	public configer(String filename)
	{
		file = new File(filename);
	}
	
	//除非成功写入，否则返回false
	//注意，key不能包含冒号(':')，否则直接返回false
	public boolean set(String key,int value)
	{
		message.info("1+"+file);
		key=key.trim();
		if(key.indexOf(':')!=-1) {return false;}
		try 
		{
			message.info("2");
			//判断这个键是否已经存在
			BufferedReader file_reader = new BufferedReader(new FileReader(file));
			message.info("22");
			int len = 0;//这个文件有多少行
			int bz = 0;//是否存在的标志，1为存在，0为不存在
			
			int wz=-1;//需要修改的位置
			while(true)//判断这个键是否已经存在
			{
				message.info("3");
				String temp_str =  file_reader.readLine();
				if(temp_str==null) {break;}//探测到文件末尾，标志置为0
				len=len+1;
				temp_str=temp_str.trim();
				String temp_str1[] = temp_str.split(":");
				if(temp_str1[0].equals(key)) {wz=len-1;bz=1;}//探测到和目标key一样的项目，标志置为1
			}
			message.info("4");
			file_reader.close();
			message.info("5");
			if(bz==1)//标志为1时将文件读进数组，然后替换键值
			{
				file_reader = new BufferedReader(new FileReader(file));
				String file_nr[] = new String[len];//创建文件内容数组
				int tbz = 0;//读取内容时的临时标志
				while(true)//将文件内容全部读进数组
				{
					message.info("55");
					String temp =file_reader.readLine();
					if(temp==null)//读到文件末尾，结束之 
					{file_reader.close();break;}
					file_nr[tbz] = temp;
					message.info(file_nr[tbz]);
					tbz=tbz+1;
				}
				message.info("6");
				for(int i=0;i<file_nr.length;i++)
				{
				System.out.println("QAQ+"+file_nr[i]);
				}
				
				//修改
				file_nr[wz]=key+":"+value;
				
				for(int i=0;i<file_nr.length;i++)
				{
				System.out.println("awa+"+file_nr[i]);
				}
				
				tbz=0;//重置标志
				BufferedWriter file_writer = new BufferedWriter(new FileWriter(file));
				message.info("66+"+file_nr.length);
				while(true)//将改过的数据写回去
				{
					message.info("666+"+file_nr[tbz]);
					file_writer.write(file_nr[tbz]);
					file_writer.newLine();
					if(tbz==file_nr.length-1)//写完数据
					{file_writer.close();break;}
					tbz++;
				}
				return true;
			}
			else//标志为0时，直接追加数据
			{
				message.info("7");
				BufferedWriter sc = new BufferedWriter(new  OutputStreamWriter(new FileOutputStream(file,true)));
//				sc.newLine();
				sc.write(key+":"+value);
//				sc.newLine();
				sc.close();
				return true;
			}
			
		}
		catch(Exception e)
		{
			message.warning(e.getLocalizedMessage());
			e.printStackTrace();
			return false;
		}
//		return false;
	}
	
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
