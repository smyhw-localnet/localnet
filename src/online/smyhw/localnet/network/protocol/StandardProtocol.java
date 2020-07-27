package online.smyhw.localnet.network.protocol;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.network.Client_sl;

/**
 * 这是一个标准协议接口</br>
 * 
 * @author smyhw
 *
 */
public interface StandardProtocol 
{
	//发送数据
	public void SendData(DataPack data);
	//断开连接
	public void Disconnect();
}
