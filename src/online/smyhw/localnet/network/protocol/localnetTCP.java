package online.smyhw.localnet.network.protocol;

import online.smyhw.localnet.data.DataPack;
import online.smyhw.localnet.lib.Exception.Json_Parse_Exception;
import online.smyhw.localnet.lib.Exception.TCP_LK_Exception;
import online.smyhw.localnet.lib.TCP_LK;
import online.smyhw.localnet.message;
import online.smyhw.localnet.network.Client_sl;

import java.net.Socket;
import java.util.List;

public class localnetTCP extends TCP_LK implements StandardProtocol {

    public Client_sl client;

    /**
     * list参数表</br>
     * 1. socket实例 </br>
     * 2. 参数为1作为服务端，2则为客户端(该项仅影响心跳包收发)</br>
     * 不传入第二个参数则默认为服务端</br>
     * tips:</br>
     * 服务端仅响应心跳，并在一段时间没有收到心跳后断开连接</br>
     * 客户端不会响应心跳，但是会定时发送心跳包</br>
     *
     * @param input
     * @param sy
     */
    public localnetTCP(List input, Client_sl sy) {
        super((Socket) input.get(0), input.size() > 1 ? (int) input.get(1) : 1);//这里，调用父类构造方法
        this.client = sy;
    }

    public void SendData(DataPack input) {
        String send = input.getStr();
        Smsg(send);
    }

    public void CLmsg(String msg) {
        //这里要将接收到的信息以HashMap的形式回传给上游
        DataPack re;
        try {
            re = new DataPack(msg);
        } catch (Json_Parse_Exception e) {
            message.warning("客户端发送了无法解析的信息");
            return;
        }
        this.client.on_recv(re);
    }

    public void Serr_u(TCP_LK_Exception e) {
        this.client.on_error(e);
        return;
    }

    public void Disconnect() {
        this.isERROR = true;
    }

}