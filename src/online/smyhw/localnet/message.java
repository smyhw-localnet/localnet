package online.smyhw.localnet;

public class message
{
	public static String re=new String();//当localnet.set_re==1时，这个字符串数组将会记录命令输出值
	public static String getre(String command)
	{
		
		return re;
	}
	/**
	 * @author smyhw
	 * 必要时调用，不进行任何处理，且可能会被getre()方法记录
	 * 对外接口时可能会用得到吧。。。毕竟你不希望getre出来的结果还带个"[info]"的开头2333
	 * @param input
	 */
	public static void show(String input)
	{
		if(localnet.set_re==1){re=re.concat("\n"+input);}
		System.out.println(input);
		log(input);
	}
	public static void info(String input)
	{
//		if(localnet.set_re==1){re=re.concat("\n"+input);}
		if(localnet.set_debug==1)
		{
			input = "[info]"+"["+Thread.currentThread().getStackTrace()[2].getClassName()+":"+Thread.currentThread().getStackTrace()[2].getMethodName()+":"+Thread.currentThread().getStackTrace()[2].getLineNumber()+"]"+input;
			System.out.println(input);
		}
		else
		{
//			input = "[info]"+input;
		}
		
		log(input);
	}
	public static void warning(String input)
	{
		if(localnet.set_debug==1)
		{
			input = "[warning]"+"["+Thread.currentThread().getStackTrace()[2].getClassName()+":"+Thread.currentThread().getStackTrace()[2].getMethodName()+":"+Thread.currentThread().getStackTrace()[2].getLineNumber()+"]"+input;
		}
		else
		{
			input = "[warning]"+input;
		}
		System.out.println(input);
		log(input);
	}
	public static void error(String input)
	{

		if(localnet.set_debug==1)
		{
			input="[error]"+"["+Thread.currentThread().getStackTrace()[2].getClassName()+":"+Thread.currentThread().getStackTrace()[2].getMethodName()+":"+Thread.currentThread().getStackTrace()[2].getLineNumber()+"]"+input;
		}
		else
		{
			input="[error]"+input;
		}
		System.out.println(input);
		log(input);
	}
	/**
	 * 仅在用户输入线程中使用，不换行输出,不计入log
	 * @param input
	 */
	public static void input(String input)
	{
		System.out.print(input);
	}
	static void log(String input)
	{
	}
}