package online.smyhw.localnet.lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 该类被设计为处理标准Json信息</br>
 * 
 * @author smyhw
 */
public class Json 
{
	
	/**
	 * 用于转义特殊字符</br>
	 * Json的6大构造字符：</br>
	 * begin-array = ws %x5B ws ; [ 左方括号</br>
	 * begin-object = ws %x7B ws ; { 左大括号</br>
	 * end-array = ws %x5D ws ; ] 右方括号</br>
	 * end-object = ws %x7D ws ; } 右大括号</br>
	 * name-separator = ws %x3A ws ; : 冒号</br>
	 * value-separator = ws %x2C ws ; , 逗号</br>
	 * @param input 未转义的字符串
	 * @return 转义后的字符串
	 */
	public static String Encoded(String input)
	{
		
		char[] str = input.toCharArray();
		ArrayList<Character> out_str = new ArrayList();
		ArrayList<Character> key_word = new ArrayList();
		key_word.add('{');
		key_word.add('}');
		key_word.add('[');
		key_word.add(']');
		key_word.add(';');
		key_word.add(':');
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
			re.concat(out_str.get(i)+"");
		}
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
		return null;
	}
}
