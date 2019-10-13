package online.smyhw.localnet.lib;

import online.smyhw.localnet.message;

public class CommandFJ {
	public static String fj(String command,int num)
	{
		String re=" ";
		int sy=0;//索引，即现在检查到了哪个字符，以0开头
		int dl=-1;//记录现在检查到了第几个参数，以1开头
		int s=0,e=-1;//当前参数开始处索引以及结束处索引(e=结束处索引+1)
		command=command.trim();//删除前后的空白
		if(command.isEmpty())
		{
			message.info("[命令解释器]:空指令！");
			return " ";
		}
		command=command+" ";//尾部加空格，防止单节指令无法读到空格而出错
		//添加特殊功能
		if(num==-1)//砍掉指令第一段
		{
			return command.substring(command.indexOf(' ')+1);
		}
		//一般分解过程
		while(true)
		{
			if(command.length()==sy){break;}
			if(command.charAt(sy)==' ')
			{
				s=e+1;
				e=sy;
				dl++;
				if(dl==num)//返回字符串 
				{
					try 
					{
						re=command.substring(s,e);
					}
					catch(IndexOutOfBoundsException ee)
					{
						return "error";
					}
					return re;
				}
			}
			sy++;
		}
		return " ";
	}

}
