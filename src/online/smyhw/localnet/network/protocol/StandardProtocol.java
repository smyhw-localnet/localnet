package online.smyhw.localnet.network.protocol;

import online.smyhw.localnet.data.DataPack;

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
