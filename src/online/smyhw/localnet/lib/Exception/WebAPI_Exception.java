package online.smyhw.localnet.lib.Exception;

public class WebAPI_Exception  extends Exception 
{
	private static final long serialVersionUID = 7;
	public int type;
	String url = "";
	public Exception upEXP;
	public WebAPI_Exception(int type,String url)
	{
		
		
	}
}
