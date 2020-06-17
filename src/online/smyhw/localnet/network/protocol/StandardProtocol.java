package online.smyhw.localnet.network.protocol;

import java.net.Socket;
import java.util.HashMap;

/**
 * 这是一个标准协议接口</br>
 * 
 * @author smyhw
 *
 */
public interface StandardProtocol 
{
	//根据socket连接客户端
//	public void Connect(Socket socket);
	//发送数据
	public void SendData(HashMap<String,String> msg);
	//断开连接
	public void Disconnect();
}
