package online.smyhw.localnet;

public class message
{
	public static String re[]=new String[999];//当localnet.set_re==1时，这个字符串数组将会记录命令输出值
	static int re_num=0;
	public static String[] getre()
	{
		re[re_num]=null;
		re_num=0;
		return re;
	}
	public static void show(String input)
	{
		if(localnet.set_re==1)
		{
			re[re_num]=input;
			re_num++;
		}
		System.out.println(input);
	}
	public static void info(String input)
	{
		if(localnet.set_debug==1)
		{
			System.out.println(localnet.time.toString()+"[info]"+input);
			log(localnet.time.toString()+"[info]"+input);
		}
	}
	public static void warning(String input)
	{
		if(localnet.set_debug==1)
		{
			System.out.println(localnet.time.toString()+"[warning]"+input);
			log(localnet.time.toString()+"[warning]"+input);
		}
	}
	public static void error(String input)
	{
		if(localnet.set_debug==1)
		{
			System.out.println(localnet.time.toString()+"[error]"+input);
			log(localnet.time.toString()+"[error]"+input);
		}
	}
	static void log(String input)
	{
	}
}