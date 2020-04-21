package online.smyhw.localnet.lib;

public class json {
	public static String jx(String input,String find)
	{
//		int xh=0;
		String find_msg=("\""+find+"\": \"");
//		message.show(input+"?"+find_msg);
		int b = input.indexOf(find_msg);
		b=b+find_msg.length();
//		message.show("aaa");
		int e = input.indexOf('\"',b+1);
//		message.show(b+":bbb:"+e);
		String re = input.substring(b, e);
//		message.show("vvv");
		return re;
	}
}
