package online.smyhw.localnet;

import java.net.*;

import online.smyhw.localnet.lib.*;

import java.io.*;

public class command
{
	public static void local(String command) throws Exception
	{
		message.info("[命令解释器]:开始解析:"+command);
		switch(fj(command,0))
		{
		case "test":
			command_test.main(command);
			break;
		case "to":
			command_to.main(command);
			break;
		case "mcow":
			command_mcow.main(command);
			break;
		default:
			message.show("未知指令!");
		}
	}
	public static void net(String ID,String command)
	{
		switch(fj(command,0))
		{
		case "test":
			command_test.main(command);
			break;
		case "show":
			message.show("来自 "+ID+"的文本信息："+fj(command,1));
			break;
		default:
			message.show(ID+"传输了一个未知指令："+command);
		}
	}
	static String fj(String command,int num)
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



class command_test
{
	public static void main(String input)
	{
		String temp = (WebAPI.get("https://top.timewk.cn/api/blacklist/query/1577050642"));
		message.show("gg");
		message.show(json.jx(temp,"from"));
		message.show("test指令执行成功");
	}
}

class command_to
{
	static String ID,temp;
	public static void main(String input) throws Exception
	{
		message.info("尝试连接至"+command.fj(input,1)+":4201");
		Socket client = new Socket(command.fj(input,1),4201);
		message.info("连接成功，尝试建立数据流");
		DataInputStream in = new DataInputStream(client.getInputStream());
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		message.info("数据流建立成功，读取对方ID");
		ID=in.readUTF();
		message.show(command.fj(input,1)+"报告他的ID为："+ID);
		message.show("向"+ID+"发送本终端ID"+localnet.ID);
		out.writeUTF(localnet.ID);
		temp=in.readUTF();
		message.info(command.fj(input,1)+"(报告ID为"+ID+")发来报文"+temp);
		if(temp.equals("OK"))
		{
			out.writeUTF(command.fj((command.fj(input,-1)),-1));
		}
		client.close();
	}
}

class command_mcow
{
	public static void main(String input) throws Exception
	{
		switch(command.fj(input,1))
		{
		case"pl":
			BufferedReader temp = new BufferedReader(new FileReader("E:\\pl"));
			String temp1;
			System.out.println("ga");
			while(true)
			{
				temp1=temp.readLine();
				if(temp1==null) {break;}
				message.show(temp1);
			}
			System.out.println("gg");
			temp.close();
			break;
		case"vc":
			String player_name = new String(command.fj(input, 2));
			if(player_name.equals("error"))
			{
				message.show("玩家ID不合法！");
				return;
			}
			File ml = new File("E:\\OurWorld\\plugins\\Essentials\\userdata");
			String player_file_name[] = ml.list();
			File player_file;
			int player_v;
			int i=0;
			while(true)
			{
				if(i==player_file_name.length)
				{
					message.show("找遍了"+player_file_name.length+"个玩家，但就是没找到叫“"+player_name+"”的...");
					break;
				}
				player_file = new File("E:\\OurWorld\\plugins\\Essentials\\userdata\\"+player_file_name[i]);
				BufferedReader player_file_reader = new BufferedReader(new FileReader(player_file));
				System.out.println("读取文件："+player_file);
				while(true)
				{
					temp1=player_file_reader.readLine();
					System.out.println("读取行："+temp1);
					if(temp1==null) {System.out.println("文件读取错误！(玩家名称)");break;}
					if((temp1.startsWith("lastAccountName")) && (player_name.equals(temp1.substring(17))))
					{
						while(true)
						{
							temp1=player_file_reader.readLine();
							System.out.println("读取货币数量。。。读取行："+temp1);
							if(temp1==null) {System.out.println("文件读取错误！（玩家货币）");break;}
							if(temp1.startsWith("money:"))
							{
								player_v=Double.valueOf(temp1.substring(8,temp1.length()-1)).intValue();
								message.show("玩家"+player_name+"拥有"+player_v+"货币");
								return;
							}
						}
					}
					else 
					{
						System.out.println("gg");
						if((temp1.startsWith("lastAccountName")) && (!(player_name.equals(temp1.substring(17)))))
						{System.out.println("找到玩家ID："+temp1.substring(17)+"与需求不符");break;}
						if(!(temp1.startsWith("lastAccountName")))
						{System.out.println("玩家ID不在这一行");continue;}
						System.out.println("在处理一个文件时遇到位置原因。。。跳过这个文件");
						break;
					}
//					break;
				}
				player_file_reader.close();
				i++;
			}
			break;
			//
		}
	}
}