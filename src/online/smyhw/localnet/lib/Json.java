package online.smyhw.localnet.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import online.smyhw.localnet.lib.Exception.Json_Parse_Exception;
/**
 * 该类被设计为处理标准Json信息</br>
 * 
 * @author smyhw
 */
public class Json 
{
	
	/**
	 * 解析JSON字符串
	 * @param input JSON字符串
	 *@throws Json_Parse_Exception 当传入的Json信息无法解析时
	 */
	public static HashMap<String,String> Parse(String input) throws Json_Parse_Exception
	{
		HashMap<String,String> re = new HashMap<String,String>();
//		if(!input.startsWith("{")) {return null;};
//		input = input.substring(1);
//		input = input.substring(0, input.length()-1);
		char[] str = input.toCharArray();
		String key="",value="";
		int type = 0;//type==0#键;type==1#值
		int stru = 0;//stru==0#构造字符;stru==1#数据字符
		for(int i=0;i<str.length;i++)
		{
			if(i>0 && str[i]=='"' && str[i-1]!='\\')//加前置条件i>0是为了防止检测第0个字符的前一位(i-1)导致异常
			{//如果检测到有效的双引号，则切换stru
				if(stru==1) {stru=0;}
				else {stru=1;}
				continue;
			}
			if(stru==0) 
			{//如果读取的是构造字符...
			 //需要考虑逗号问题
				if(str[i]=='{') {continue;}
				if(str[i]=='}') 
				{//处于构造字符的大括号代表字符串结束
				 //注意,这里别忘了保存最后一个键值对
					type=0;
					key = Decoded(key);
					value = Decoded(value);
					re.put(key, value);
					key="";
					value="";
					return re;	
				}
		     	if(str[i]==':') {type=1;continue;}//表示接下来读取的是值
		     	if(str[i]==',') 
		     	{//表示一个键值对已经完成，提交到HashMap
					type=0;
					key = Decoded(key);
					value = Decoded(value);
					re.put(key, value);
					key="";
					value="";
					continue;
		     	}
		     	
		     	//能处理到这，说明这个构造字符是tm非法的,直接返回null,表示错误数据
//		     	return null;
		     	throw new Json_Parse_Exception();
			}
			else
			{//如果读取的是数据
				if(type==0)
				{//如果读取的是键
					key = key+str[i];
					continue;
				}
				else
				{//如果读取的是值
					value=value+str[i];
					continue;
				}
			}
		}
		key = Decoded(key);
		value = Decoded(value);
		re.put(key, value);
//		message.show(re.toString());
		return re;
	}
	
	/**
	 * 根据HashMap构造JSON字符串
	 * @param input 
	 * @return
	 */
	public static String Create(HashMap<String,String> input)
	{
		String re = "{";
		Iterator<Entry<String, String>> temp1 = input.entrySet().iterator();
		while( temp1. hasNext() )
		{
			Entry<String, String> temp2 =temp1.next();
			String key = temp2.getKey();
			String value = temp2.getValue();
			key = Encoded(key);
			value = Encoded(value);
			re = re+"\""+key+"\":\""+value+"\",";
		}
		re = re.substring(0, re.length()-1);
		re = re+"}";
		return re;
	}
	
	
	
	/**
	 * 用于转义特殊字符</br>
	 * <\>(反斜杠)</br>
	 * <">(双引号)</br>
	 * 都会被转义</br>
	 * @param input 未转义的字符串
	 * @return 转义后的字符串
	 */
	public static String Encoded(String input)
	{
//		message.info("en++"+input);
		char[] str = input.toCharArray();
		ArrayList<Character> out_str = new ArrayList<Character>();
		ArrayList<Character> key_word = new ArrayList<Character>();
		key_word.add('\\');
		key_word.add('"');
		for(int i=0;i<str.length;i++)
		{
			if(key_word.contains(str[i])) 
			{
				out_str.add('\\');
			}
			out_str.add(str[i]);
		}
		String re = "";
		for(int i = 0 ;i<out_str.size();i++)
		{
			re = re.concat(out_str.get(i)+"");
		}
//		message.info("en--"+re);
		return re;
	}
	
	/**
	 * 反转义特殊字符
	 * @param input
	 * @return
	 * @see public static String Encoded(String input)
	 */
	public static String Decoded(String input)
	{
//		message.info("de++"+input);
		char[] str = input.toCharArray();
		ArrayList<Character> out_str = new ArrayList<Character>();
		ArrayList<Character> key_word = new ArrayList<Character>();
		key_word.add('\\');
		key_word.add('"');
		for(int i=0;i<str.length;i++)
		{
			if(str[i]=='\\' && key_word.contains(str[i+1])) 
			{
				i=i+1;
			}
			out_str.add(str[i]);
		}
		String re = "";
		for(int i = 0 ;i<out_str.size();i++)
		{
			re = re.concat(out_str.get(i)+"");
		}
//		message.info("de--"+re);
		return re;
	}
}
