package online.smyhw.localnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.network.Client_sl;
import online.smyhw.localnet.network.local_sl;

public class helper 
{
	/**
	 * 初始化回调方法
	 */
	public static void call_back(){}
	
	
	/**
	 * 向给定终端发送消息
	 * @param id 终端ID
	 * @param msg 消息内容
	 * @return 返回1时，终端不存在，否则成功执行
	 */
	public static int send_to_someone(String id,String msg) {
		Client_sl tmp1 = Find_Client(id);
		if(tmp1==null) {return 1;}
		tmp1.sendMsg(msg);
		return 0;
	}
	
	/**
	 * 获取所有的在线终端
	 * @return
	 */
	public static List<Client_sl> get_terminals_list(){
		return (ArrayList<Client_sl>) LN.client_list.clone();
	}
	
	/**
	 * 
	 * 将信息发送到所有连接到本地的客户端</r>
	 * @param msg 要发送的信息
	 */
	@Deprecated
	public static void SendAll(String msg)
	{
		message.info("sendALL:"+msg);
		ArrayList<Client_sl> temp1 = (ArrayList<Client_sl>) LN.client_list.clone();
		Iterator<Client_sl> temp2 = temp1.iterator();
		while(temp2.hasNext())
		{
			Client_sl temp3 = temp2.next();
			message.info("SENDALL_ID:"+temp3.remoteID);
			temp3.sendMsg(msg);
		}
	}
	
	/**
	 * 指定一个发送终端</br>
	 * 信息将被发送为以这个终端为发送者的转发(forward)消息</br>
	 * 同时，指定的终端并不会收到这个消息
	 * @param msg 要发送的消息
	 * @param Sender 指定的发送人
	 */
	@Deprecated
	public static void SendAll(String msg,Client_sl Sender)
	{
		message.info("sendALL:"+msg);
		ArrayList<Client_sl> temp1 = (ArrayList<Client_sl>) LN.client_list.clone();
		Iterator<Client_sl> temp2 = temp1.iterator();
		while(temp2.hasNext())
		{
			Client_sl temp3 = temp2.next();
			if(temp3==Sender) {continue;}
			HashMap<String,String> send = new HashMap<String,String>();
			send.put("type", "forward_message");
			send.put("From", Sender.remoteID);
			send.put("message", msg);
			temp3.sendData(new DataPack(send));
		}
	}
	
	
	
	/**
	 * 
	 * 检查传入的ID是否已经存在
	 * @param UserName 要检查的ID
	 * @return 如果为true,则已存在
	 */
	public static boolean ID_repeat(String UserName)
	{
		if(UserName.equalsIgnoreCase(LN.ID)) {return true;}
		ArrayList<Client_sl> temp1 = (ArrayList<Client_sl>) LN.client_list.clone();
		Iterator<Client_sl> temp2 = temp1.iterator();
		while(temp2.hasNext())
		{
			Client_sl temp3 = temp2.next();
			if(temp3.remoteID.equalsIgnoreCase(UserName)) {return true;}
		}
		return false;
	}
	
	/**
	 * 
	 * 检查出入的ID是否符合要求</br>
	 * [要求]:仅包含大小写字母和数字
	 * @param UserName 要检查的ID
	 * @return 如果符合，返回true,不然返回false
	 */
	public static boolean ID_rightful(String UserName)
	{
		if(UserName.matches("^[A-Za-z0-9]+$")) {return true;}
		else {return false;}
	}
	
	/**
	 * 
	 * 检查输入的ID是否合法（是否重复，是否符合正则）
	 * @param UserName
	 * @return 为true则合法
	 */
	public static boolean ID_test(String UserName)
	{
		if(!ID_repeat(UserName) && ID_rightful(UserName)) {return true;}
		else {return false;}
	}
	
	/**
	 * 根据用户ID返回Client_sl实例</br>
	 * 不会使用带有线程安全的doclient方法,所以不会消耗太多性能,效率也不低XD</br>
	 * @param ID 用户ID
	 * @return 用户的Client_sl实例,如果用户不存在,则返回null
	 */
	public static Client_sl Find_Client(String ID)
	{
		
		ArrayList<Client_sl> temp1 = (ArrayList<Client_sl>) LN.client_list.clone();
		Iterator<Client_sl> temp2 = temp1.iterator();
		while(temp2.hasNext())
		{
			Client_sl temp3 = temp2.next();
			if(temp3.remoteID.equals(ID)) {return temp3;}
		}
		return null;
	}
	
	/**
	 * 检查数据包是否合法</br>
	 * 即检查是否包含必要节点</br>
	 * 例如,如果type=auth,那么ID节点必须存在</br>
	 * 如果type=note,那么NoteType节点和NoteText节点必须存在</br>
	 * 	@param input 需要检查的数据包
	 * @return 如果没有问题，则返回true,否则返回false
	 */
	public static Boolean CheckMapNode(HashMap<String,String> input)
	{
		String type = (String) input.get("type");
		if(type==null) {return false;}
		switch(type)
		{
		case"auth":
			if(!input.containsKey("ID")) {return false;}
			break;
		case"note":
			if(!input.containsKey("NoteType")) {return false;}
			if(!input.containsKey("NoteText")) {return false;}
			break;
		case"command":
			if(!input.containsKey("CmdText")) {return false;}
			break;
		case"connect":
			if(!input.containsKey("operation")) {return false;}
			break;
		case"message":
			if(!input.containsKey("message")) {return false;}
			break;
		default:
			return true;
		}
		return true;
	}
	
	static Client_sl local_client;
	/**
	 * 获取本地虚终端
	 * 不存在于终端列表中
	 * 可以存取ClientData
	 * 向其发送的message和note会打印到控制台中，但其他类型的消息会报错
	 * @return 本地虚拟终端
	 */
	public static Client_sl get_local_client() {
		if(local_client==null) {
			local_client = new local_sl();
		}
		return local_client;
	}
}	
