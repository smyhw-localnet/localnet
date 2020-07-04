package online.smyhw.localnet.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;

/**
 * 该类用于读取文档内容
 * @author smyhw
 *
 */
public class DocManager 
{
	static HashMap<String,String> RunTimeDoc = new HashMap<String,String>();
	/**
	 * 从指定JAR路径获取文档文本
	 * @param path文档路径
	 * @return 文档文本,若文档不存在或读取出错，返回null
	 */
	public static String readFromJar(String path)
	{
		String re = "";
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(LN.class.getResourceAsStream(path),"utf-8"));
			while(true)
			{
				String temp1 = br.readLine();
				if(temp1==null) {break;}
				re = re +"\n"+temp1;
			}
		}catch (Exception e) 
		{
			message.warning("DocManager读取JAR中文档错误", e);
			return null;
		}
		return re;
	}
	
	/**
	 * 从指定文件路径获得文档文本
	 * @param path 文件路径
	 * @return 文档文本,若文档不存在，返回null
	 */
	public static String readFromFile(String path)
	{
		String re ="";
		try 
		{
			BufferedReader temp1 = new BufferedReader(new FileReader(path));
			while(true)
			{
				String temp2 = temp1.readLine();
				if(temp2==null) {break;}
				re = re + "\n" + temp2;
			}
		}
		catch (Exception e) 
		{
			message.warning("从文件<"+path+">中读取文档出错", e);
			return null;
		}
		return re;
	}
	
	/**
	 * 根据给定的名称获得文档
	 * @param path 文档名称
	 * @return 文档文本,若文档不存在，返回null
	 */
	public static String readFromRunTime(String path)
	{
		return RunTimeDoc.get(path);
	}
	
	/**
	 * 根据给定名称设定RunTime文档</br>
	 * RunTime文档会覆盖其他相同路径的文档</br>
	 * 
	 * @param doc 文档文本
	 * @param path 文档名称
	 * @return 如果设置成功即返回true，否则返回false
	 */
	public static boolean setRunTimeDoc(String doc,String path)
	{
		RunTimeDoc.put(path, doc);
		return false;
	}
	/**
	 * 从指定路径获取文档文本</br>
	 * 自动选择从JAR中获取或从文件获取</br>
	 * JAR中的文档优先级高于文件</br>
	 * RunTime中的文档优先级高于JAR
	 * @param path 文档路径
	 * @return 文档文本,若文档不存在，返回null
	 */
	public static String getDoc(String path)
	{
		String re;
		re  = readFromRunTime(path);
		if(re == null)
		{re = readFromJar(path);}
		if(re == null)
		{re = readFromFile(path);}
		return re;
	}
}
