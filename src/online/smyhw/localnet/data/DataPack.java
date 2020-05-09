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
	
	public DataPack(HashMap<String,String> data)
	{
		DataMap=data;
	}
	
	public void add(String key,String value)
	{
		DataMap.put(key, value);
	}
	
	public void del(String key)
	{
		DataMap.remove(key);
	}
	
	public String getStr()
	{
		return Json.Create(DataMap);
	}
	
	public HashMap<String,String> getMap()
	{
		return (HashMap<String,String>) DataMap.clone();
	}
	
	public String getValue(String key)
	{
		return DataMap.get(key);
	}
}
