package online.smyhw.localnet.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import online.smyhw.localnet.message;

/**
 * 该类被设计为处理标准Json信息</br>
 * 
 * @author smyhw
 */
public class Json 
{
	
	public static HashMap<String,String> Parse(String input)
	{
		HashMap<String,String> re = new HashMap<String,String>();
		if(!input.startsWith("{")) {return null;};
		input = input.substring(1);
		input = input.substring(0, input.length()-1);
		char[] str = input.toCharArray();
		String key="",value="";
		int type = 0;
		for(int i=0;i<str.length;i++)
		{
			if(type==0) 
			{
				if(str[i]!=':') {key=key+str[i];}
				else {type=1;}
			}
			else
			{
				if(str[i]!=',') {value=value+str[i];}
				else 
				{
					type=0;
					key = Decoded(key);
					value = Decoded(value);
					re.put(key, value);
					key="";
					value="";
				}
			}
		}
		key = Decoded(key);
		value = Decoded(value);
		re.put(key, value);
		message.show(re.toString());
		return re;
	}
	
	public static String Create(HashMap<String,String> input)
	{
		String re = "{";
		Iterator temp1 = input.entrySet().iterator();
		while( temp1. hasNext() )
		{
			String key = (String) temp1.next();
			String value = (String) input.get(key);
			key = Encoded(key);
			value = Encoded(value);
			re = re+key+":"+value+",";
		}
		re = re.substring(0, re.length()-1);
		re = re+"}";
		return re;
	}
	
	
	
	/**
	 * 用于转义特殊字符</br>
	 * Json的6大构造字符：</br>
	 * begin-array = ws %x5B ws ; [ 左方括号</br>
	 * begin-object = ws %x7B ws ; { 左大括号</br>
	 * end-array = ws %x5D ws ; ] 右方括号</br>
	 * end-object = ws %x7D ws ; } 右大括号</br>
	 * name-separator = ws %x3A ws ; : 冒号</br>
	 * value-separator = ws %x2C ws ; , 逗号</br>
	 * 以及转义字符“\”(反斜杠)</br>
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
		key_word.add('{');
		key_word.add('}');
		key_word.add('[');
		key_word.add(']');
		key_word.add(';');
		key_word.add(':');
		key_word.add('\\');
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
		key_word.add('{');
		key_word.add('}');
		key_word.add('[');
		key_word.add(']');
		key_word.add(';');
		key_word.add(':');
		key_word.add('\\');
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
