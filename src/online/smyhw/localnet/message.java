package online.smyhw.localnet;

public class message
{
	public static void show(String input)
	{
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