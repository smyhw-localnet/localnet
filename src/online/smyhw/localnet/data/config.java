package online.smyhw.localnet.data;

import java.util.HashMap;
import online.smyhw.localnet.message;

public class config implements java.io.Serializable
{
	protected HashMap<String,String> main_data = new HashMap<String,String>();
	
	private static final long serialVersionUID = 7;
	
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
	protected String get(String key)
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
	@Deprecated
	public int get_int(String key)
	{
		String sre = get(key);
		if(sre==null) 
		{
			message.info("config类get_int方法:给定的key查询为null,返回0");
			return 0;
		}
		int re = Integer.parseInt(sre);
		return re;
	}
	
	/**
	 * @see get_int
	 * @param key
	 * @return
	 */
	@Deprecated
	public double get_double(String key)
	{
		String sre = get(key);
		if(sre==null) 
		{
			message.info("config类get_douoble方法:给定的key查询为null,返回0");
			return 0;
		}
		double re = Double.valueOf(sre);
		return re;
	}
	
	/**
	 * @see get_int
	 * @param key
	 * @return
	 */
	@Deprecated
	public String get_String(String key)
	{
		String re = get(key);
		if(re==null) 
		{
			message.info("config类get_String方法:给定的key查询为null,返回空字符串");
			return "";
		}
		return re;
	}
	
	/**
	 * @see get_int
	 * @param key
	 * @return
	 */
	public boolean get_boolean(String key)
	{
		String sre = get(key);
		if(sre==null) {return false;}
		if(sre.equalsIgnoreCase("true"))
		{return true;}else {return false;}
		
	}
	
	/**
	 * @see get_int(String key)
	 * </br>在参考方法的基础上，如果查询到的值为null，则返回传入的第二个参数
	 * @param key
	 * @return
	 */
	public int get_int(String key, int if_null)
	{
		String sre = get(key);
		if(sre==null) 
		{
			return if_null;
		}
		int re = Integer.parseInt(sre);
		return re;
	}
	
	public String get_String(String key,String if_null)
	{
		String re = get(key);
		if(re==null) 
		{
			return if_null;
		}
		return re;
		
	}
	
	public boolean get_boolean(String key,boolean if_null)
	{
		String sre = get(key);
		if(sre==null) {return if_null;}
		if(sre.equalsIgnoreCase("true"))
		{return true;}else {return false;}
		
	}
	
	public double get_double(String key,double if_null)
	{
		String sre = get(key);
		if(sre==null) 
		{
			return if_null;
		}
		double re = Double.valueOf(sre);
		return re;
	}
}
