package online.smyhw.localnet.data;

import java.util.HashMap;
import java.util.Set;

import online.smyhw.localnet.lib.Json;
import online.smyhw.localnet.lib.Exception.Json_Parse_Exception;


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
	
	/**
	 * 使用HashMap创建一个数据包</br>
	 * 这会浅表clone这个传入参数，对传入参数的后续改变可能会影响这个数据包
	 * @param data
	 */
	public DataPack(HashMap<String,String> data)
	{
		DataMap=(HashMap<String, String>) data.clone();
	}
	
	/**
	 * 根据Json创建数据包
	 * @param jsonData
	 * @throws Json_Parse_Exception 当传入的Json信息无法解析时
	 */
	public DataPack(String jsonData) throws Json_Parse_Exception
	{
		DataMap = Json.Parse(jsonData);
	}
	
	/**
	 * 添加键值对
	 * @param key
	 * @param value
	 */
	public void add(String key,String value)
	{
		DataMap.put(key, value);
	}
	
	/**
	 * 根据键删除值
	 * @param key
	 */
	public void del(String key)
	{
		DataMap.remove(key);
	}
	
	/**
	 * 获取该数据包JSON格式的文本
	 * @return
	 */
	public String getStr()
	{
		return Json.Create(DataMap);
	}
	
	/**
	 * 返回这个数据包的HashMap形式</br>
	 * 这是一个深层clone，对返回对象的任何操作均不会影响本数据包
	 * @return
	 */
	public HashMap<String,String> getMap()
	{
		HashMap<String,String> re = new HashMap<String,String>();
		Set<String> keys =  DataMap.keySet();
		for(String key:keys)
		{
			String rkey = new String(key);
			String value = new String(DataMap.get(key));
			re.put(rkey, value);
		}
		return re;
	}
	
	/**
	 * 根据键获取值
	 * @param key
	 * @return
	 */
	public String getValue(String key)
	{
		return DataMap.get(key);
	}
}
