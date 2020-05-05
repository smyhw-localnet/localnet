package online.smyhw.localnet.data;

import java.util.HashMap;

import online.smyhw.localnet.message;

public class data 
{
	protected HashMap<String,Object> main_data = new HashMap<String,Object>();
	
	
	/**
	 * 
	 * 线程安全的方法,对该实例的数据表进行设置维护</br>
	 * 可以将value置为null来删除该key</br>
	 * 参数不用我解释了吧...
	 * @param key 
	 * @param value
	 * 
	 */
	protected synchronized void p_set(String key,String value)
	{
		if(value==null) 
		{
			main_data.remove(key);
		}
		main_data.put(key, value);
	}
	
	public void set(String key,Object value)
	{
		 p_set(key,value.toString());
	}
	
	/**
	 * 
	 * 从该实例的数据表中读取数据</br>
	 * 参数不用我解释了吧...
	 * @param key 
	 * 
	 */
	public Object get(String key)
	{
		return main_data.get(key);
	}
	/**
	 * 
	 * 从给定的key中读出int类型数据</br>
	 * 如果给定的key不存在,则返回0</br>
	 * 请不要使用这个方法去读取其他类型的数据,鬼知道会返回什么
	 * @param key
	 * @return
	 * 
	 */
	public int get_int(String key)
	{
		Object sre = get(key);
		if(sre==null) 
		{
			message.info("config类get_int方法:给定的key查询为null,返回0");
			return 0;
		}
		int re = (Integer)sre;
		return re;
	}
	
	/**
	 * @see get_int
	 * @param key
	 * @return
	 */
	public double get_double(String key)
	{
		Object sre = get(key);
		if(sre==null) 
		{
			message.info("config类get_douoble方法:给定的key查询为null,返回0");
			return 0;
		}
		double re = (Double)sre;
		return re;
	}
	
	/**
	 * @see get_int
	 * @param key
	 * @return
	 */
	public String get_String(String key)
	{
		Object sre = get(key);
		if(sre==null) 
		{
			message.info("config类get_douoble方法:给定的key查询为null,返回空String");
			return "";
		}
		String re = (String)sre;
		return re;
	}
	
	/**
	 * @see get_int
	 * @param key
	 * @return
	 */
	public boolean get_boolean(String key)
	{
		Object sre = get(key);
		return (boolean)sre;
	}
	
	public HashMap<String,Object> GetClone()
	{
		HashMap<String,Object> re  = (HashMap<String, Object>) main_data.clone();
		return re;
	}
}
