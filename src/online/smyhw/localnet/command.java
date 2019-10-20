package online.smyhw.localnet;

import java.net.*;

import online.smyhw.localnet.lib.*;

import java.io.*;

public class command
{
	public static void local(String command) throws Exception
	{
		message.info("[命令解释器]:开始解析:"+command);
		switch(CommandFJ.fj(command,0))
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
		case "mc_p":
			command_mc_p.main(command);
		default:
			message.show("未知指令!");
		}
	}
	public static void net(String ID,String command)
	{
		switch(CommandFJ.fj(command,0))
		{
		case "test":
			command_test.main(command);
			break;
		case "show":
			message.show("来自 "+ID+"的文本信息："+CommandFJ.fj(command,1));
			break;
		default:
			message.show(ID+"传输了一个未知指令："+command);
		}
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
		message.info("尝试连接至"+CommandFJ.fj(input,1)+":4201");
		Socket client = new Socket(CommandFJ.fj(input,1),4201);
		message.info("连接成功，尝试建立数据流");
		DataInputStream in = new DataInputStream(client.getInputStream());
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		message.info("数据流建立成功，读取对方ID");
		ID=in.readUTF();
		message.show(CommandFJ.fj(input,1)+"报告他的ID为："+ID);
		message.show("向"+ID+"发送本终端ID"+localnet.ID);
		out.writeUTF(localnet.ID);
		temp=in.readUTF();
		message.info(CommandFJ.fj(input,1)+"(报告ID为"+ID+")发来报文"+temp);
		if(temp.equals("OK"))
		{
			out.writeUTF(CommandFJ.fj((CommandFJ.fj(input,-1)),-1));
		}
		client.close();
	}
}

class command_mcow
{
	public static void main(String input) throws Exception
	{
		switch(CommandFJ.fj(input,1))
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
			String player_name = new String(CommandFJ.fj(input, 2));
			if(player_name.equals("error"))
			{
				message.show("玩家ID不合法！");
				return;
			}
			File ml = new File(".\\plugins\\Essentials\\userdata");
			String player_file_name[] = ml.list();
			File player_file;
			int player_v;
			int i=0;
			System.out.println("1");
			while(true)
			{
				System.out.println("2");
				if(i==player_file_name.length)
				{
					message.show("找遍了"+player_file_name.length+"个玩家，但就是没找到叫“"+player_name+"”的...");
					break;
				}
				player_file = new File(".\\plugins\\Essentials\\userdata\\"+player_file_name[i]);
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
//						System.out.println("gg");
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
		case "st":
			{
				Socket s = null;
				try 
         		{
					s = new Socket("mc.smyhw.online",4202);
					message.info("建立连接:服务器状态");
	          		DataInputStream input_1 = new DataInputStream(s.getInputStream());
					DataOutputStream output = new DataOutputStream(s.getOutputStream());
					output.writeUTF("#st");
					String IP=input_1.readUTF();
					String status = input_1.readUTF();
					String member = input_1.readUTF();
					String TPS = input_1.readUTF();
					message.show("服务器状态检测：\nIP："+IP+"\n状态："+status+"\n在线人数:"+member+"\nTPS："+TPS);
					s.close();
	          	}
	          	catch(Exception e)
	          	{
	          		message.show("服务器状态检测：\nIP：mc.smyhw.online:25565\n状态：离线\n在线人数:0/0\nTPS：0");
	          		s.close();
	          	}
			}
			break;
		case "dh":
			configer config = new configer("config");
			if(config.get("dh")==1)
			{config.set("dh",0);message.show("死亡信息显示已关闭");}
			else
			{config.set("dh",1);message.show("死亡信息显示已开启");}
			break;
		case "help":
			message.show("命令列表\n"
						+"!!st——查询服务器状态\n"
						+"!!pl——列出在线列表\n"
//						+"!!vc <玩家ID>——查询玩家的货币数量\n"
						+"!!mo <玩家ID>——查询玩家死亡次数，不加玩家ID则显示服务器死亡榜\n"
						+"!!dh——开关死亡显示\n"
						+"------\n"
						+"localnet信息系统 by smyhw"
						);
			break;
		default:
			message.show("未知指令！\n使用!!help获取帮助列表");
		}
	}
}

class command_mc_p
{
	static String IP;
	public static void main(String input) throws Exception
	{
		IP=CommandFJ.fj(input,1);
		int port=1650;
		String re;
		while(true)
		{
//			if(port==65535) {return;}
			re=WebAPI.get("https://status.mctalks.com/return.php?address="+IP+"&port="+port);
			message.info("扫描端口："+port);
			if(!re.startsWith("无法连接至服务器")) 
			{
				if(re.indexOf("状态:下线")!=-1) {port++;continue;}
				message.show(port+">>"+re);
			}
			port++;
		}
	}
}