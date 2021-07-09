package online.smyhw.localnet.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import online.smyhw.localnet.LN;
import online.smyhw.localnet.message;

public class NetWorkManager 
{
	
	
	public static void bind(int port,String protocol)
	{
		try 
		{
			LN.online_threads.add(new OnlineThread(port,protocol));
		} catch (Exception e) 
		{
			message.warning("[网络管理器]监听端口失败!", e);
		}
	}
	
	
	public static Client_sl connect(String ip,int port)
	{
		try 
		{
			List temp = new ArrayList();
			temp.add(new Socket(ip,port));
			temp.add(2);
			return new Client_sl("localnetTCP",temp);
			
		} 
		catch (Exception e) 
		{
			message.warning("[网络管理器]连接至终端\""+ip+":"+port+"\"时出错！");
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 
	 * 操作客户端列表</br>
	 * 这个方法是线程安全的</br>
	 * 因为线程安全，所以可能会阻塞！</br>
	 * type==0	将信息移除list</br>
	 * type==1	将信息加入list</br>
	 * type==2	根据索引查询信息</br>
	 * type==4	根据索引插入信息</br>
	 * type==7	根据索引移除信息</br>
	 * </br>
	 * !如果任何一个参数不需要,则该参数可以为任何值(可以为null)</br>
	 * @param lj
	 */
	public synchronized static Client_sl doclient(int type,Client_sl lj,int sy)
	{
		Client_sl re = null;
		switch(type)
		{
		case 0:
			LN.client_list.remove(lj);
			message.info("[列表]终端<"+lj.remoteID+">删除");
			break;
		case 1:
			LN.client_list.add(lj);
			message.info("[列表]终端<"+lj.remoteID+">添加");
			break;
		case 2:
			re=LN.client_list.get(sy);
			break;
		case 4:
			LN.client_list.add(sy, lj);
			break;
		case 7 :
			LN.client_list.remove(sy);
			break;
		default:
			message.warning("警告，doclient收到未定义的操作类型<"+type+">，将不会对list做出任何改动！");
			break;
		}
		return re;
	}
	
	/**
	 * 测试协议是否存在
	 * @param protocol
	 * @return
	 */
	public static boolean testProtocol(String protocol)
	{
		try 
		{
			Class.forName(protocol);
			return true;
		}
		catch (ClassNotFoundException e1) 
		{
			try 
			{
				Class.forName("online.smyhw.localnet.network.protocol."+protocol);
				return true;
			}
			catch (ClassNotFoundException e) 
			{
				return false;
			}
		}	
	}
}
