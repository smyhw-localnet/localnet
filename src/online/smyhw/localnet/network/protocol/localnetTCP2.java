package online.smyhw.localnet.network.protocol;

import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

import online.smyhw.localnet.message;
import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.lib.Exception.Json_Parse_Exception;
import online.smyhw.localnet.network.Client_sl;

public class localnetTCP2 implements StandardProtocol {
	Client_sl client;
	online.smyhw.localnet.lib.localnetTCP2 lntcp;
	
	public localnetTCP2(List input,Client_sl sy) {
		this.client = sy;
		try {
			Method recv_md = this.getClass().getMethod("recv_method", String.class);
			Method err_md = this.getClass().getMethod("error_method", String.class,Exception.class);
			this.lntcp = new online.smyhw.localnet.lib.localnetTCP2((Socket) input.get(0),this,recv_md,this,err_md);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			client.on_error(e);
		}
	}

	@Override
	public void SendData(DataPack data) {
		this.lntcp.send_msg(data.getStr());
	}

	@Override
	public void Disconnect() {
		//TODO 断开连接
	}
	
	public void recv_method(String msg){
		try {client.on_recv(new DataPack(msg));} 
		catch (Json_Parse_Exception e) {message.warning("终端<"+client.remoteID+">发送的数据解码失败<"+msg+">", e);}
	}
	
	public void error_method(String msg,Exception e) {
		message.warning("终端<"+client.remoteID+">连接异常<"+msg+">", new Exception(msg));
		client.on_error(e);
	}
}
