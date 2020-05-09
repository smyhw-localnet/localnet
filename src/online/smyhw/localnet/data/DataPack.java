package online.smyhw.localnet.data;

import java.util.HashMap;

import online.smyhw.localnet.lib.Json;


/**
 * 该类被设计成一个LocalNet的标准传输数据单元</br>
 * 内含一个数据包 
 * @author smyhw
 *
 */
public class DataPack implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7;
	HashMap<String,String> DataMap = new HashMap<String,String>();
	
	public DataPack(String data)
	{
		
	}
	
	public DataPack(HashMap data)
	{
		DataMap=data;
	}
	
	public void add(String key,String value)
	{
		
	}
	
	public void del(String key)
	{
		
	}
	
	public String getStr()
	{
		return Json.Create(DataMap);
	}
	
	public HashMap getMap()
	{
		return (HashMap) DataMap.clone();
	}
}
