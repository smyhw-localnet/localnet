package online.smyhw.localnet;

import java.net.Socket;

import online.smyhw.localnet.lib.TCP_LK;

public class Client_sl extends TCP_LK
{
	
//	Boolean ISln = false;
	
	String ID;
	public Client_sl(Socket s)
	{
		super(s,1);//这里，调用父类构造方法
		try
		{
			this.Smsg("&"+localnet.ID);//发送自身ID

		}catch(Exception e){message.info(" 客户端\""+ID+"\"连接异常！丢弃线程"+e.getMessage());e.printStackTrace();return;}

	}
	
	/**
	 * 
	 * 警告！这个构造方法是为了给本地用户一个标准输出而“特殊”编写的方法，</br>
	 * 会构造出一个“半死不活”的实例，它是“不正规”的，</br>
	 * 除非你知道它的原理，否则不要用它来构造用户！</br>
	 * （用它来构造个虚拟用户似乎也不坏？）</br>
	 * @param type
	 * @author smyhw
	 */
	public Client_sl(int type)
	{
		super(new Socket(),1);

	}
	public void CLmsg(String msg)
	{
		if(ID==null && !(msg.startsWith("&")))
		{this.Smsg("!1客户端，请先报告你的ID!");return;}
		switch(msg.charAt(0))
		{
		case '/':
			//不允许使用指令，以后可能会添加
//			localnet.set_re=1;//打开回显记录器
//			message.re=new String("smyhwOS");//重置回显记录器
//			this.Smsg(message.getre());//发送命令回显
			break;
		case '$':
			break;
		case '&':
			if(ID!=null) {this.Smsg("!1请误重复鉴权!");return;}
			msg=msg.replace('&', ' ');
			msg=msg.trim();
			if(LNlib.ID_repeat(msg)) 
			{
				message.show("!1ID重复！");
				return;
			}
			if(!LNlib.ID_rightful(msg))
			{
				message.show("!1ID不合法！");
				return;
			}
			this.ID=msg;
//			System.out.println("aaa");
			online.doclient(1, this, 0);
			break;
		default:
			localnet.mdata(ID, msg);
			break;
		}
	}
	public void Serr_u()
	{		
		StackTraceElement[] temp=Thread.currentThread().getStackTrace();
		StackTraceElement temp2=(StackTraceElement)temp[3];
		online.doclient(0, this, 0);
		message.warning("一个连接出错！丢弃！，位置："+temp2.getFileName()+":"+temp2.getClassName()+":"+temp2.getMethodName()+":"+temp2.getLineNumber());
		return;
	}
	
}